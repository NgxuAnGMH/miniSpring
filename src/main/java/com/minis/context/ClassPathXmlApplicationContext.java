package com.minis.context;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.NoSuchBeanDefinitionException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory{
	BeanFactory beanFactory;

	//context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
    public ClassPathXmlApplicationContext(String fileName){
    	Resource res = new ClassPathXmlResource(fileName);
    	BeanFactory bf = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(res);
        this.beanFactory = bf;
    }

	//context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
	public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
		return this.beanFactory.getBean(beanName);
	}


	public void registerBeanDefinition(BeanDefinition bd) {
		this.beanFactory.registerBeanDefinition(bd);
		
	}

    
}
