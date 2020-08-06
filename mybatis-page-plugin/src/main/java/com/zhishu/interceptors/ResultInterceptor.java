package com.zhishu.interceptors;

import com.zhishu.common.dto.Page;
import com.zhishu.utils.ParameterObjectUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 结果拦截器
 *
 * @author huangfu
 */
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
@SuppressWarnings("all")
public class ResultInterceptor implements Interceptor {
    /**
     * 参数映射器名称
     */
    public static final String PARAMETER_HANDLER = "parameterHandler";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取结果集处理器
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        //获取参数映射器
        ParameterHandler parameterHandler = (ParameterHandler) SystemMetaObject.forObject(resultSetHandler).getValue(PARAMETER_HANDLER);
        //获取参数
        Object parameterObject = parameterHandler.getParameterObject();
        //筛选参数里有没有携带分页参数
        Page page = ParameterObjectUtil.getPageParam(parameterObject);
        //获取最终转换的结果集
        Object proceed = invocation.proceed();
        if (page != null) {
            //结果集处理器
            if (proceed instanceof Collection) {
                Collection collection = (Collection) proceed;
                ArrayList results = new ArrayList();
                results.addAll(collection);
                page.setResults(results);
            } else if (proceed instanceof List) {
                page.setResults((List) proceed);
            }
        }
        //正常返回结果集
        return proceed;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
