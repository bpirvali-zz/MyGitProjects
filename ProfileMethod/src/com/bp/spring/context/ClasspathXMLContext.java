package com.bp.spring.context;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClasspathXMLContext {
	private static String s_sFileXml = null;	
	private static ClassPathXmlApplicationContext s_app_ctx = null; 
	
	public static ClassPathXmlApplicationContext getAppCtx(String sFileXml) {
		if (sFileXml==null)
			return s_app_ctx;
		
		if (s_sFileXml==null) {
			s_sFileXml = sFileXml;
			s_app_ctx = new ClassPathXmlApplicationContext(sFileXml);
		} else if (!s_sFileXml.equals(sFileXml)) {
			s_sFileXml = sFileXml;
			s_app_ctx = new ClassPathXmlApplicationContext(sFileXml);
		}			
		return s_app_ctx;
	}
}
