package com.minis.beans.factory.annotation;

import com.minis.beans.factory.exception.BeansException;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.support.BeanFactory;

import java.lang.reflect.Field;

/**
 * 虽然继承了 BeanPostProcessor，但它是用来处理注解的！
 */
public class AutowiredAnnotationBeanPostProcessor implements BeanPostProcessor {
	private BeanFactory beanFactory;

	// 初始化前完成注解的处理
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Object result = bean;
		// 获取类
		Class<?> clazz = bean.getClass();
		// 获取属性
		Field[] fields = clazz.getDeclaredFields();
		if(fields!=null){
			for(Field field : fields){
				// 对每一个属性进行判断，如果带有@Autowired注解则进行处理
				boolean isAutowired = field.isAnnotationPresent(Autowired.class);
				if(isAutowired){
					// 根据属性名查找同名的bean
					String fieldName = field.getName();
					Object autowiredObj = this.getBeanFactory().getBean(fieldName);
					// 设置属性值，完成注入
					try {
						field.setAccessible(true);
						// 将autowiredObj，绑定到bean上
						field.set(bean, autowiredObj);
						System.out.println("autowire " + fieldName + " for bean " + beanName);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}

				}
			}
		}
		
		return result;
	}

	// 初始后做额外的处理
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

}
