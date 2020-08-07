package com.zhishu.interceptors;

import com.zhishu.common.dto.Page;
import com.zhishu.config.CachePageConfig;
import com.zhishu.utils.ParameterObjectUtil;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.cache.TransactionalCacheManager;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 分页数据的缓存处理拦截器
 *
 * @author huangfu
 */
@Intercepts({@Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
@SuppressWarnings("all")
public class CachePageInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取参数
        Object[] args = invocation.getArgs();
        Executor executor = (Executor) invocation.getTarget();
        //获取jdbc执行器
        MappedStatement mappedStatement = (MappedStatement) args[0];
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        //获取缓存key
        CacheKey cacheKey = executor.createCacheKey(mappedStatement, args[1], (RowBounds) args[2], boundSql);
        //验证缓存结构，缓存清理
        cacheEmpty(executor, mappedStatement, cacheKey);

        Page page = CachePageConfig.PAGE_CACHE.get(cacheKey);
        Object result = invocation.proceed();
        //结果集
        if (page != null) {
            //获取分页参数
            Page pageParam = ParameterObjectUtil.getPageParam(args[1]);
            if (pageParam != null) {
                //设置缓存数据
                pageParam.setResults(page.getResults());
                pageParam.setPageNo(page.getPageNo());
                pageParam.setPageSize(page.getPageSize());
                pageParam.setTotalPage(page.getTotalPage());
                pageParam.setTotalRecord(page.getTotalRecord());
            }
        } else {
            page = ParameterObjectUtil.getPageParam(args[1]);
            CachePageConfig.PAGE_CACHE.put(cacheKey, page);
        }
        //执行处理器
        return result;
    }

    /**
     * 刷新分页缓存逻辑
     *
     * @param executor        执行器
     * @param mappedStatement 执行器
     * @param cacheKey        缓存主键
     */
    private void cacheEmpty(Executor executor, MappedStatement mappedStatement, CacheKey cacheKey) {

        //如果是二级缓存
        if (executor instanceof CachingExecutor) {
            Cache cache = mappedStatement.getCache();
            if (cache != null) {
                cachingLogic(executor, cacheKey, cache);
            } else {
                Executor delegate = (Executor) SystemMetaObject.forObject(executor).getValue("delegate");
                perpetualCacheLogic(delegate, cacheKey);
            }
        }
        //当没有开启二级缓存或者当前mapper没有启用二级缓存的时候 就检测一级缓存
        if (!(executor instanceof CachingExecutor)) {
            perpetualCacheLogic(executor, cacheKey);
        }

    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 二级缓存处理逻辑
     * <p>
     * 一个调用时机：
     * 1.开启二级缓存且mapper配置二级缓存
     *
     * @param executor
     * @param cacheKey
     * @param cache
     */
    private void cachingLogic(Executor executor, CacheKey cacheKey, Cache cache) {
        CachingExecutor cachingExecutor = (CachingExecutor) executor;
        TransactionalCacheManager tcm = (TransactionalCacheManager) SystemMetaObject.forObject(cachingExecutor).getValue("tcm");
        Object object = tcm.getObject(cache, cacheKey);
        //如果缓存为空但是page不为空  就删除这个page的缓存
        if (object == null && CachePageConfig.PAGE_CACHE.containsKey(cacheKey)) {
            CachePageConfig.PAGE_CACHE.remove(cacheKey);
            Executor delegate = (Executor) SystemMetaObject.forObject(executor).getValue("delegate");
            perpetualCacheLogic(delegate, cacheKey);
        }
        //如果缓存不为空但是page为空  就删除这个缓存
        if (object != null && !CachePageConfig.PAGE_CACHE.containsKey(cacheKey)) {
            //删除这个缓存
            cache.removeObject(cacheKey);
            tcm.clear(cache);
        }
    }

    /**
     * 一级缓存校验逻辑
     * <p>
     * 调用的三个时机
     * 1.二级缓存不存在
     * 2.mapper没有标识二级缓存
     * 3.没有开启二级缓存
     *
     * @param executor
     * @param cacheKey
     */
    private void perpetualCacheLogic(Executor executor, CacheKey cacheKey) {
        PerpetualCache perpetualCache = (PerpetualCache) SystemMetaObject.forObject(executor).getValue("localCache");
        Object object = perpetualCache.getObject(cacheKey);
        //如果缓存为空但是page不为空  就删除这个page的缓存
        if (object == null && CachePageConfig.PAGE_CACHE.containsKey(cacheKey)) {
            CachePageConfig.PAGE_CACHE.remove(cacheKey);
        }
        //如果缓存不为空但是page为空  就删除这个缓存
        if (object != null && !CachePageConfig.PAGE_CACHE.containsKey(cacheKey)) {
            //删除这个缓存
            perpetualCache.removeObject(cacheKey);
        }
    }
}
