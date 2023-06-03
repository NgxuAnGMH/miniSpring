package com.minis.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet implementation class DispatcherServlet
 */
public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private String sContextConfigLocation;
	private List<String> packageNames = new ArrayList<>();
	// 存储需要扫描的packages列表
	private Map<String, Object> controllerObjs = new HashMap<>();
	// 存储controller的名称与对象的映射关系
	private List<String> controllerNames = new ArrayList<>();
	// 存储controller名称数组列表
	private Map<String, Class<?>> controllerClasses = new HashMap<>();
	// 存储controller名称与类的映射关系

	private List<String> urlMappingNames = new ArrayList<>();
	// 保存自定义的@RequestMapping(url)的列表
	private Map<String, Object> mappingObjs = new HashMap<>();
	// 保存URL与对象的映射关系
	private Map<String, Method> mappingMethods = new HashMap<>();
	// 保存URL与方法的映射关系

	public DispatcherServlet() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// 为了启动这个 servlet，我们要提前解析 minisMVC-servlet.xml 文件。
		sContextConfigLocation = config.getInitParameter("contextConfigLocation");

		URL xmlPath = null;
		try {
			xmlPath = this.getServletContext().getResource(sContextConfigLocation);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		// 初始化时我们把 minisMVC-servlet.xml 里扫描出来的 package 名称存入 packageNames 列表
		this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

		Refresh();

	}

	protected void Refresh() {
		initController();    // 初始化 controller
		initMapping();        // 初始化 url 映射
	}

	protected void initController() {
		/**
		 * 对扫描到的每一个类进行加载和实例化，与类的名字建立映射关系，
		 * 分别存在  controllerClasses 和  controllerObjs 这两个 map 里，类名就是 key 的值
		 */
		this.controllerNames = scanPackages(this.packageNames);

		for (String controllerName : this.controllerNames) {
			Object obj = null;
			Class<?> clz = null;

			try {
				clz = Class.forName(controllerName); //加载类
				this.controllerClasses.put(controllerName, clz);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				obj = clz.newInstance(); //实例化bean
				this.controllerObjs.put(controllerName, obj);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	private List<String> scanPackages(List<String> packages) {
		// 对文件目录的递归处理，最后的结果就是把所有的类文件扫描出来
		List<String> tempControllerNames = new ArrayList<>();
		for (String packageName : packages) {
			tempControllerNames.addAll(scanPackage(packageName));
		}
		return tempControllerNames;
	}

	private List<String> scanPackage(String packageName) {
		List<String> tempControllerNames = new ArrayList<>();
//		URI uri = null;
//		//将以.分隔的包名换成以/分隔的uri
//		try {
//			uri = this.getClass().getResource("/" +
//					packageName.replaceAll("\\.", "/")).toURI();
//		} catch (Exception e) {
//		}
		URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.", "/"));
		File dir = new File(url.getFile());
		//处理对应的文件目录
		for (File file : dir.listFiles()) { //目录下的文件或者子目录
			if (file.isDirectory()) { // 1 对子目录递归扫描
				scanPackage(packageName + "." + file.getName());
			} else {                    // 2 类文件
				String controllerName = packageName + "." + file.getName().replace(".class", "");
				tempControllerNames.add(controllerName);
			}
		}
		return tempControllerNames;
	}

	protected void initMapping() {
		for (String controllerName : this.controllerNames) {
			Class<?> clazz = this.controllerClasses.get(controllerName);
			Object obj = this.controllerObjs.get(controllerName);
			Method[] methods = clazz.getDeclaredMethods();
			if (methods != null) {
				// 初始化 URL 映射，找到使用了注解 @RequestMapping 的方法
				for (Method method : methods) {    // 检查所有的方法
					boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
					if (isRequestMapping) { //有RequestMapping注解
						//建立方法名和URL的映射
						String methodName = method.getName();
						String urlmapping = method.getAnnotation(RequestMapping.class).value();
						this.urlMappingNames.add(urlmapping);        //URL 存放到 urlMappingNames 里
						this.mappingObjs.put(urlmapping, obj);        //映射的对象存放到 mappingObjs 里
						this.mappingMethods.put(urlmapping, method);//映射的方法存放到 mappingMethods 里
					}
				}
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sPath = request.getServletPath(); //获取请求的path
		System.out.println(sPath);
		if (!this.urlMappingNames.contains(sPath)) {
			return;    // 不包含该URL直接结束返回。
		}
		// 存在该URL
		Object obj = null;
		Object objResult = null;
		try {    // 该URL对应的Method + 该URL对应的Object
			Method method = this.mappingMethods.get(sPath);
			obj = this.mappingObjs.get(sPath);
			objResult = method.invoke(obj); // 方法调用得到结果
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		//将方法返回值写入response
		response.getWriter().append(objResult.toString());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
