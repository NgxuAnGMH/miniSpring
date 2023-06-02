package com.minis.beans.factory.support;

import com.minis.beans.factory.exception.BeansException;

import java.util.Map;

/**
 * IoC5：BeanFactory体系
 * Listable将BeanFactory中所有Bean视为一个集合进行管理。
 * 跟BeanDefinitionRegistry不同的是，不管理注册、删除、获取的是数量，而不是定义。
 * 这里强调最终获取的是集合。和BeanDefinition不太相关。
 * 返回值：boolean、int、String[]、<T> Map<String, T>
 */
public interface ListableBeanFactory extends BeanFactory {
	// 判断是否包含Bean
	boolean containsBeanDefinition(String beanName);
	// 获取 Bean 的数量、而非定义。
	int getBeanDefinitionCount();
	// 得到所有 Bean 的定义名单
	String[] getBeanDefinitionNames();
	// 得到所有 Bean 的类型名单
	String[] getBeanNamesForType(Class<?> type);
	/**
	 * 按照某个类型获取 Bean 列表
	 * <T>：这个<T> T 表示返回值T的类型是泛型，T是一个占位符，
	 * 用来告诉编译器，这个东西是先给我留着， 等我编译的时候再告诉你是什么类型。
	 */
	<T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}

