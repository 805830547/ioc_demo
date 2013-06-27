package com.howto.ioc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.howto.parseXML.util.ParseXML;

public class BeanFactory {
	public static Map<String, Object> beanContainer = new HashMap<String, Object>();
	
	public static void init() {
		//通过 反射 初始化 bean 
		instanceBean();
		
		//构建 bean 依赖关系 
		invokeBean();
		
		System.out.println("init success!");
	}



	public
	static ParseXML parseXML = new ParseXML();
	//解析 xml
	public
	static Map<String, Map<String, Object>> beanXML = parseXML.parseIOC("application.xml");
	
	private static void instanceBean() {
		
		Set<String> beanKeys = beanXML.keySet();
		//初始化 bean
		for(String beanID: beanKeys) {
			
			Map<String, Object> beanDesc = beanXML.get(beanID);
				
			Object bean = getBean(beanID);
			
			beanContainer.put(beanID, bean);
				
			
			
		}
		
	}
	
	private static void invokeBean() {
		Set<String> beanKeys = beanXML.keySet();
		
		for(String beanID: beanKeys) {
			
			Map<String, Object> beanDesc = beanXML.get(beanID);
			
			invoke(beanDesc);
				
		}
	}
	
	private static void invoke(Map<String, Object> beanDesc) {
		
		Class<?> beanClass;
		try {
			beanClass = Class.forName((String)beanDesc.get("class"));

			List<Map<String, Object>> methodList = getBeanSetMethod(beanClass, (List<Map<String, String>>)beanDesc.get("property"));
			
			for(Map<String, Object> tempMap: methodList) {
				Method setMethod = (Method)tempMap.get("method");
				
				Class<?>[] para = (Class<?>[])tempMap.get("para");
				
				String beanID = (String)beanDesc.get("id");
				Object obj = getBean(beanID);
				setMethod.invoke(obj, tempMap.get("ref"));
				System.out.println(obj);
			}
			
			
			
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		
		
		
	}

	private static List<Map<String, Object>> getBeanSetMethod(Class<?> beanClass, List<Map<String, String>> propertylist)  {
		List<Map<String, Object>> setMethodList = new ArrayList<Map<String, Object>>();
		for(Map<String, String> propertyMap: propertylist) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			
			Method setProMethod = getSetMethod(beanClass, propertyMap);
			
			Class<?> para[] = setProMethod.getParameterTypes();
			
			tempMap.put("methodName", setProMethod.getName());
			tempMap.put("para", para);
			tempMap.put("method", setProMethod);
			tempMap.put("ref", getBean(propertyMap.get("ref")));
			setMethodList.add(tempMap);
		}
		return setMethodList;
	}
	//拿到 属性set 方法method
	private static Method getSetMethod(Class<?> beanClass,
			Map<String, String> propertyMap) {
		Method[] methods = beanClass.getMethods();
		
		for(Method tempMethod: methods) {
			if(tempMethod.getName().equals(getSetPramMethodName(propertyMap)))
				return tempMethod;
		}
		return null;
	}
	//生成 属性 set 方法名
	private static Object getSetPramMethodName(Map<String, String> propertyMap) {
		String prameterName = propertyMap.get("name");
		String setPramMethodName = "set" + prameterName.substring(0, 1).toUpperCase() + prameterName.substring(1);
		
		return setPramMethodName;
	}

	private static Object getBean(String beanID)  {
		
		if(beanContainer.containsKey(beanID)) {
			return beanContainer.get(beanID);
		} else {
			Map<String, Object> beanDesc = beanXML.get(beanID);
			
			Object obj = null;
			try {
				obj = Class.forName((String)beanDesc.get("class")).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			return obj;
		}
		
		
	}
}
