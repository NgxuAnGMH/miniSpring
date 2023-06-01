package com.minis.beans.factory.config;

import com.minis.beans.factory.support.ListableBeanFactory;

/**
 * IoC5：BeanFactory体系
 * ConfigurableListable：仅仅是一个集成接口！
 */
public interface ConfigurableListableBeanFactory
		extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

}
