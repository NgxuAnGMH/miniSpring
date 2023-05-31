package com.minis.test;

import com.minis.beans.factory.annotation.Autowired;

public class BaseService {
	// 该Bean依赖了我们定义的其他bean。IoC4新增注解@Autowired
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
	// IoC4新增
	public void init() {
		System.out.print("Base Service init method.");
	}
}
