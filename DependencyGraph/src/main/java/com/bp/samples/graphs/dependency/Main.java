package com.bp.samples.graphs.dependency;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length==0) {
			showSyntax();
			System.exit(0);
		}
		
		logger.debug("Boostraping spring context...");
		AbstractApplicationContext appCtx = new ClassPathXmlApplicationContext(new String[] {"AppCtx-Main.xml", "AppCtx-JDBC.xml"});
		appCtx.registerShutdownHook();
		logger.debug("DONE!\n");
		
		logger.debug("Getting command processor object...");
		CmdProcessor cmdProc = (CmdProcessor)appCtx.getBean("cmdProcessor");
		CmdProcessorDAO dao = (CmdProcessorDAO)appCtx.getBean("cmdProcessorDAO");
		logger.debug("DONE!\n");
		
		logger.debug("Reading the input file:{}...", args[0]);
		Path inputFile=Paths.get(args[0]);
		Charset charset = Charset.forName("US-ASCII");
		try (BufferedReader reader = Files.newBufferedReader(inputFile, charset)) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	String[] cmdArray = line.split("\\s+");
		    	
		    	// register 
		    	if (cmdArray.length>0 && cmdArray[0].equals("DEPEND")) {
		    		System.out.printf("%s\n", line);
		    		cmdProc.registerComponents(cmdArray);
		    		
		    	// install	
		    	} else if (cmdArray.length>0 && cmdArray[0].equals("INSTALL")) {
		    		System.out.printf("%s\n", line);
		    		for (int i=1; i<cmdArray.length; i++) {
		    			cmdProc.installComponent(cmdArray[i]);
		    		}
		    	// remove	
		    	} else if (cmdArray.length>0 && cmdArray[0].equals("REMOVE")) {
		    		System.out.printf("%s\n", line);
		    		for (int i=1; i<cmdArray.length; i++) {
		    			cmdProc.removeComponent(cmdArray[i]);
		    		}
		    	} else if (cmdArray.length>0 && cmdArray[0].equals("LIST")) {
		    		System.out.printf("%s\n", line);
		    		cmdProc.listInstalledComponents();
		    	} else if (cmdArray.length>0 && cmdArray[0].equals("END")) {
		    		System.out.printf("%s\n", line);
		    		System.exit(0);
		    	} else {
		    		throw new RuntimeException("Unknown command:" + cmdArray[0]);
		    	}
		    }
		} catch (IOException x) {
			logger.error("Failed to read:" + inputFile, x);
		}
		List<Map<String, Object>> tbl = dao.getStateTable();
		for (Map<String,Object> row:tbl) {
			logger.debug("({},{},{},{})", row.get("ID"), row.get("INSTALLED"), row.get("OBJ"), row.get("DEPENDENT_ON"));
		}		
	}
	
	private static void showSyntax() {
		System.out.printf("DependencyGraph <input-filename>\n");
		
	}
}
