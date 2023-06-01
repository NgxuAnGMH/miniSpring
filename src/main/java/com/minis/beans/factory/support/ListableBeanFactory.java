package com.minis.beans.factory.support;

import com.minis.beans.factory.exception.BeansException;

import java.util.Map;

/**
 * IoC5：BeanFactory体系
 * Listable将BeanFactory中所有Bean视为一个集合进行管理。
 */
public interface ListableBeanFactory extends BeanFactory {
	// 判断是否包含Bean
	boolean containsBeanDefinition(String beanName);
	// 获取 Bean 的数量
	int getBeanDefinitionCount();
	// 得到所有 Bean 的定义名单
	String[] getBeanDefinitionNames();
	// 得到所有 Bean 的类型名单
	String[] getBeanNamesForType(Class<?> type);
	// 按照某个类型获取 Bean 列表
	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}

