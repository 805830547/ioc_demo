package com.howto.parseXML.util;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParseXML {
	
	static Logger logger = Logger.getLogger(ParseXML.class.getName());
	
	
	public Map<String, Map<String, Object>> parseIOC(String filename) {
		List parseList = new ArrayList();
		
		SAXReader saxReader = new SAXReader();
		Document document;
		
		Map<String, Map<String, Object>> iocMap = new HashMap<String, Map<String, Object>>();
		try{
			
			InputStream input = this.getClass().getClassLoader().getResourceAsStream(filename); 
			document = saxReader.read(input);
			//document = saxReader.read(new File(filename));
			
			Element root_EL = document.getRootElement();
			
			for(Iterator iter=root_EL.elementIterator();iter.hasNext();) {
				Element bean_EL = (Element)iter.next();
				
				Map<String, Object> tempMap = new HashMap<String, Object>();
				
				tempMap.put("id", bean_EL.attribute("id").getValue());
				tempMap.put("class", bean_EL.attribute("class").getValue());
				List<Map<String, String>> propertyList = new ArrayList<Map<String, String>>();
				for(Iterator iter0=bean_EL.elementIterator();iter0.hasNext();){
					Element property_EL = (Element)iter0.next();
					Map<String, String> propertyMap = new HashMap<String, String>();

					propertyMap.put("name", property_EL.attribute("name").getValue());
					propertyMap.put("ref", property_EL.attribute("ref").getValue());
					
					propertyList.add(propertyMap);
					
				}
				
				tempMap.put("property", propertyList);
				
				iocMap.put(bean_EL.attribute("id").getValue(), tempMap);
			}
		} catch(DocumentException e) {
			e.printStackTrace();
		}
		return iocMap;
	}
	
}
