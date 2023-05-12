package com.minis.test;

import com.minis.beans.NoSuchBeanDefinitionException;
import com.minis.context.ClassPathXmlApplicationContext;

public class Test1 {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		AService aService= null;
//		这里Try Catch
		try {
			aService = (AService)ctx.getBean("aservice");
		} catch (NoSuchBeanDefinitionException e) {
			throw new RuntimeException(e);
		}
		aService.sayHello();
	}

}
