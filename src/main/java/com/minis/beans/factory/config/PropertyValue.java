package com.minis.beans.factory.config;

/**
 * 就我们之前，是针对`<beans><bean>`这两对标签，现在自己新增了 `<property>` 和 `<constructor-args>` 标签，
 * 所以我们需要完成这方面的定义工作。
 * 这里是Setter注入的属性类。单个
 * 通过Setter进行配置的属性(类)
 */
public class PropertyValue {
	private final String type;    // 属性类型type
	private final String name;    // 属性名称name
	private final Object value;   	// 赋值value

	private final boolean isRef;	// value是否为ref的情况
	// 判断注入的属性，xml中显示是ref引用类型还是value普通的值类型，是否依赖其他bean

	public PropertyValue(String type, String name, Object value, boolean isRef) {
		this.type = type;
		this.name = name;
		this.value = value;
		this.isRef = isRef;
	}

	// 以下是getter和setter

	public String getType() {
		return this.type;
	}

	public String getName() {
		return this.name;
	}

	public Object getValue() {
		return this.value;
	}

	public boolean getIsRef() {
		return isRef;
	}
}

