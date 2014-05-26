package com.bp.bs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
// Git test-01
public class Book {
	// Net Data
	private String isbn;
	private String title;
	private List<Review> reviews;

	// Versioning Info
	private Date lastModified;
	private long version;

	// Cache policy
	private final int firstUpdateHourOfday;
	private final int nCacheType;
	
	public final static int BOOK_FIRST_UPDATE_HOUR_OF_DAY = 10; // 10 AM Local Time
	
	public int getFirstUpdateHourOfday() {
		return firstUpdateHourOfday;
	}

	public int getCacheType() {
		return nCacheType;
	}

//	public int getMaxAge() {
//		//GregorianCalendar now = new GregorianCalendar();
////		this.nextUpdate = getNextUpdateTime(now, book);
//		return (int) ((getNextUpdate().getTimeInMillis() - now.getTimeInMillis()) / 1000L);
//	}

	public Book(String isbn, String title) {
		this.isbn = isbn;
		this.title = title;
		this.lastModified = genLastModifiedDate(new Date());
		this.version = 0;
		
		this.firstUpdateHourOfday = BOOK_FIRST_UPDATE_HOUR_OF_DAY;
		this.nCacheType = 1;
		this.reviews = genReviews();
		//GregorianCalendar now = new GregorianCalendar();
		//this.nextUpdate = getNextUpdateTime(now, BOOK_FIRST_UPDATE_HOUR_OF_DAY, 1);
		//this.maxAge = (int)((nextUpdate.getTimeInMillis() - now.getTimeInMillis())/1000) ;
	}

	
	public String getIsbn() {
		return isbn;
	}

	public String getTitle() {
		return title;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public long getVersion() {
		return version;
	}
	
//	public void setIsbn(String isbn) {
//		this.isbn = isbn;
//		version++;
//	}

	public void setTitle(String title) {
		this.title = title;
		this.lastModified = genLastModifiedDate(new Date());
		version++;
	}
	
	public List<Review> getReviews() {
		return reviews;
	}

//	public void setLastModified(Date lastModified) {
//		this.lastModified = lastModified;
//		version++;
//	}

//	public List<Review> getReviews() {
//		return reviews;
//	}	
	private Date genLastModifiedDate(Date date) {
		Date lastModified = date;
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setTime(lastModified);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	private List<Review> genReviews() {
		List<Review> l = new ArrayList<Review>();
		l.add(new Review("Ross", "Excellent Book and Author!"));
		l.add(new Review("Noor", "Awesome Author!"));
		l.add(new Review("Sooleng", "MUST have Book!"));
		return l;
	}
	// CacheType 
	//	1..Once a day update
	//	2..Anytime
//	private static GregorianCalendar getNextUpdateTime(GregorianCalendar now, int HourOfDay, int nCacheType) {
//		GregorianCalendar nextUpdate = new GregorianCalendar();
//		if (nCacheType==1) { // Once Per Day update 
//			nextUpdate.setTime(now.getTime());
//			nextUpdate.set(Calendar.HOUR_OF_DAY, HourOfDay);
//			nextUpdate.set(Calendar.MINUTE, 0);
//			nextUpdate.set(Calendar.SECOND, 0);
//			nextUpdate.set(Calendar.MILLISECOND, 0);
//			if (now.get(Calendar.HOUR_OF_DAY) >= HourOfDay) {
//				nextUpdate.add(Calendar.DAY_OF_YEAR, 1);
//			}
//		} else if (nCacheType==2) {
//			nextUpdate.add(Calendar.YEAR, -100);
//		} else if (nCacheType==3) {
////			nextUpdate.setTime(now.getTime());
////			nextUpdate.set(Calendar.HOUR_OF_DAY, HourOfDay);
////			nextUpdate.set(Calendar.MINUTE, 0);
////			nextUpdate.set(Calendar.SECOND, 0);
////			nextUpdate.set(Calendar.MILLISECOND, 0);
////			if (now.get(Calendar.HOUR_OF_DAY) >= HourOfDay) {
////				nextUpdate.add(Calendar.DAY_OF_YEAR, 1);
////			}
//		}
//		return nextUpdate;
//	}
}