package com.bp.samples.spring.aop.annotated;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ClassPathXmlApplicationContext app_ctx = new ClassPathXmlApplicationContext("bean-definitions.xml"); 

		Hello hello = (Hello)app_ctx.getBean("hello");
		printHello(hello.getName());
		printHello(hello.getName());
		printHello(hello.getName());
		
		MonitoringAspects monAspects = (MonitoringAspects)app_ctx.getBean("monAspect");
		try {
			System.out.println(monAspects.ReportDetail("csv"));
			System.out.println(monAspects.ReportSummary("csv"));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void printHello(String sName) {
		System.out.println("Hello, " + sName + "!" );
	}
}
