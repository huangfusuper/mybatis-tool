package com.zhishu.processors.executors;

import com.zhishu.common.dto.Page;
import com.zhishu.config.CachePageConfig;
import com.zhishu.utils.ParameterObjectUtil;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;

/**
 * 缓存处理器
 * @author huangfu
 */
@SuppressWarnings("all")
@EqualsAndHashCode
public class CachingExecutorsPageProcessors implements ExecutorsPageProcessors {
    @Override
    public Object invoker(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        //获取参数
        Object[] args = invocation.getArgs();
        CachingExecutor cachingExecutor = (CachingExecutor) invocation.getTarget();
        //获取执行器
        Executor executor = (Executor) SystemMetaObject.forObject(cachingExecutor).getValue("delegate");
        //获取jdbc执行器
        MappedStatement mappedStatement = (MappedStatement) args[0];
        BoundSql boundSql = mappedStatement.getBoundSql(args[1]);
        //获取缓存key
        CacheKey cacheKey = executor.createCacheKey(mappedStatement, args[1], (RowBounds) args[2], boundSql);
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
        return result;
    }

    @Override
    public Class<? extends Executor> getExecutor() {
        return CachingExecutor.class;
    }
}
