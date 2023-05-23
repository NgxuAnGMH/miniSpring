package com.minis.beans;


public class BeanDefinition {
    private String id;          // 接口引用，对象的名称
    private String className;   // 具体实现，生成单例的模板

    // SCOPE：表示 bean 是单例模式还是原型模式
	private String scope;
    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    // lazyInit：表示 Bean 要不要在加载的时候初始化
    private boolean lazyInit = false;
    // dependsOn：记录 Bean 之间的依赖关系
    private String[] dependsOn;
    // 属性注入的集合类：构造器注入和Setter注入，也即构造器参数和property列表
    private ArgumentValues constructorArgumentValues;
    private PropertyValues propertyValues;
    // 初始化方法的声明：当一个 Bean 构造好并实例化之后是否要让框架调用初始化方法
    private String initMethodName;

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
        this.id = id;
        this.className = className;
    }

    /**
     * 以下是IoC2新增的方法
     * @return
     */

	public boolean hasBeanClass() {
		return (this.beanClass instanceof Class);
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	public Class<?> getBeanClass(){

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

	public void setConstructorArgumentValues(ArgumentValues constructorArgumentValues) {
		this.constructorArgumentValues =
				(constructorArgumentValues != null ? constructorArgumentValues : new ArgumentValues());
	}

	public ArgumentValues getConstructorArgumentValues() {
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
