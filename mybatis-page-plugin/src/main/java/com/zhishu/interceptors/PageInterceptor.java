package com.zhishu.interceptors;

import com.zhishu.common.dto.Page;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

/**
 * 分页插件拦截器
 * @see Intercepts 该注解用于指定拦截的方法 type为要拦截的类  method为拦截的该类里面的方法 args为该方法的参数类型
 * @author huangfu
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@SuppressWarnings("all")
public class PageInterceptor implements Interceptor {

    /**
     * 拦截方法回调数据
     * @param invocation 拦截数据
     * @return 代理后的逻辑
     * @throws Throwable 最终结果集
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        //获取所有的sql包
        BoundSql boundSql = statementHandler.getBoundSql();
        //获取所有的参数
        Object parameterObject = boundSql.getParameterObject();
        Page page = null;
        if(parameterObject instanceof Page) {
            page = (Page) parameterObject;
        }else if(parameterObject instanceof Map){
            page = (Page) ((Map) parameterObject).values().stream().filter(v -> v instanceof Page).findFirst().orElse(null);
        }

        if(page != null) {
            int totalCount = selectCount(invocation);
            page.setTotalRecord(totalCount);
        }
        //直接放行  执行原有的方法逻辑
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
    }

    /**
     * 获取总条数
     * @param invocation 拦截数据
     * @return 该sql的总条数
     */
    private int selectCount(Invocation invocation) throws SQLException {
        int totalCount = 0;
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        //获取原生的sql
        String sql = boundSql.getSql();
        //拼装查询sql
        String countSql = String.format("select count(*) from %s _page_count", sql);
        //获取数据库链接
        Connection connection = (Connection) invocation.getArgs()[0];
        //获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(countSql);
        //设置原来的参数
        statementHandler.getParameterHandler().setParameters(preparedStatement);
        //开始执行
        ResultSet resultSet = preparedStatement.executeQuery();
        //获取总数
        if(resultSet.next()) {
            totalCount = resultSet.getInt(1);
        }
        resultSet.close();
        preparedStatement.close();
        return totalCount;
    }
}
