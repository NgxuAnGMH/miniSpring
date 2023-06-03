package com.minis.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet0 extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private String sContextConfigLocation;
    private List<String> packageNames = new ArrayList<>();

    private Map<String, MappingValue0> mappingValues;            // URL对应的MappingValue
    private Map<String, Class<?>> mappingClz = new HashMap<>(); // URL对应的类Class
    private Map<String, Object> mappingObjs = new HashMap<>();  // URL对应的实例方法Method

    public DispatcherServlet0() {
        super();
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        // 为了启动这个 servlet，我们要提前解析 minisMVC-servlet.xml 文件。
        sContextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(sContextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }// 从外部传入的资源，将 XML 文件内容解析后存入 mappingValues 内
        Resource0 rs = new ClassPathXmlResource0(xmlPath);
        XmlConfigReader0 reader = new XmlConfigReader0();
        mappingValues = reader.loadConfig(rs);
        // 最后调用 Refresh() 函数创建 Bean
        Refresh();// 例子就是 HelloWorldBean，这些 Bean 的类和实例方法存放在 mappingClz 和 mappingObjs 里
    }

    //对所有的mappingValues中注册的类进行实例化，默认构造函数
    protected void Refresh() {
        // 通过读取 mappingValues 中的 Bean 定义，加载类，创建实例
        for (Map.Entry<String, MappingValue0> entry : mappingValues.entrySet()) {
            String id = entry.getKey();
            String className = entry.getValue().getClz();
            Object obj = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(className);
                obj = clz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mappingClz.put(id, clz);
            mappingObjs.put(id, obj);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String sPath = request.getServletPath(); //获取请求的path
        if (this.mappingValues.get(sPath) == null) {
            return;
        }
        // 通过 Bean 的 id(sPath) 获取其对应的类和方法，依赖反射机制进行调用
        Class<?> clz = this.mappingClz.get(sPath); //获取bean类定义
        Object obj = this.mappingObjs.get(sPath);  //获取bean实例
        String methodName = this.mappingValues.get(sPath).getMethod(); //获取调用方法名
        Object objResult = null;
        try {
            Method method = clz.getMethod(methodName);
            objResult = method.invoke(obj); //方法调用
        } catch (Exception e) {
        }
        //将方法返回值写入response
        response.getWriter().append(objResult.toString());
    }

}
