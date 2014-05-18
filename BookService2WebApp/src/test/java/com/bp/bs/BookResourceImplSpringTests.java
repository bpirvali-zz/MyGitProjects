package com.bp.bs;


//import java.util.GregorianCalendar;

import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.apache.cxf.jaxrs.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
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
	
	//@Autowired
	//private Config cfg;
	
	
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
		Assert.assertEquals(sTitle, book.getTitle());
		logger.info("get PASSED!");
		/*
		if (cfg.isTestFailover()) {
			try {
				System.out.println("You have got 15 secs to shutdown the primary instance...");
				System.out.println("The remainings tests will run on the secondary instance...");
				Thread.sleep(15000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		*/
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
		Assert.assertEquals(sTitle, book.getTitle());
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
		Assert.assertEquals(sTitle, book.getTitle());
	}

	// -------------
	// searchBooks
	// -------------
	@Test//(dependsOnMethods = { "add" })//(enabled=false)//
	public void searchBooks() {
		System.out.println("Entering searchBooks() Test ...");

		client.path("books");
		client.query("keyword", "WHY");
		//client.query("pubdate", new GregorianCalendar(2012, 10, 12).getTime());
		client.query("pubdate", "2012-10-10");
		BooksState st = client.get(BooksState.class);		
		// verify results
		Assert.assertEquals(1, st.getBook().size());
		Assert.assertEquals("WHY did I become a programmer???!!!", st.getBook().get(0).getTitle());
	}

	// -------------
	// getReviews
	// -------------
	@Test//(dependsOnMethods = { "add" })//(enabled=false)//
	public void getReviewsResource() {
		System.out.println("Entering getReviewsResource() Test ...");

		client.path("books/1234/reviews");
		ReviewsState st = client.get(ReviewsState.class);		
		// verify results
		Assert.assertEquals(3, st.getReviewRef().size());
	}

	// -------------
	// getReview
	// -------------
	@Test//(dependsOnMethods = { "add" })//(enabled=false)//
	public void getReview() {
		System.out.println("Entering getReview() Test ...");

		client.path("books/1234/reviews/0");
		ReviewState st = client.get(ReviewState.class);		
		// verify results
		Assert.assertEquals("Ross", st.by);
	}

	// -------------
	// DELETE
	// -------------
	@Test(dependsOnMethods = { "add" })//(enabled=false)//
	public void delete() {
		System.out.println("Entering delete() Test ...");

		client.path("books/5678");
		client.delete();
		Response response = client.get();
		
		// verify results
		Assert.assertEquals(response.getStatus(), HTTP_CODE_PAGE_NOT_FOUND);
	}
}
