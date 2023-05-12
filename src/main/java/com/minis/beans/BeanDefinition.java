package com.minis.beans;


public class BeanDefinition {
    private String id;          // 接口引用，对象的名称
    private String className;   // 具体实现，生成单例的模板
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    
    public BeanDefinition(String id, String className) {
        this.id = id;
        this.className = className;
    }
}
