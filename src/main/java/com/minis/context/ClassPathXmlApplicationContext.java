package com.minis.context;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeansException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
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
public class ClassPathXmlApplicationContext implements BeanFactory,ApplicationEventPublisher{
															// IoC2：新增实现了事件发布接口。
	// 原来是BeanFactory beanFactory; 或 SimpleBeanFactory beanFactory;

	// IoC4新增
	AutowireCapableBeanFactory beanFactory;
	private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors =
			new ArrayList<BeanFactoryPostProcessor>();

	//context负责整合容器的启动过程，读外部配置，解析Bean定义，创建BeanFactory
	public ClassPathXmlApplicationContext(String fileName){
    	this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh){
		Resource res = new ClassPathXmlResource(fileName);
//		IoC4注释掉 SimpleBeanFactory bf = new SimpleBeanFactory();
		AutowireCapableBeanFactory bf = new AutowireCapableBeanFactory();
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

	// context再对外提供一个getBean，底下就是调用的BeanFactory对应的方法
	public Object getBean(String beanName) throws BeansException {
		return this.beanFactory.getBean(beanName);
	}

	public boolean containsBean(String name) {
		return this.beanFactory.containsBean(name);
	}

	//	IoC1原来是
//	public void registerBeanDefinition(BeanDefinition bd) {
//		this.beanFactory.registerBeanDefinition(bd);
	public void registerBean(String beanName, Object obj) {
		this.beanFactory.registerBean(beanName, obj);
	}

	public void publishEvent(ApplicationEvent event) {
	}

	public boolean isSingleton(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPrototype(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	public Class<?> getType(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	// IoC4 新增以下所有

	public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
		return this.beanFactoryPostProcessors;
	}

	public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
		this.beanFactoryPostProcessors.add(postProcessor);
	}

	public void refresh() throws BeansException, IllegalStateException {
		// Register bean processors that intercept bean creation.
		registerBeanPostProcessors(this.beanFactory);

		// Initialize other special beans in specific context subclasses.
		onRefresh();
	}

	private void registerBeanPostProcessors(AutowireCapableBeanFactory bf) {
		//if (supportAutowire) {
		bf.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
		//}
	}

	private void onRefresh() {
		this.beanFactory.refresh();
	}


}
