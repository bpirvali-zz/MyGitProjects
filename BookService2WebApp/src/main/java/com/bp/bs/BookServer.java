package com.bp.bs;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BookServer {
	@SuppressWarnings("unused") private static final Logger logger = LoggerFactory.getLogger(BookResourceImpl.class);

	public static void main(String[] args) throws InterruptedException {
		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(BookResourceImpl.class);
		sf.setAddress("http://localhost:8081/bs/rest");
		sf.create();
		System.out.println("Started");
		Thread.sleep(30 * 60 * 1000);
		System.out.println("ended");
		System.exit(0);
	}
}