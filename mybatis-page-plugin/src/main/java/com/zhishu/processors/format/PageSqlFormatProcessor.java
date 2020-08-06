package com.zhishu.processors.format;

import com.zhishu.common.dto.Page;

/**
 * sql格式化处理器
 *
 * @author huangfu
 */
public interface PageSqlFormatProcessor {

    /**
     * sql语句格式化器
     *
     * @param olSql 原始sql
     * @param page  分页数据
     * @return 格式化后的sql
     */
    String sqlConversion(String olSql, Page<?> page);
}
