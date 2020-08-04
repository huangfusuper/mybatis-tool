package com.zhishu.common.enums.exception;

/**
 * 基类枚举
 *
 * @author huangfu
 */

public interface ErrorCode {
    /**
     * 返回执行码值
     *
     * @return 错误码
     */
    String getCode();

    /**
     * 返回运行信息
     *
     * @return 错误信息
     */
    String getMsg();

    /**
     * 格式化方法
     *
     * @return 格式化后的信息
     */
    @Override
    String toString();
}
