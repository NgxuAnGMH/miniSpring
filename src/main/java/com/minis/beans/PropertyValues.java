package com.minis.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 就我们之前，是针对`<beans><bean>`这两对标签，现在自己新增了 `<property>` 和 `<constructor-args>` 标签，
 * 所以我们需要完成这方面的定义工作。
 * 这里是Setter注入的属性类。
 * 这里是多个属性的集合：封装了增加、获取、判断等操作方法，<u>简化调用</u>。
 * 如此一来既给外面提供单个的参数 / 属性的对象，也提供集合对象。
 */
public class PropertyValues {
    private final List<PropertyValue> propertyValueList;

    public PropertyValues() {
        this.propertyValueList = new ArrayList<PropertyValue>(10);
    }

    // 以下增删改查各项操作，注意，有时是操作一整个propertyValue对象，有时候是索引Name名称。
    public List<PropertyValue> getPropertyValueList() {
        return this.propertyValueList;
    }

    public int size() {
        return this.propertyValueList.size();
    }

    public void addPropertyValue(PropertyValue pv) {
        this.propertyValueList.add(pv);
    }

//	public void addPropertyValue(String propertyType, String propertyName, Object propertyValue) {
//		addPropertyValue(new PropertyValue(propertyType, propertyName, propertyValue));
//	}


    public void removePropertyValue(PropertyValue pv) {
        this.propertyValueList.remove(pv);
    }

    public void removePropertyValue(String propertyName) {
        this.propertyValueList.remove(getPropertyValue(propertyName));
    }

    public PropertyValue[] getPropertyValues() {
        return this.propertyValueList.toArray(new PropertyValue[this.propertyValueList.size()]);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue pv : this.propertyValueList) {
            if (pv.getName().equals(propertyName)) {
                return pv;
            }
        }
        return null;
    }

    public Object get(String propertyName) {
        PropertyValue pv = getPropertyValue(propertyName);
        return (pv != null ? pv.getValue() : null);
    }

    public boolean contains(String propertyName) {
        return (getPropertyValue(propertyName) != null);
    }

    public boolean isEmpty() {
        return this.propertyValueList.isEmpty();
    }


}

