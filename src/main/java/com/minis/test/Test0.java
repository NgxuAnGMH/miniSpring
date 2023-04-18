package com.minis.test;

import com.minis.context.ClassPathXmlApplicationContext0;

public class Test0 {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext0 ctx = new ClassPathXmlApplicationContext0("beans.xml");
		AService aService= null;
		aService = (AService)ctx.getBean("aservice");
		aService.sayHello();
	}
}
