package com.minis.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value={ElementType.METHOD})
// 修饰方法
@Retention(RetentionPolicy.RUNTIME)
// 在运行时生效
public @interface RequestMapping {
    String value() default "";
    // @RequestMapping 定义很简单，现在只有 value 一个字段，用来接收配置的 URL。
}
