package com.minis.beans;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.minis.core.Resource;

/**
 * @author: cmx
 * @date: 2023/04/19
 * @classname: XmlBeanDefinitionReader
 * 读取器，xml专用，负责xml资源，提高可读可复用
 */
public class XmlBeanDefinitionReader {
    // IoC1 这里是BeanFactory bf;
    SimpleBeanFactory bf; // 委派给工厂

    public XmlBeanDefinitionReader(SimpleBeanFactory bf) {
        this.bf = bf;
    }

    public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
            Element element = (Element) res.next();
            // id -> List<String> beanNames || id + class -> List<BeanDefinition> beanDefinitions
            // 底层都是数组，二者算是同步。
            String beanID = element.attributeValue("id");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

            // this.bf.registerBeanDefinition(beanDefinition); IoC1这里是
            // 因为IoC2增加了更多的属性注入机制，因此以下：
			// 如果有构造器参数集合，或者Setter注入集合，则也打包成属性成员，传递给beanDefinition。

            //get constructor
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues AVS = new ArgumentValues();
            for (Element e : constructorElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                AVS.addArgumentValue(new ArgumentValue(pType, pName, pValue));
            }
            beanDefinition.setConstructorArgumentValues(AVS);
            //end of handle constructor


            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
			// IoC3新增引用列表
            List<String> refs = new ArrayList();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");

				// IoC3引用列表的处理
                String pRef = e.attributeValue("ref");
                String pV = "";
                boolean isRef = false;
                if (pValue != null && !pValue.equals("")) {
                    isRef = false;
                    pV = pValue;
                } else if (pRef != null && !pRef.equals("")) {
                    isRef = true;
                    pV = pRef;
                    refs.add(pRef);
                }

                PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
            }
            beanDefinition.setPropertyValues(PVS);
            // IoC3 新增：
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);
            //end of handle properties

            this.bf.registerBeanDefinition(beanID, beanDefinition);
            // this.bf.registerBeanDefinition(beanDefinition); IoC1这里是
        }

    }


}
