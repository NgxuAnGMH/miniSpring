package com.minis.beans;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeansException;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.BeanDefinitionRegistry;
import com.minis.beans.factory.support.DefaultSingletonBeanRegistry;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: cmx
 * @date: 2023/04/19
 * @classname: SimpleBeanFactory
 * @see BeanFactory
 * 不负责区分具体 Resource，只负责 名称注册表、单例容器
 * 实现了BeanFactory，所以是工厂
 * 实现了BeanDefinitionRegistry，所以是仓库
 * 继承了DefaultSingletonBeanRegistry，所以有一些默认实现
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap(256);
    // 已注册了的，但可能还没有实例化
    private List<String> beanDefinitionNames = new ArrayList();
    // 名称注册表

    // IoC3新增属性：早期的毛胚实例，在实例化与属性注入这两个阶段之间增加一个环节：确保给 Bean 注入属性的时候，Spring 内部已经准备好了 Bean 的实例
    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    public SimpleBeanFactory() {
    }

    // IoC3新增方法
    public void refresh() {
        /**
         * 在 Spring 体系中，Bean 是结合在一起同时创建完毕的。为了减少它内部的复杂性，
         * Spring 对外提供了一个很重要的包装方法：**refresh()**。
         * 具体的包装方法也很简单，就是对所有的 Bean 调用了一次 getBean()，
         * 利用 getBean() 方法中的 createBean() 创建 Bean 实例，
         * 就可以只用一个方法把容器中所有的 Bean 的实例创建出来了
         */
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    //getBean，容器的核心方法				原来是NoSuchBeanDefinitionException
    public Object getBean(String beanName) throws BeansException {
        //先尝试直接拿Bean实例
        Object singleton = this.getSingleton(beanName);
        //如果此时还没有这个Bean的实例，则获取它的定义来创建实例
        if (singleton == null) {
            // 如果没有实例，则尝试从毛胚实例中获取
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                // 如果连毛胚都没有，则创建bean实例并注册
                System.out.println("get bean null -------------- " + beanName);
                //获取Bean的定义
                BeanDefinition bd = beanDefinitionMap.get(beanName);
                //并进行实例化
                singleton = createBean(bd);
                //注册Bean实例
                this.registerBean(beanName, singleton);

// 				IoC2这里是
//				if (bd.getInitMethodName() != null) {
//					//init method 待补充
//				}

                //预留beanpostprocessor位置
                //step 1 : postProcessBeforeInitialization
                //step 2 : afterPropertiesSet
                //step 3 : init-method
                //step 4 : postProcessAfterInitialization。
            }
        }
        if (singleton == null) {
            throw new BeansException("bean is null.");
        }
        return singleton;
    }



    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);

        // beanpostprocessor 实例化对象注册到单例容器中之后，的处理
    }


    public void registerBeanDefinition(String name, BeanDefinition bd) {
        // 注册信息，但未必进行实例化。
        this.beanDefinitionMap.put(name, bd);
        this.beanDefinitionNames.add(name);
        // 要不要懒加载，不然在注册时就初始化生成实例
        if (!bd.isLazyInit()) {
            try {
                getBean(name);
            } catch (BeansException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void removeBeanDefinition(String name) {
        // 删除信息，但未必进行实例化。
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);

    }

    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    public boolean containsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }

    // 通过BeanDefinition实例化
    private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = doCreateBean(bd);
        //先是构造器注入，也即是：创建毛胚bean实例
        // 存放到毛胚实例缓存中
        this.earlySingletonObjects.put(bd.getId(), obj);

        try {
            clz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //再是处理属性：Setter属性注入，将毛坯bean补齐。
        handleProperties(bd, clz, obj);
        return obj;
    }

    /**
     * doCreateBean创建毛胚实例，仅仅调用构造方法，没有进行属性处理
     * 专门负责创建早期的毛胚实例。毛胚实例创建好后会放在 `earlySingletonObjects` 结构中，
     * 然后 createBean() 方法再调用 handleProperties() 补齐这些 property 的值
     * @param bd bean定义
     * @return 早期的毛坯实例
     */
    private Object doCreateBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = null;
        Constructor<?> con = null;

        // 先是构造器参数集合
        try {
            clz = Class.forName(bd.getClassName());

            //handle constructor
            ConstructorArgumentValues argumentValues = bd.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                // 先通过构造器参数集合的size，new出需要的各类集合。
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                // 属性类型type：因此用Class<?>[]。
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                // 赋值value：因此用Object[]。
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    // 对每个构造器参数，分数据类型分别处理
                    ConstructorArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
                    } else {	//默认为string
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try { // 按照特定构造器创建实例
                    con = clz.getConstructor(paramTypes);    // 针对每个参数的类型Types，找到构造器
                    obj = con.newInstance(paramValues);        // 针对每个参数的赋值Value，进行实例化
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else { // 如果构造器参数集合为空，直接通过clz进行实例化
                obj = clz.newInstance();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(bd.getId() + " bean created. " + bd.getClassName() + " : " + obj.toString());

        return obj;

    }

    // 还有个额外的作用：在这里可以补齐毛坯Bean，补齐一些属性值。
    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
        //handle properties 再是setter注入列表
        System.out.println("handle properties for bean : " + bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                // 对列表中的每个属性，都调用Setter方法。
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                // 属性类型type 与 赋值value
                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) { //如果不是ref，只是普通属性，对每一个属性，分数据类型分别处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }
                    // 赋值value
                    paramValues[0] = pValue;
                } else { //is ref, create the dependent beans
                    /**
                     * 对 ref 所指向的另一个 Bean 再次调用 getBean() 方法，
                     * 这个方法会获取到另一个 Bean 实例，这样就实现了另一个 Bean 的注入
                     */
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try { //再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String) pValue);
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                    /**
                     * 这样一来，如果有多级引用，就会形成一个多级的 getBean() 调用链。
                     * 由于在调用 getBean() 的时候会判断容器中是否包含了 bean instance，没有的话会立即创建，
                     * 所以 XML 配置文件中声明 Bean 的先后次序是任意的。并不是按顺序实例化的。是按实际需求的产生顺序。
                     */
                }
                // 属性名称name：通过这个找到Setter
                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    // 调用对应Setter方法
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }


    }

}
