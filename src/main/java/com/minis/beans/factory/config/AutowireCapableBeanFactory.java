package com.minis.beans.factory.config;


import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.support.BeanFactory;

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
