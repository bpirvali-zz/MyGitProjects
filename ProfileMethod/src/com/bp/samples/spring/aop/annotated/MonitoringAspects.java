package com.bp.samples.spring.aop.annotated;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;


@Aspect
public class MonitoringAspects{
	
	private Map<String, List<Long>> perfTimes = new HashMap<String, List<Long>>();
	private static final String LINE_SEP = System.getProperty("line.separator");
	
	@Pointcut(value="execution(public * *(..))")
	public void anyPublicMethod() {
	}
 
	@Around("anyPublicMethod() && @annotation(profileMethod)")
	public Object profileMethod(ProceedingJoinPoint pjp, ProfileMethod profileMethod) throws Throwable {
		System.out.println("Timing for:" + profileMethod.methodName() +  "(...) ...");
		
		// measure time
		long t = System.currentTimeMillis();
		Object o = pjp.proceed();
		t = System.currentTimeMillis() - t;
		
		// report it
		String sMethodName = profileMethod.methodName();
		addTimeToList(sMethodName, t);
		System.out.println(profileMethod.methodName() + "(...) DONE in " + t + " msec" );
		return o;
	}
	
	private void addTimeToList(String sKey, long lTime) {
		List<Long> l = perfTimes.get(sKey);
		if (l==null) {
			l = new ArrayList<Long>();
			l.add(lTime);
			perfTimes.put(sKey, l);
		} else {
			l.add(lTime);
		}
	}
	
	/* 
	 * report:
	 *     fun-name1, v1, v2,.. 
	 *     fun-name2, v1, v2,.. 
	 *     ........., v1, v2,.. 
	 */
	public String ReportDetail(String sStyle) throws Exception {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,List<Long>>> entries = perfTimes.entrySet();
		Iterator<Entry<String, List<Long>>> itEntries = entries.iterator();
		
		Entry<String, List<Long>> e = null;
		List<Long> l = null;
		if (sStyle.equalsIgnoreCase("csv")) {
			while(itEntries.hasNext()) {
				e = itEntries.next();
				
				// adding name
				sb.append("\"");
				sb.append(e.getKey());
				sb.append("\",");
				
				// adding values
				l = e.getValue();
				for (long n: l) {
					sb.append(n);
					sb.append(",");
				}
				sb.append(LINE_SEP);
			}			
		} else {
			throw new Exception("Style not supported!");
		}
		return sb.toString();
	}
	
	/* 
	 * report:
	 *     fun-name1, cnts, total,.. 
	 *     fun-name2, cnts, total,..
	 *     ........., cnts, total,..
	 */
	public String ReportSummary(String sStyle) throws Exception {
		StringBuilder sb = new StringBuilder();
		Set<Entry<String,List<Long>>> entries = perfTimes.entrySet();
		Iterator<Entry<String, List<Long>>> itEntries = entries.iterator();
		
		Entry<String, List<Long>> e = null;
		List<Long> l = null;
		if (sStyle.equalsIgnoreCase("csv")) {
			while(itEntries.hasNext()) {
				e = itEntries.next();
				
				// adding name
				sb.append("\"");
				sb.append(e.getKey());
				sb.append("\",");
				
				// adding counts and total
				l = e.getValue();
				long cnts=0, total=0;
				
				for (long n: l) {
					total+=n;
					cnts++;
				}
				sb.append(cnts);
				sb.append(",");
				sb.append(total);

				sb.append(LINE_SEP);
			}			
		} else {
			throw new Exception("Style not supported!");
		}
		return sb.toString();
	}

}