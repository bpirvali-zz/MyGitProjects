package com.bp.bs;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BookResourceImplTest {
	private static final String sServiceEndPoint = "http://localhost:8080/bs/rest";
	private static final WebClient client = WebClient.create(sServiceEndPoint);
	private static final int HTTP_CODE_PAGE_NOT_FOUND=404;

	
	@BeforeMethod
	public void beforeMethod() {
		client.back(true);
	}
	
	// -------------
	// GET
	// -------------
	@Test
	public void get() {
		System.out.println("Entering get() Test ...");

		String sTitle="Java Programming Done Right(or Maybe not)  :))!";
		client.path("books/1234");
		Class<BookState> bsClass = BookState.class;
		BookState book = client.get(bsClass);
		Assert.assertEquals(book.getTitle(), sTitle);
	}

	// -------------
	// PUT
	// -------------
	@Test//(enabled=false)//(dependsOnMethods = { "get" })
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
	@Test//(enabled=false)//(dependsOnMethods = { "update" })
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
	@Test//(enabled=false)//(dependsOnMethods = { "add" })
	public void delete() {
		System.out.println("Entering delete() Test ...");

		client.path("books/1236");
		client.delete();
		Response response = client.get();
		
		// verify results
		Assert.assertEquals(response.getStatus(), HTTP_CODE_PAGE_NOT_FOUND);
	}
}
