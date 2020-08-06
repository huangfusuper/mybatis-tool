package com.zhishu.interceptors;

import com.zhishu.common.dto.Page;
import com.zhishu.enums.SqlFormatMatch;
import com.zhishu.processors.format.PageSqlFormatProcessor;
import com.zhishu.utils.ParameterObjectUtil;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.*;

/**
 * 分页插件拦截器
 *
 * @author huangfu
 * @see Intercepts 该注解用于指定拦截的方法 type为要拦截的类  method为拦截的该类里面的方法 args为该方法的参数类型
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@SuppressWarnings("all")
public class PageInterceptor implements Interceptor {

    public static final String PAGE_CPUNT_SQL = "select count(*) from (%s)  _page_count";

    /**
     * 获取数据库信息
     *
     * @param connection 数据库链接对象
     * @return 数据库类型
     */
    private static String getDbType(Connection connection) {
        String dbType = null;
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            dbType = metaData.getDriverName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbType;
    }

    /**
     * 拦截方法回调数据
     *
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
        //筛选参数里有没有携带分页参数
        Page page = ParameterObjectUtil.getPageParam(parameterObject);
        //如果携带分页参数
        if (page == null) {
            return invocation.proceed();
        }
        //获取总条数
        int totalCount = selectCount(invocation);
        //设置分页总条数
        page.setTotalRecord(totalCount);
        Connection connection = (Connection) invocation.getArgs()[0];
        PageSqlFormatProcessor pageSqlFormatProcessor = SqlFormatMatch.match(getDbType(connection));
        //获取分页后的sql
        String newSql = pageSqlFormatProcessor.sqlConversion(boundSql.getSql(), page);
        //替换sql
        SystemMetaObject.forObject(boundSql).setValue("sql", newSql);
        //放行
        return invocation.proceed();
    }

    /**
     * 获取总条数
     *
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
        String countSql = String.format(PAGE_CPUNT_SQL, sql);
        //获取数据库链接
        Connection connection = (Connection) invocation.getArgs()[0];
        //获取预编译对象
        PreparedStatement preparedStatement = connection.prepareStatement(countSql);
        //设置原来的参数
        statementHandler.parameterize(preparedStatement);
        //开始执行
        ResultSet resultSet = preparedStatement.executeQuery();
        //获取总数
        if (resultSet.next()) {
            totalCount = resultSet.getInt(1);
        }
        resultSet.close();
        preparedStatement.close();
        return totalCount;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }
}
