package com.bp.bs;

public class Review {
	private String by;
	private String text;

	public Review(String by, String text) {
		this.by = by;
		this.text = text;
	}

	public String getBy() {
		return by;
	}

	public void setBy(String by) {
		this.by = by;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
