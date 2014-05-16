package com.bp.bs;

import org.apache.cxf.jaxrs.client.WebClient;

public class BookClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//WebClient client = WebClient.create("http://localhost:8080/bs");
		WebClient client = WebClient.create("http://localhost:1234/bs/rest");
		
		// -------------
		// PUT
		// -------------
		client.path("books/1235");
		BookState book = client.get(BookState.class);
		System.out.println("Book Title Before UPDATE:" + book.getTitle());
		book.setTitle("C# Programming");
		client.put(book);  // <-- PUT
		// Display results
		book = client.get(BookState.class);
		System.out.println("Book Title After UPDATE:" + book.getTitle());
		
		// -------------
		// POST
		// -------------
//		BookState st = new BookState();
//		st.setIsbn("5678");
//		st.setTitle("Design Patterns");
//		client.path("books");
//		client.post(st); // <-- POST
//		// Display results
//		client.back(true);
//		client.path("books/5678");
//		BookState book = client.get(BookState.class);
//		System.out.println(book.getTitle());
	
		// -------------
		// DELETE
		// -------------
//		client.path("books/1234");
//		client.delete();
//		Response response = client.get();
//		System.out.println(response.getStatus());		
	}

}
