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
