package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeanDefinition;

/**
 * 需要集中存放BeanDefinition，新增一个相应的管理接口。
 * 实现该接口的类就像是一个存放 BeanDefinition 的仓库，
 * 可以操作：存放、移除、获取及判断 BeanDefinition 对象。
 * 注意：register注册方法，现在分配到了这个接口中。(原本是BeanFactory.java)
 */
public interface BeanDefinitionRegistry {
    // 注册
    void registerBeanDefinition(String name, BeanDefinition bd);

    // 删除
    void removeBeanDefinition(String name);

    // 获取
    BeanDefinition getBeanDefinition(String name);

    // 判断是否已注册
    boolean containsBeanDefinition(String name);

}
