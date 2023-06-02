package com.minis.beans.factory.config;

import com.minis.beans.factory.support.BeanFactory;
import com.minis.beans.factory.support.SingletonBeanRegistry;

/**
 * IoC5：BeanFactory体系
 * Configurable 1 Bean的存在模式（单例/原型） 2 支持额外的Bean处理器 3 维护Bean之间的依赖关系
 * 但这里的BeanPostProcessor，仅有 add 和 get。没有apply！
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {
	// Bean的存在模式是单例还是原型。
	String SCOPE_SINGLETON = "singleton";
	String SCOPE_PROTOTYPE = "prototype";
	// 支持额外的Bean处理器
	void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
	int getBeanPostProcessorCount();
	// 维护Bean之间的依赖关系
	void registerDependentBean(String beanName, String dependentBeanName);
	String[] getDependentBeans(String beanName);
	String[] getDependenciesForBean(String beanName);
}

