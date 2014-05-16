package com.bp.bs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BooksDB {
	private static Map<String, Book> books;
	
	private BooksDB() {
		books = new ConcurrentHashMap<String, Book>();
		books.put("1234", new Book("1234", "Java Programming Done Right(or Maybe not)  :))!"));
		books.put("1235", new Book("1235", "Time to move on, So Java --> C#"));
		books.put("1236", new Book("1236", "WHY did I become a programmer???!!!"));
		books.put("1237", new Book("1237", "Java Rocks!"));
	}
	
	private static class ResourceHolder {
		public static BooksDB resource = new BooksDB();
	}
	
	// Thread safe lazy init
	public static BooksDB getInstance() {
		return ResourceHolder.resource ;
	}
	
	public Book getBook(String isbn) {
		return books.get(isbn);
	}

	public void addBook(Book book) {
		books.put(book.getIsbn(), book);
	}

	public boolean updateBook(String isbn, String newTitle) {
		Book b = books.get(isbn);
		if (b==null)
			return false;
		synchronized(b) { // lock on entity and not code!!!
			b.setTitle(newTitle);
		}
		return true;
	}
	
	public boolean deleteBook(String isbn) {
		Book b = books.remove(isbn);
		if (b!=null)
			return true;
		return false;
	}	
	
	public boolean deleteBook(Book book) {
		Book b = books.remove(book.getIsbn());
		if (b!=null)
			return true;
		return false;
	}	
}

