package com.zhishu.boot.annotations;

import com.zhishu.boot.mark.MyBatisPagePluginMark;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 分页插件自动注入启动注解
 * @author huangfu
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Import(MyBatisPagePluginMark.class)
public @interface EnableMyBatisPagePlugin {
}
