package com.zhishu.common.exception;


import com.zhishu.common.enums.exception.ErrorCode;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 全局异常基类
 *
 * @author huangfu
 */
public interface IException {
    /**
     * 返回运行枚举
     *
     * @return 返回错误枚举
     */
    ErrorCode getErrorCode();

    /**
     * 获取错误信息
     *
     * @param throwable 异常信息
     * @return 标准的堆栈异常
     */
    static String getMessage(Throwable throwable) {
        StringWriter str = new StringWriter();
        PrintWriter pw = new PrintWriter(str);
        throwable.printStackTrace(pw);
        return str.toString();
    }
}
