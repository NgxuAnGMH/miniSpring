package com.minis.beans;

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
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory,BeanDefinitionRegistry{
	// IoC1 原来的属性成员
	//	private List<BeanDefinition> beanDefinitions=new ArrayList();
	//	private List<String> beanNames=new ArrayList();
	//	private Map<String, Object> singletons =new HashMap();


	private Map<String,BeanDefinition> beanDefinitionMap=new ConcurrentHashMap(256);
	// 已实例化的单例容器
	private List<String> beanDefinitionNames=new ArrayList();
	// 名称注册表

	// IoC3新增属性
    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

	public SimpleBeanFactory() {
	}

	// IoC3新增方法
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

	//getBean，容器的核心方法				原来是NoSuchBeanDefinitionException
	public Object getBean(String beanName) throws BeansException{
		//先尝试直接拿Bean实例
		Object singleton = this.getSingleton(beanName);
		//如果此时还没有这个Bean的实例，则获取它的定义来创建实例
		if (singleton == null) {
			singleton = this.earlySingletonObjects.get(beanName);
			if (singleton == null) {
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

                //beanpostprocessor
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


//	@Override
	public boolean containsBean(String name) {
		return containsSingleton(name);
	}

	public void registerBean(String beanName, Object obj) {
		this.registerSingleton(beanName, obj);

        //beanpostprocessor
	}

	//  原来的方法
	//	public void registerBeanDefinition(BeanDefinition bd){
	//		this.beanDefinitions.add(bd);
	//		this.beanNames.add(bd.getId());
	//	}
//	@Override
	public void registerBeanDefinition(String name, BeanDefinition bd) {
		this.beanDefinitionMap.put(name,bd);
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

//	@Override
	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);

	}

//	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}

//	@Override
	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}

//	@Override
	public boolean isSingleton(String name) {
		return this.beanDefinitionMap.get(name).isSingleton();
	}

//	@Override
	public boolean isPrototype(String name) {
		return this.beanDefinitionMap.get(name).isPrototype();
	}

//	@Override
	public Class<?> getType(String name) {
		return this.beanDefinitionMap.get(name).getClass();
	}

	// 通过BeanDefinition实例化
	private Object createBean(BeanDefinition bd) {
        Class<?> clz = null;
        Object obj = doCreateBean(bd);//先是构造器注入

        this.earlySingletonObjects.put(bd.getId(), obj);

        try {
            clz = Class.forName(bd.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		//再是Setter属性注入
        handleProperties(bd, clz, obj);
        return obj;
    }

    private Object doCreateBean(BeanDefinition bd) {
		Class<?> clz = null;
		Object obj = null;
		Constructor<?> con = null;

		// 先是构造器参数集合
		try {
			clz = Class.forName(bd.getClassName());

			//handle constructor
			ArgumentValues argumentValues = bd.getConstructorArgumentValues();
			if (!argumentValues.isEmpty()) {
				// 先通过构造器参数集合的size，new出需要的各类集合。
				Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
				// 属性类型type：因此用Class<?>[]。
				Object[] paramValues =   new Object[argumentValues.getArgumentCount()];
				// 赋值value：因此用Object[]。
				for (int i=0; i<argumentValues.getArgumentCount(); i++) {
					// 对每个构造器参数
					ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
					if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
					}
					else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
						paramTypes[i] = Integer.class;
						paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
					}
					else if ("int".equals(argumentValue.getType())) {
						paramTypes[i] = int.class;
						paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
					}
					else {
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
					}
				}
				try {
					con = clz.getConstructor(paramTypes); 	// 针对每个参数的类型Types，找到构造器
					obj = con.newInstance(paramValues);		// 针对每个参数的赋值Value，进行实例化
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
			else {
				// 如果构造器参数集合为空，直接通过clz进行实例化
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

    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
		//handle properties 再是setter注入列表
        System.out.println("handle properties for bean : " + bd.getId());
		PropertyValues propertyValues = bd.getPropertyValues();
		if (!propertyValues.isEmpty()) {
			for (int i=0; i<propertyValues.size(); i++) {
				// 对列表中的每个属性，都调用Setter方法。
				PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
				String pName = propertyValue.getName();
				String pType = propertyValue.getType();
				Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
				// 属性类型type 与 赋值value
				Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];
                if (!isRef) {
				if ("String".equals(pType) || "java.lang.String".equals(pType)) {
					paramTypes[0] = String.class;
				}
				else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
					paramTypes[0] = Integer.class;
				}
				else if ("int".equals(pType)) {
					paramTypes[0] = int.class;
				}
				else {
					paramTypes[0] = String.class;
				}
				// 赋值value
				paramValues[0] = pValue;
                } else { //is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        paramValues[0] = getBean((String) pValue);
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                }
				// 属性名称name：通过这个找到Setter
				String methodName = "set" + pName.substring(0,1).toUpperCase() + pName.substring(1);
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
