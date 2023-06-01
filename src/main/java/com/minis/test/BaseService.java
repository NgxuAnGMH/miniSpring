package com.minis.test;

import com.minis.beans.factory.annotation.Autowired;

public class BaseService {
	// 该Bean依赖了我们定义的其他bean。IoC4新增注解@Autowired
	// 因此不必在beans.xml中手动配置。
	@Autowired
	private BaseBaseService bbs;
	
	public BaseBaseService getBbs() {
		return bbs;
	}
	public void setBbs(BaseBaseService bbs) {
		this.bbs = bbs;
	}
	public BaseService() {
	}
	public void sayHello() {
		System.out.print("Base Service says hello");
		bbs.sayHello();
	}
	// IoC4新增 IoC5又去掉
//	public void init() {
//		System.out.print("Base Service init method.");
//	}
}
