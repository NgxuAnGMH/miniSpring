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
import com.minis.beans.BeansException;
import com.minis.beans.NoSuchBeanDefinitionException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * @author: cmx
 * @date: 2023/04/19
 * @classname: ClassPathXmlApplicationContext
 * @see BeanFactory
 * 应用上下文，虽然也是继承BeanFactory，但其本身只负责在构造函数中，启动初始化，整合容器的启动过程，
 * 调用<u>BeanDefinitionReader</u>，之后绑定一个业务无关的BeanFactory成员，通过委托的方式，让其代劳。
 *
 * ApplicationContext有点像策略模式，不过其本身不仅持有BeanFactory，还继承自BeanFactory，
 * 结果是通过委托代理回给BeanFactory的方式，实现继承方法的重写。
 * 意思是其成员变量BeanFactory，就像策略一样，可以根据业务需要随时替换。
 */
public class ClassPathXmlApplicationContext implements BeanFactory,ApplicationEventPublisher{
	// 原来是BeanFactory beanFactory;
	SimpleBeanFactory beanFactory;

	//context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
	public ClassPathXmlApplicationContext(String fileName){
		Resource res = new ClassPathXmlResource(fileName);
		SimpleBeanFactory bf = new SimpleBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
		reader.loadBeanDefinitions(res);
		this.beanFactory = bf;
	}

	// context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
//	@Override
	public Object getBean(String beanName) throws BeansException {
		return this.beanFactory.getBean(beanName);
	}

//	@Override
	public boolean containsBean(String name) {
		return this.beanFactory.containsBean(name);
	}

	//	IoC1原来是
//	public void registerBeanDefinition(BeanDefinition bd) {
//		this.beanFactory.registerBeanDefinition(bd);
	public void registerBean(String beanName, Object obj) {
		this.beanFactory.registerBean(beanName, obj);
	}

//	@Override
	public void publishEvent(ApplicationEvent event) {
	}

//	@Override
	public boolean isSingleton(String name) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
	public boolean isPrototype(String name) {
		// TODO Auto-generated method stub
		return false;
	}

//	@Override
	public Class<?> getType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

}
