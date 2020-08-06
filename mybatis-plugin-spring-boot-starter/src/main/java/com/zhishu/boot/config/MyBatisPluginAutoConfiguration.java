package com.zhishu.boot.config;

import com.zhishu.boot.config.properties.MyBatisToolConfigurationProperties;
import com.zhishu.boot.mark.MyBatisPagePluginMark;
import com.zhishu.interceptors.CachePageInterceptor;
import com.zhishu.interceptors.PageInterceptor;
import com.zhishu.interceptors.ResultInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis插件自动配置注入
 * @author huangfu
 */
@Configuration
@EnableConfigurationProperties(MyBatisToolConfigurationProperties.class)
public class MyBatisPluginAutoConfiguration {

    /**
     * 分页插件自动装配
     * @return 分页插件
     */
    @ConditionalOnBean(MyBatisPagePluginMark.class)
    @Bean
    public Interceptor pageInterceptor(){
        return new PageInterceptor();
    }

    /**
     * 分页插件结果期拦截器注册
     * @return 结果集拦截器
     */
    @ConditionalOnBean(MyBatisPagePluginMark.class)
    @Bean
    public Interceptor resultInterceptor(){
        return new ResultInterceptor();
    }

    /**
     * 分页插件 二级缓存拦截器注册
     * @return 结果集拦截器
     */
    @ConditionalOnBean(MyBatisPagePluginMark.class)
    @Bean
    public Interceptor cachePageInterceptor(){
        return new CachePageInterceptor();
    }
}
