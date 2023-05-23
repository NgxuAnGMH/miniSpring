package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的单例：可以替换
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    protected List<String> beanNames = new ArrayList();
    // 容器中存放所有bean的名称的列表
    protected Map<String, Object> singletonObjects = new ConcurrentHashMap(256);
    // 容器中存放所有bean实例的map

    // 这是为后续扩展做准备的，管理Bean的依赖！
    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap(64);
    protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap(64);

//    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
        }
    }

//    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

//    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

//    @Override
    public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.beanNames.remove(beanName);
        }
    }

    // 这是为后续扩展做准备的，管理Bean的依赖！
    protected void registerDependentBean(String beanName, String dependentBeanName) {
    }
    protected boolean hasDependentBean(String beanName) {
        return false;
    }
    protected String[] getDependentBeans(String beanName) {
        return null;
    }
    protected String[] getDependenciesForBean(String beanName) {
        return null;
    }
}
