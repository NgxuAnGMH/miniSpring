package com.minis.beans;

public interface BeanDefinitionRegistry {
	// 注册
	void registerBeanDefinition(String name, BeanDefinition bd);
	// 删除
	void removeBeanDefinition(String name);
	// 获取
	BeanDefinition getBeanDefinition(String name);
	// 判断是否已注册
	boolean containsBeanDefinition(String name);
}
