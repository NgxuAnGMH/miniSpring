package com.minis.test;

public class BaseBaseService {
	// 该Bean依赖了我们定义的其他bean
	private AServiceImpl as;
	
	public AServiceImpl getAs() {
		return as;
	}
	public void setAs(AServiceImpl as) {
		this.as = as;
	}
	public BaseBaseService() {
	}
	public void sayHello() {
		System.out.println("Base Base Service says hello");
	}
}
