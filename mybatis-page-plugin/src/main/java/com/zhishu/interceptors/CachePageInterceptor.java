package com.zhishu.interceptors;

import com.zhishu.adaptations.ExecutorsPageProcessorsAdaptation;
import com.zhishu.common.dto.Page;
import com.zhishu.config.CachePageConfig;
import com.zhishu.processors.executors.ExecutorsPageProcessors;
import com.zhishu.utils.ParameterObjectUtil;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;
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
        //适配处理器
        ExecutorsPageProcessors executorsPageProcessors = ExecutorsPageProcessorsAdaptation.executorsPageProcessorsAdaptation((Executor) invocation.getTarget());
        //没有预设处理器就执行原有逻辑
        if(executorsPageProcessors == null) {
            return invocation.proceed();
        }
        //执行处理器
        return executorsPageProcessors.invoker(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
