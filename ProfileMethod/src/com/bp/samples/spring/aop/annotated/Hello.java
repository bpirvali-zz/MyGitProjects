package com.bp.samples.spring.aop.annotated;

public class Hello {
	private String name;


	public void setName(String name) {
		this.name = name;
	}

	@ProfileMethod(methodName="getName")
	public String getName() {
		return name;
	}
}
