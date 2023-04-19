package com.minis.beans;

/**
 * @author: cmx
 * @date: 2023/04/19
 * @classname: BeanFactory
 * 不负责区分具体 Resource
 */
public interface BeanFactory {
	Object getBean(String beanName) throws NoSuchBeanDefinitionException;
	void registerBeanDefinition(BeanDefinition bd);

}
