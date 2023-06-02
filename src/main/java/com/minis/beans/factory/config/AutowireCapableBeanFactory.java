package com.minis.beans.factory.config;


import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.support.BeanFactory;

/**
 * 提供自动装配选项： 1 不装配 2 byName 3 byType
 * 提供初始化前后的Method： 通过应用(apply)BeanPostProcessor！
 * 			注：不同于 ConfigurableBF中的 add & get。
 */
public interface AutowireCapableBeanFactory extends BeanFactory {
	// 自动装配选项
	int AUTOWIRE_NO = 0;
	int AUTOWIRE_BY_NAME = 1;
	int AUTOWIRE_BY_TYPE = 2;

	Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException;

	Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException;

}
