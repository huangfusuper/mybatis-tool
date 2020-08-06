package com.zhishu.interceptors;

import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.*;

/**
 * 分页缓存清理
 *
 * @author huangfu
 */
@Intercepts({@Signature(type = Executor.class, method = "clearLocalCache", args = {})})
public class CacheEmptyPageInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        CachingExecutor cachingExecutor = (CachingExecutor) invocation.getTarget();
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof CachingExecutor) {
            return Plugin.wrap(target, this);
        }
        return target;
    }
}
