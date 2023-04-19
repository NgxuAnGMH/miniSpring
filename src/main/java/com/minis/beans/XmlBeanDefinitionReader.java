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
	BeanFactory bf; // 委派给工厂
	public XmlBeanDefinitionReader(BeanFactory bf) {
		this.bf = bf;
	}
	public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
        	Element element = (Element)res.next();
			// id -> List<String> beanNames || id + class -> List<BeanDefinition> beanDefinitions
			// 底层都是数组，二者算是同步。
            String beanID=element.attributeValue("id");
            String beanClassName=element.attributeValue("class");
            BeanDefinition beanDefinition=new BeanDefinition(beanID,beanClassName);
            this.bf.registerBeanDefinition(beanDefinition);
        }
		
	}
	


}
