package com.minis.core;

import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.minis.beans.BeanDefinition;

public class ClassPathXmlResource implements Resource {
	Document document;
	Element rootElement;
	Iterator<Element> elementIterator;
	
	public ClassPathXmlResource(String fileName) {
        SAXReader saxReader=new SAXReader();
		// 猜测：触发启动这个类，加载类信息，并使用其方法绑定资源
        URL xmlPath=this.getClass().getClassLoader().getResource(fileName);
		//将配置文件装载进来，生成一个迭代器，可以用于遍历
        try { // 操作 XML 文件格式都是 dom4j 帮我们做的。
			this.document = saxReader.read(xmlPath); 	// 读取器
			this.rootElement=document.getRootElement();	// 获得根节点
			this.elementIterator=this.rootElement.elementIterator(); // 从根节点开始的迭代器。用于遍历。
		} catch (DocumentException e) {
			e.printStackTrace();
		}		
	}
	
	public boolean hasNext() {
		return this.elementIterator.hasNext();
	}

	public Object next() {
		return this.elementIterator.next();
	}


}
