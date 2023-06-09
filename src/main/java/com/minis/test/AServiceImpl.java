package com.minis.test;

public class AServiceImpl implements AService {
    // bean.xml中的Bean的属性
    private String name;    // 构造器注入
    private int level;        // 构造器注入
    private String property1;    // Setter注入
    private String property2;    // Setter注入
    // IoC3新增依赖引用
    private BaseService ref1;

    public BaseService getRef1() {
        return ref1;
    }

    public void setRef1(BaseService bs) {
        this.ref1 = bs;
    }

    // Setter注入
    public String getProperty1() {
        return property1;
    }
    public void setProperty1(String property1) {
        this.property1 = property1;
    }
    public String getProperty2() {
        return property2;
    }
    public void setProperty2(String property2) {
        this.property2 = property2;
    }

    public AServiceImpl() {
    }

    public AServiceImpl(String name, int level) {
        // 构造器注入，这里是没有处理Ref（依赖的其他bean）因此把Ref放到Property中处理
        // 一个可能的原因是，构造器处理Ref会让循环依赖问题无法解决。
        this.name = name;
        this.level = level;
        System.out.println(this.name + "," + this.level);
    }

    public void sayHello() {
        System.out.println(this.property1 + "," + this.property2);
        ref1.sayHello();
    }

}
