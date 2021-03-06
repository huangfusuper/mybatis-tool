package com.zhishu.processors.format;

import com.zhishu.common.dto.Page;

/**
 * mysql分页sql格式化器
 *
 * @author huangfu
 */
public class MyPageSqlPageFormatProcessor implements PageSqlFormatProcessor {

    @Override
    public String sqlConversion(String olSql, Page<?> page) {
        //计算起始位置
        int offset = (page.getPageNo() - 1) * page.getPageSize();
        return String.format("%s limit %s offset %s", olSql, page.getPageSize(), offset);
    }
}
