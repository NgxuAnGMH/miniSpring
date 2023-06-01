package com.minis.beans.factory.config;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.support.BeanFactory;

public interface BeanFactoryPostProcessor {
	void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;
}
