package com.zhishu.common.enums.exception;

public enum MyBatisPageErrorCode implements ErrorCode {
    ;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }

    @Override
    public String toString() {
        return String.format("Code:【%s】,Description:【%s】", code, msg);
    }
}
