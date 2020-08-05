package com.zhishu.samples;

import com.zhishu.boot.annotations.EnableMyBatisPagePlugin;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMyBatisPagePlugin
@MapperScan("com.zhishu.samples.mapper")
public class MyBatisPluginBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBatisPluginBootApplication.class,args);
    }
}
