package com.bp.samples.graphs.dependency;


public class Util {
	public static String componentToString(String[] component) {
		if(component.length!=2)
			throw new RuntimeException("Invalid compnent-lenght(valid=2):" + 
					component.length);
		StringBuilder s = new StringBuilder();
		s.append(component[0]);
		s.append("-");
		s.append(component[1]);
		
		return s.toString();
	}
}
