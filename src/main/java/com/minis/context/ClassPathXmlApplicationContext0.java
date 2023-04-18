package com.minis.context;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.NoSuchBeanDefinitionException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

public class ClassPathXmlApplicationContext0 {
    private List<BeanDefinition> beanDefinitions = new ArrayList();
    // 泛型提供编译时检查，另外底层是数组容器来管理
    // HashMap进行映射，所以是单例模式。
    private Map<String, Object> singletons = new HashMap();
    //构造器获取外部配置，解析出Bean的定义，形成内存映像
    public ClassPathXmlApplicationContext0(String fileName) {
        this.readXml(fileName);
        this.instanceBeans();
    }
    // （通过传入的文件路径，也就是 XML 文件的全路径名，来获取 XML 内的信息）
    private void readXml(String fileName) {
        SAXReader saxReader = new SAXReader();
        try {
            URL xmlPath =
                    this.getClass().getClassLoader().getResource(fileName);
            // 猜测：触发启动这个类，加载类信息，并使用其方法绑定资源
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            //对配置文件中的每一个<bean>，进行处理
            for (Element element : (List<Element>) rootElement.elements()) {
                //获取Bean的基本信息
                String beanID = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanID,
                        beanClassName);
                //将Bean的定义存放到beanDefinitions
                beanDefinitions.add(beanDefinition);
            }
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
    }
    //利用反射创建Bean实例，并存储在singletons中（根据读取到的信息实例化 Bean）
    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                singletons.put(beanDefinition.getId(),
                        Class.forName(beanDefinition.getClassName()).newInstance());
                // newInstance()创建一个实例，并获取类信息，放到singletons（Map容器）中。
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
    //这是对外的一个方法，让外部程序从容器中获取Bean实例，会逐步演化成核心方法
    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }
}
