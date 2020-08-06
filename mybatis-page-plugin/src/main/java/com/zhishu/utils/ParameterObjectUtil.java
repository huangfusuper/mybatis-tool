package com.zhishu.utils;

import com.zhishu.common.dto.Page;

import java.util.Map;

/**
 * 参数工具类
 *
 * @author huangfu
 */
@SuppressWarnings("all")
public class ParameterObjectUtil {
    /**
     * 从参数对象里面获取分页参数
     *
     * @param parameterObject
     * @return
     */
    public static Page getPageParam(Object parameterObject) {
        Page page = null;
        if (parameterObject instanceof Page) {
            page = (Page) parameterObject;
        } else if (parameterObject instanceof Map) {
            page = (Page) ((Map) parameterObject).values().stream().filter(v -> v instanceof Page).findFirst().orElse(null);
        }
        return page;
    }
}
