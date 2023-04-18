package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleBeanFactory implements BeanFactory{
    private List<BeanDefinition> beanDefinitions=new ArrayList();
    private List<String> beanNames=new ArrayList();
    private Map<String, Object> singletons =new HashMap();

    public SimpleBeanFactory() {
    }

	//getBean，容器的核心方法
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException{
		//先尝试直接拿Bean实例
        Object singleton = singletons.get(beanName);
		//如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
        	int i = beanNames.indexOf(beanName);
        	if (i == -1) {
        		throw new NoSuchBeanDefinitionException();
        	}
        	else {
				//获取Bean的定义
        		BeanDefinition bd = beanDefinitions.get(i);
        		try {
					// 这里的singleton是方法中的临时变量，Object类型
            		singleton=Class.forName(bd.getClassName()).newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				//注册Bean实例（singletons才是成员变量，Map<String, Object>）
				singletons.put(bd.getId(),singleton);
        	}       	
        }
        return singleton;
    }
    public void registerBeanDefinition(BeanDefinition bd){
    	this.beanDefinitions.add(bd);
    	this.beanNames.add(bd.getId());
    }
    
}
