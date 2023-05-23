package com.minis.beans;

public interface SingletonBeanRegistry {
    // 单例注册
    void registerSingleton(String beanName, Object singletonObject);
    // 单例获取
    Object getSingleton(String beanName);
    // 判断是否已注册
    boolean containsSingleton(String beanName);
    // 获取所有已注册的单例：返回名称列表
    String[] getSingletonNames();
}
