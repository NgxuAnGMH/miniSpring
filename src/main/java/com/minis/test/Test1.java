package com.minis.test;

import com.minis.beans.factory.exception.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class Test1 {

	public static void main(String[] args) {

		// IoC4的测试 "beans.xml"
		// ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		// IoC5的测试 "applicationContext.xml"
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		AService aService;
	    BaseService bService;
		try {
			// IoC4 注释 IoC5 取消注释
			aService = (AService)ctx.getBean("aservice");
		    aService.sayHello();


			bService = (BaseService)ctx.getBean("baseservice");
			bService.sayHello();
		} catch (BeansException e) {
			e.printStackTrace();
		}

	}

}
