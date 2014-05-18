package com.bp.bs;

import java.util.Calendar;
import java.util.GregorianCalendar;

//import javax.ws.rs.core.CacheControl;
//import javax.ws.rs.core.Response.ResponseBuilder;

public class BookCacheController {
	private final int maxAge;
	private final GregorianCalendar nextUpdate;
	
	public int getMaxAge() {
		return maxAge;
	}

	public GregorianCalendar getNextUpdate() {
		return nextUpdate;
	}

	public BookCacheController(Book book) {
		if (book.getIsbn().equals("1235")) {
			this.nextUpdate = new GregorianCalendar(2000, 0, 1);
			this.maxAge = 0;
			
		}else { 
			GregorianCalendar now = new GregorianCalendar();
			this.nextUpdate = getNextUpdateTime(now, book);
			this.maxAge = (int) ((nextUpdate.getTimeInMillis() - now.getTimeInMillis()) / 1000L);
		}
	}

	private static GregorianCalendar getNextUpdateTime(GregorianCalendar now, Book book) {
		GregorianCalendar nextUpdate = new GregorianCalendar();
		if (book.getCacheType()==1) {
			nextUpdate.setTime(now.getTime());
			nextUpdate.set(Calendar.HOUR_OF_DAY, Book.BOOK_FIRST_UPDATE_HOUR_OF_DAY);
			nextUpdate.set(Calendar.MINUTE, 0);
			nextUpdate.set(Calendar.SECOND, 0);
			nextUpdate.set(Calendar.MILLISECOND, 0);
			if (now.get(Calendar.HOUR_OF_DAY) >= Book.BOOK_FIRST_UPDATE_HOUR_OF_DAY) {
				nextUpdate.add(Calendar.DAY_OF_YEAR, 1);
			}
		}
		return nextUpdate;
	}
}