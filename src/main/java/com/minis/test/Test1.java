package com.minis.test;

import com.minis.beans.factory.exception.BeansException;
import com.minis.context.ClassPathXmlApplicationContext;

public class Test1 {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		AService aService;
	    BaseService bService; // IoC4的测试
		try {
			//aService = (AService)ctx.getBean("aservice");
		    //aService.sayHello();

			// IoC4的测试：用到时是否能够自动装配
			bService = (BaseService)ctx.getBean("baseservice");
			bService.sayHello();
		} catch (BeansException e) {
			e.printStackTrace();
		}

	}

}
