package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: cmx
 * @date: 2023/04/19
 * @classname: SimpleBeanFactory
 * @see BeanFactory
 * 不负责区分具体 Resource，只负责 名称注册表、信息注册表、单例容器
 */
public class SimpleBeanFactory implements BeanFactory{
	private List<String> beanNames=new ArrayList();
	private List<BeanDefinition> beanDefinitions=new ArrayList();
    private Map<String, Object> singletons =new HashMap();

    public SimpleBeanFactory() {
    }

	//getBean，容器的核心方法，是从单例容器当中，获取已实例化的Bean。
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException{
		// 先尝试直接拿Bean实例
        Object singleton = singletons.get(beanName);
		// 如果此时还没有这个Bean的实例，进一步判断
        if (singleton == null) {
			// 先是从名称注册表当中查找，得到同步的索引。
        	int i = beanNames.indexOf(beanName);
        	if (i == -1) {
        		throw new NoSuchBeanDefinitionException();
        	}
        	else {
				// 通过同步的索引，获取Bean的定义，来创建实例。
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
				// 向单例容器，注册添加Bean实例（singletons是成员变量，Map<String, Object>）
				singletons.put(bd.getId(),singleton);
        	}       	
        }
        return singleton; // 返回实例
    }
    public void registerBeanDefinition(BeanDefinition bd){
		// 名称注册表 与 信息注册表：具有同步关系，即列表索引也是同步的。
		this.beanNames.add(bd.getId());
		this.beanDefinitions.add(bd);
		// 没有生成单例对象，即在需要时才生成，而非一开始就注册添加进容器
    }
    
}
