package com.zhishu.interceptors;

import com.zhishu.common.dto.Page;
import com.zhishu.config.CachePageConfig;
import com.zhishu.utils.ParameterObjectUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * 解决开启二级缓存后无法获取分页数据的缓存处理器
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
        CachingExecutor cachingExecutor = (CachingExecutor) invocation.getTarget();
        //获取jdbc执行器
        MappedStatement mappedStatement = (MappedStatement) args[0];
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        //获取缓存key
        CacheKey cacheKey = cachingExecutor.createCacheKey(mappedStatement, args[1], (RowBounds) args[2], boundSql);
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

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
