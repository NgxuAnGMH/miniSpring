package com.minis.beans.factory.support;

import com.minis.beans.factory.exception.BeansException;

/**
 * 不负责区分具体 Resource。
 * Bean工厂
 */
public interface BeanFactory {
    Object getBean(String beanName) throws BeansException;
	// void registerBeanDefinition(BeanDefinition bd); 原来IoC1的方法

	// IoC2所有新增改动如下。
	boolean containsBean(String name);	// 是否有这个Bean

	// void registerBean(String beanName, Object obj);
	// 注册方法被分配给了【仓库接口】BeanDefinitionRegistry.java

	boolean isSingleton(String name);	// 这个Bean是单例？
	boolean isPrototype(String name);	// 这个Bean是原型？
	Class<?> getType(String name);		// 获取Bean的类型。

}
