package com.zhishu.processors.impl;

import com.zhishu.common.dto.Page;
import com.zhishu.processors.PageSqlFormatProcessor;

/**
 * oracle分页sql格式化器
 * @author huangfu
 */
public class OraclePageFormatProcessorPage implements PageSqlFormatProcessor {

    @Override
    public String sqlConversion(String olSql, Page<?> page) {
        int offset = (page.getPageNo() - 1) * page.getPageSize() + 1;
        return String.format("select * from (select oraclePageFormat.*,rownum r from (%s) oraclePageFormat where rownum < %s) where r >= %s", olSql, (offset + page.getPageSize()), offset);
    }
}
