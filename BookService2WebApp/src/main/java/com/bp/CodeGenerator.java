package com.bp;

import com.sun.tools.xjc.XJCFacade;

public class CodeGenerator {
	public static void main(String[] args) throws Throwable {
		XJCFacade.main(new String[] { 
				"-b", "src/main/resources/Bindings.xml",
				"-d", "src/main/java",
				"src/main/resources/BookService.xsd" });
	}
}
