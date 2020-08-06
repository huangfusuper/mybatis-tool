package com.zhishu.adaptations;

import com.zhishu.processors.executors.CachingExecutorsPageProcessors;
import com.zhishu.processors.executors.ExecutorsPageProcessors;
import org.apache.ibatis.executor.Executor;

import java.util.HashSet;
import java.util.Set;

/**
 * 执行器分页缓存处理器适配器
 * @author huangfu
 */
public class ExecutorsPageProcessorsAdaptation {
    /**
     * 处理器适配器集合
     */
    private final static Set<ExecutorsPageProcessors> EXECUTORS_PAGE_PROCESSORS = new HashSet<>(16);

    static {
        EXECUTORS_PAGE_PROCESSORS.add(new CachingExecutorsPageProcessors());
    }

    /**
     * 添加一个执行器分页缓存处理器
     * @param executorsPageProcessors 执行器分页缓存处理器
     */
    public static void addExecutorsPageProcessors(ExecutorsPageProcessors executorsPageProcessors){
        EXECUTORS_PAGE_PROCESSORS.add(executorsPageProcessors);
    }

    /**
     * 执行器适配
     * @param executor 执行器
     * @return 分页缓存处理器
     */
    public static ExecutorsPageProcessors executorsPageProcessorsAdaptation(Executor executor){
        return EXECUTORS_PAGE_PROCESSORS.stream().filter(executorsPageProcessors -> executorsPageProcessors.getExecutor().equals(executor.getClass())).findFirst().orElse(null);
    }
}
