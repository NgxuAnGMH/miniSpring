package com.minis.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
// 修饰成员变量（属性）
@Retention(RetentionPolicy.RUNTIME)
// 在运行时生效
public @interface Autowired {

}
