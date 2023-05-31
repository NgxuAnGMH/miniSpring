package com.minis.beans.factory.config;

import com.minis.beans.factory.BeansException;

public interface BeanPostProcessor {
	// Bean初始化之前
	Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;
	// Bean初始化之后
	Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
