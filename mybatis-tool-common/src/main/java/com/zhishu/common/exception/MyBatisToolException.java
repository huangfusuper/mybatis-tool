package com.zhishu.common.exception;


import com.zhishu.common.enums.exception.ErrorCode;

/**
 * mybatis通用异常
 *
 * @author huangfu
 */
public class MyBatisToolException extends RuntimeException implements IException {
    private final ErrorCode errorCode;

    /**
     * 错误信息传递
     *
     * @param errorCode    异常信息枚举
     * @param errorMessage 具体的错误信息
     */
    public MyBatisToolException(ErrorCode errorCode, String errorMessage) {
        super(String.format("%s - %s", errorCode.toString(), errorMessage));
        this.errorCode = errorCode;
    }

    /**
     * 错误信息传递
     *
     * @param errorCode 异常信息枚举
     * @param cause     异常堆栈载体
     */
    public MyBatisToolException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.toString() + "-" + IException.getMessage(cause));
        this.errorCode = errorCode;
    }

    /**
     * 错误信息传递
     *
     * @param errorCode    错误信息枚举
     * @param errorMessage 异常信息枚举
     * @param cause        异常堆栈载体
     */
    public MyBatisToolException(ErrorCode errorCode, String errorMessage, Throwable cause) {
        super(errorCode.toString() + "-" + errorMessage + "-" + IException.getMessage(cause));
        this.errorCode = errorCode;
    }

    /**
     * 异常转换
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @return 自定义异常信息
     */
    public static MyBatisToolException asMyBatisToolException(ErrorCode errorCode, String errorMessage) {
        return new MyBatisToolException(errorCode, errorMessage);
    }

    /**
     * 异常转换
     *
     * @param errorCode    错误码
     * @param errorMessage 错误信息
     * @param cause        真实的异常信息
     * @return 自定义异常信息
     */
    public static MyBatisToolException asMyBatisToolException(ErrorCode errorCode, String errorMessage, Throwable cause) {
        if (cause instanceof MyBatisToolException) {
            return (MyBatisToolException) cause;
        }
        return new MyBatisToolException(errorCode, errorMessage, cause);
    }

    /**
     * 异常转换
     *
     * @param errorCode 错误码
     * @param cause     真实异常信息
     * @return 自定义异常信息
     */
    public static MyBatisToolException asMyBatisToolException(ErrorCode errorCode, Throwable cause) {
        if (cause instanceof MyBatisToolException) {
            return (MyBatisToolException) cause;
        }
        return new MyBatisToolException(errorCode, cause);
    }

    @Override
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
