package com.minis.beans.factory.config;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.support.AbstractBeanFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 继承了AbstractBeanFactory，但没有重写任何一个方法。
 * 意思是：它的重点在于提供额外的Bean处理器增强！
 */
public class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{
	private final List<AutowiredAnnotationBeanPostProcessor> beanPostProcessors = new ArrayList<AutowiredAnnotationBeanPostProcessor>();
	// 用*一个列表 beanPostProcessors* 记录*所有的 Bean 处理器*，这样可以按照需求注册若干个不同用途的处理器，然后调用处理器。
	
	public void addBeanPostProcessor(AutowiredAnnotationBeanPostProcessor beanPostProcessor) {
		this.beanPostProcessors.remove(beanPostProcessor);	// 先去掉
		this.beanPostProcessors.add(beanPostProcessor);		// 后添加
	}
	public int getBeanPostProcessorCount() {
		return this.beanPostProcessors.size();
	}
	public List<AutowiredAnnotationBeanPostProcessor> getBeanPostProcessors() {
		return this.beanPostProcessors;
	}

	public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		for (AutowiredAnnotationBeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			beanProcessor.setBeanFactory(this);
			// **对每个 Bean 处理器**，调用方法 postProcess`Before`Initialization。
			result = beanProcessor.postProcessBeforeInitialization(result, beanName);
			if (result == null) {
				return result;
			}
		}
		return result;
	}

	public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
			throws BeansException {

		Object result = existingBean;
		for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
			// **对每个 Bean 处理器**，调用方法 postProcess`After`Initialization。
			result = beanProcessor.postProcessAfterInitialization(result, beanName);
			if (result == null) {
				return result;
			}
		}
		return result;
	}
	
}
