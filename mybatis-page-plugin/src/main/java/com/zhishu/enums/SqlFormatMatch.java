package com.zhishu.enums;

import com.zhishu.common.enums.exception.MyBatisPageErrorCode;
import com.zhishu.common.exception.MyBatisToolException;
import com.zhishu.processors.PageSqlFormatProcessor;
import com.zhishu.processors.impl.MyPageSqlPageFormatProcessor;
import com.zhishu.processors.impl.OraclePageFormatProcessorPage;

import java.util.Arrays;

/**
 * sql语句格式化器匹配器
 * @date  2020年8月5日16:09:37
 * @author huangfu
 */
public enum SqlFormatMatch {
    /**
     * mysql处理器
     */
    MYSQL_PROCESSOR("mysql", new MyPageSqlPageFormatProcessor()),
    /**
     * oracle处理器
     */
    ORACLE_PROCESSOR("oracle", new OraclePageFormatProcessorPage())
    ;
    /**
     * 数据库类型
     */
    private final String dbName;
    /**
     * 对应的sql处理器
     */
    private final PageSqlFormatProcessor pageSqlFormatProcessor;

    SqlFormatMatch(String dbName, PageSqlFormatProcessor pageSqlFormatProcessor) {
        this.dbName = dbName;
        this.pageSqlFormatProcessor = pageSqlFormatProcessor;
    }

    /**
     * 匹配程序
     * @param dbName 数据库名字
     * @date 2020年8月5日16:18:12
     * @return 对应的处理器
     */
    public static PageSqlFormatProcessor match(String dbName) {
        SqlFormatMatch sqlFormatMatch = Arrays.stream(SqlFormatMatch.values())
                .filter(processor -> dbName.toUpperCase().contains(processor.dbName.toUpperCase()))
                .findFirst().orElse(null);
        if(null != sqlFormatMatch){
            return sqlFormatMatch.pageSqlFormatProcessor;
        }
        String message = String.format("【mybatis-page-plugin】暂不支持该数据库【%s】类型！",dbName);
        throw MyBatisToolException.asMyBatisToolException(MyBatisPageErrorCode.NOT_SUPPORT_DB_TYPE,message);
    }
}
