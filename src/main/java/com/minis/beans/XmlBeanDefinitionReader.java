package com.minis.beans;

import java.net.URL;
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

            //handle properties
            List<Element> propertyElements = element.elements("property");
            PropertyValues PVS = new PropertyValues();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                PVS.addPropertyValue(new PropertyValue(pType, pName, pValue));
            }
            beanDefinition.setPropertyValues(PVS);
            //end of handle properties

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

            this.bf.registerBeanDefinition(beanID, beanDefinition);
            // this.bf.registerBeanDefinition(beanDefinition); IoC1这里是
        }

    }


}
