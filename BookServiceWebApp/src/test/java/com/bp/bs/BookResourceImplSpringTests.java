package com.bp.bs;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.apache.cxf.clustering.FailoverFeature;
import org.apache.cxf.clustering.SequentialStrategy;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@ContextConfiguration(locations={"classpath:beans-tests.xml"})
public class BookResourceImplSpringTests extends AbstractTestNGSpringContextTests {
	private static final Logger logger = LoggerFactory.getLogger(BookResourceImplSpringTests.class);
	private static final int HTTP_CODE_PAGE_NOT_FOUND=404;
	
	@Autowired
	private ApplicationContext appCtx;

	@Autowired
	private WebClient client;
	
	@Autowired
	private Config cfg;
	
	
	@BeforeMethod
	public void beforeMethod() {
		client.back(true);
	}
	
	@Test
	public void testSpring() {
		Assert.assertNotNull(appCtx);
		Assert.assertNotNull(client);
	}

	// -------------
	// GET
	// -------------
	@Test
	public void get() {
		System.out.println("Entering get() Test ...");

		String sTitle="Java Programming Done Right(or Maybe not)  :))!";
		client.path("books/1234");
		BookState book = client.get(BookState.class);
		Assert.assertEquals(book.getTitle(), sTitle);
		logger.info("get PASSED!");
		if (cfg.isTestFailover()) {
			try {
				System.out.println("You have got 15 secs to shutdown the primary instance...");
				System.out.println("The remainings tests will run on the secondary instance...");
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// -------------
	// PUT
	// -------------
	@Test(dependsOnMethods = { "get" })//(enabled=false)//
	public void update() {
		System.out.println("Entering update() Test ...");
		String sTitle = "C# Programming";
		client.path("books/1235");
		BookState book = client.get(BookState.class);
		book.setTitle(sTitle);
		client.put(book); // <-- PUT

		// verify results
		book = client.get(BookState.class);
		Assert.assertEquals(book.getTitle(), sTitle);
	}

	// -------------
	// POST
	// -------------
	@Test(dependsOnMethods = { "update" })//(enabled=false)//
	public void add() {
		System.out.println("Entering add() Test ...");

		String sTitle="Design Patterns";
		BookState st = new BookState();
		st.setIsbn("5678");
		st.setTitle(sTitle);
		client.path("books");
		client.post(st); // <-- POST
		
		// Verify results
		client.back(true);
		client.path("books/5678");
		BookState book = client.get(BookState.class);
		Assert.assertEquals(book.getTitle(), sTitle);
	}

	// -------------
	// DELETE
	// -------------
	@Test(dependsOnMethods = { "add" })//(enabled=false)//
	public void delete() {
		System.out.println("Entering delete() Test ...");

		client.path("books/1236");
		client.delete();
		Response response = client.get();
		
		// verify results
		Assert.assertEquals(response.getStatus(), HTTP_CODE_PAGE_NOT_FOUND);
	}
}
