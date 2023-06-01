package com.minis.beans.factory.config;

/**
 * 扩展 BeanDefinition 的属性，在原有 id 与 name 两个属性的基础上，
 * 新增 <u>lazyInit（延迟初始化）、dependsOn（依赖于）、initMethodName（初始化方法名称）</u> 等属性。
 */
public class BeanDefinition {
    private String id;          // 接口引用，对象的名称
    private String className;   // 具体实现，生成单例的模板

    // SCOPE：表示 bean 是单例模式还是原型模式
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";
    private String scope=SCOPE_SINGLETON;

    // lazyInit：表示 Bean 要不要懒加载，不然在注册时就初始化生成实例
    private boolean lazyInit = true;
    // dependsOn：记录 Bean 之间的依赖关系
    private String[] dependsOn;
    // 属性注入的集合类：构造器注入和Setter注入，也即构造器参数和property列表
    // 用在SimpleBeanFactory中的createBean(BeanDefinition bd)方法
    private ConstructorArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;
    // 初始化方法的声明：当一个 Bean 构造好并实例化之后是否要让框架调用初始化方法
    private String initMethodName;
    // 注意这里是volatile！
    private volatile Object beanClass;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public BeanDefinition(String id, String className) {
        // 构造器也是同样不包含Setter注入属性和Ref！
        this.id = id;
        this.className = className;
    }

    /**
     * 以下是IoC2新增的方法
     */

    public boolean hasBeanClass() {
        return (this.beanClass instanceof Class);
    }
    public void setBeanClass(Class<?> beanClass) {
        this.beanClass = beanClass;
    }
    public Class<?> getBeanClass() {
        return (Class<?>) this.beanClass;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
    public String getScope() {
        return this.scope;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }
    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }
    public boolean isLazyInit() {
        return this.lazyInit;
    }

    public void setDependsOn(String... dependsOn) {
        this.dependsOn = dependsOn;
    }
    public String[] getDependsOn() {
        return this.dependsOn;
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues =
                (constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues());
    }
    public ConstructorArgumentValues getConstructorArgumentValues() {
        return this.constructorArgumentValues;
    }
    public boolean hasConstructorArgumentValues() {
        return !this.constructorArgumentValues.isEmpty();
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = (propertyValues != null ? propertyValues : new PropertyValues());
    }
    public PropertyValues getPropertyValues() {
        return this.propertyValues;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }
    public String getInitMethodName() {
        return this.initMethodName;
    }

}
