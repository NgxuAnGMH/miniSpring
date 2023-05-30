package com.minis.beans;

/**
 * 就我们之前，是针对`<beans><bean>`这两对标签，现在自己新增了 `<property>` 和 `<constructor-args>` 标签，
 * 所以我们需要完成这方面的定义工作。
 * 这里是构造器注入的属性类。单个
 * 构造器参数(类)！
 */
public class ArgumentValue {

    private String type;    // 属性类型type
    private String name;    // 属性名称name
    private Object value;    // 赋值value
    // 不同于PropertyValue，这里不用增加 isRef
    // 因为构造器处理Ref会让循环依赖问题无法解决。


    public ArgumentValue(String type, Object value) {
        this.value = value;
        this.type = type;
    }

    public ArgumentValue(String type, String name, Object value) {
        this.value = value;
        this.type = type;
        this.name = name;
    }

    // 以下是getter和setter


    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}

