package com.minis.beans.factory.xml;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.factory.config.*;
import com.minis.beans.factory.support.AbstractBeanFactory;
import org.dom4j.Element;

import com.minis.core.Resource;

/**
 * @author: cmx
 * @date: 2023/04/19
 * @classname: XmlBeanDefinitionReader
 * 读取器，xml专用，负责xml资源，提高可读可复用
 */
public class XmlBeanDefinitionReader {
    // IoC1 这里是BeanFactory bf;
    // IoC2+3 这里是SimpleBeanFactory bf
    // IoC4+5 这里是AbstractBeanFactory
    AbstractBeanFactory bf; // 委派给工厂

    public XmlBeanDefinitionReader(AbstractBeanFactory bf) {
        this.bf = bf;
    }

    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element) res.next();
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            // IoC4新增 IoC5又去掉！
            // String initMethodName=element.attributeValue("init-method");

            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            // 因为IoC2增加了更多的属性注入机制，因此以下：
			// 如果有构造器参数集合，或者Setter注入集合，则也打包成属性成员，传递给beanDefinition。

            //get constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues AVS = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                // 加到集合里面
                AVS.addArgumentValue(new ConstructorArgumentValue(pType, pName, pValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor


            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
			// IoC3在Property中增加了isRef判断，因此
            List<String> refs = new ArrayList();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");

				// IoC3在Property中增加了isRef判断，因此
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);// 将依赖引用加入到列表里
                }
                // 加到集合里面：区分出来是ref，但还是要放到属性集合中去处理
                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            // IoC3 新增：将ref列表设置到BeanDefinition中
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);
            //end of handle properties

            // IoC4新增 IoC5又去掉！
        	// beanDefinition.setInitMethodName(initMethodName);

            this.bf.registerBeanDefinition(beanID, beanDefinition);
        }

    }


}
