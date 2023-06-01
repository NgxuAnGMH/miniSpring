package com.minis.beans.factory.config;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.support.BeanFactory;

public interface BeanPostProcessor {
	// Bean初始化之前
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
	// Bean初始化之后
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;
	// 设置bf
	void setBeanFactory(BeanFactory beanFactory);
}
