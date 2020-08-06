package com.zhishu.processors.executors;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Invocation;

/**
 * 执行器处理器
 *
 * @author huangfu
 */
public interface ExecutorsPageProcessors {

    /**
     * 开始执行核心逻辑
     *
     * @param invocation 参数信息
     * @return 返回执行结果
     * @throws Throwable 异常信息
     */
    Object invoker(Invocation invocation) throws Throwable;

    /**
     * 返回对应的处理器
     *
     * @return 适配的执行器
     */
    Class<? extends Executor> getExecutor();
}
