package com.zhishu.common.enums.exception;

/**
 * mybatis错误枚举
 * @author huangfu
 */
public enum MyBatisPageErrorCode implements ErrorCode {
    /**
     * 不支持该类数据库类型
     */
    NOT_SUPPORT_DB_TYPE("not support db type","分页工具暂不支持该数据库类型，如有需要请反馈至github issue！"),
    ;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String msg;

    MyBatisPageErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

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
