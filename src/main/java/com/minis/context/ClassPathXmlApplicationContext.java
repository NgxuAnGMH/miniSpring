package com.minis.context;

import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.BeanFactory;
import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;

import com.minis.beans.factory.config.AbstractAutowireCapableBeanFactory;

import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

import java.util.ArrayList;
import java.util.List;

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
public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
	DefaultListableBeanFactory beanFactory;
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
			new ArrayList<BeanFactoryPostProcessor>();

	//context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
	public ClassPathXmlApplicationContext(String fileName){
    	this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh){
		Resource res = new ClassPathXmlResource(fileName);
    	DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
		reader.loadBeanDefinitions(res);
		this.beanFactory = bf;
		// IoC3 新增：在 Spring 体系中，Bean 是结合在一起同时创建完毕的
		// 用一个 refresh() 就将整个 IoC 容器激活了，运行起来，加载所有配置好的 Bean
        if (isRefresh) {
			try {
				refresh();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (BeansException e) {
				e.printStackTrace();
			}
		}
	}

	// IoC5新增
	void registerListeners() {
		ApplicationListener listener = new ApplicationListener();
		this.getApplicationEventPublisher().addApplicationListener(listener);

	}

	void initApplicationEventPublisher() {
		ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
		this.setApplicationEventPublisher(aep);
	}

	void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
	}

	void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
		this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
	}



	void onRefresh() {
		this.beanFactory.refresh();
	}

	@Override
	public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
		return this.beanFactory;
	}

	@Override
	public void addApplicationListener(ApplicationListener listener) {
		this.getApplicationEventPublisher().addApplicationListener(listener);

	}

	@Override
	void finishRefresh() {
		publishEvent(new ContextRefreshEvent("Context Refreshed..."));

	}

	@Override
	public void publishEvent(ApplicationEvent event) {
		this.getApplicationEventPublisher().publishEvent(event);

	}

}
