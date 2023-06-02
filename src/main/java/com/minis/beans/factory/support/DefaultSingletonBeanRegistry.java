package com.minis.beans.factory.support;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的单例：可以替换。
 * 除了作为 SingletonBeanRegistry 外，它主要是额外补充了查询Bean依赖于被依赖问题。
 * Field：1 注册列表 2 单例容器 3 依赖注入相关信息x2 （依赖 + 被依赖）
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {
    protected List<String> beanNames = new ArrayList();
    // 容器中存放所有bean的名称的列表
    protected Map<String, Object> singletonObjects = new ConcurrentHashMap(256);
    // 容器中存放所有bean实例的map

    // 这是为后续扩展做准备的，管理Bean的依赖！
    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap(64);
    // beanName与dependentBeans（Set<>）。一个bean，对应依赖的所有Set<bean>。
    protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap(64);

    @Override public void registerSingleton(String beanName, Object singletonObject) {
        /**
         * 将 singletonObjects 定义为了一个 **ConcurrentHashMap**，
         * 而且在实现 registrySingleton 时前面加了一个关键字 **synchronized**。
         */
        synchronized (this.singletonObjects) {
            // IoC3新增
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                        "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            // IoC2原有
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
            // IoC3新增
            System.out.println(" bean registerded............. " + beanName);
        }
    }

    @Override public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
    }

    @Override public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override public String[] getSingletonNames() {
        return (String[]) this.beanNames.toArray();
    }

    public void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.beanNames.remove(beanName);
        }
    }

    // 这是为后续扩展做准备的，管理Bean的依赖！IOC3全部填充

    /**
     *
     * @param beanName 一个bean的名称
     * @param dependentBeanName 它依赖的某个bean
     *                          TODO IoC3
     */
    public void registerDependentBean(String beanName, String dependentBeanName) {
        // 根据 beanName 查询 Map，得到 Set<>。
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        // 如果 dependentBeans(Set) 不为空，且已包含dependentBeanName，直接返回
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
            return;
        }

        // No entry yet -> fully synchronized manipulation of the dependentBeans Set
        // dependentBeans(Set) 1 为空 2 不为空但不包含dependentBeanName
        synchronized (this.dependentBeanMap) {
            dependentBeans = this.dependentBeanMap.get(beanName);
            // dependentBeans(Set) 1 为空，先将Set<>给new出来。并创建好映射。dependentBeanMap。
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<String>(8);
                this.dependentBeanMap.put(beanName, dependentBeans);
            }
            // dependentBeans(Set) 2 不为空但不包含dependentBeanName，添加。
            dependentBeans.add(dependentBeanName);
        }
        // 根据 dependentBeanName 查询 Map，得到 Set<>。
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            // dependenciesForBean(Set) 1 为空，先将Set<>给new出来。并创建好映射。dependenciesForBeanMap。
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<String>(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            // dependenciesForBean(Set) 2 不为空但不包含beanName，添加。
            dependenciesForBean.add(beanName);
        }

    }

	public boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }

	public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return (String[]) dependentBeans.toArray();
    }

	public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return (String[]) dependenciesForBean.toArray();
    }
}
