package com.minis.test;

import com.minis.beans.factory.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class Test1 {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
//		AService aService= null; IoC1
		AService aService;
		try {
			aService = (AService)ctx.getBean("aservice");
		aService.sayHello();
		} catch (BeansException e) {
			e.printStackTrace();
		}

	}

}
