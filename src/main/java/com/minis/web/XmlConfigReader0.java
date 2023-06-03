package com.minis.web;

import org.dom4j.Element;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析得到MappingValue，再放入Map<String,MappingValue>。
 */
public class XmlConfigReader0 {

    public XmlConfigReader0() {
    }

    public Map<String, MappingValue0> loadConfig(Resource0 res) {
        Map<String, MappingValue0> mappings = new HashMap<>();

        while (res.hasNext()) { //读所有的节点，解析id, class和value
            // id = uri        class = clz       value = method
            Element element = (Element)res.next();
            String beanID=element.attributeValue("id");
            String beanClassName=element.attributeValue("class");
            String beanMethod=element.attributeValue("value");

            mappings.put(beanID, new MappingValue0(beanID,beanClassName,beanMethod));
        }
        return mappings;
    }
}
