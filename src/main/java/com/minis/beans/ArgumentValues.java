package com.minis.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 就我们之前，是针对`<beans><bean>`这两对标签，现在自己新增了 `<property>` 和 `<constructor-args>` 标签，
 * 所以我们需要完成这方面的定义工作。
 * 这里是构造器注入的属性类。
 * 这里是多个属性的集合：封装了增加、获取、判断等操作方法，<u>简化调用</u>。
 * 如此一来既给外面提供单个的参数 / 属性的对象，也提供集合对象。
 */
public class ArgumentValues {
    private final List<ArgumentValue> argumentValueList = new ArrayList<ArgumentValue>();

    public ArgumentValues() {
    }

    public void addArgumentValue(ArgumentValue argumentValue) {
        this.argumentValueList.add(argumentValue);
    }

    public ArgumentValue getIndexedArgumentValue(int index) {
        ArgumentValue argumentValue = this.argumentValueList.get(index);
        return argumentValue;
    }

    public int getArgumentCount() {
        return (this.argumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.argumentValueList.isEmpty());
    }

}