package com.bp.shipmanagement;


import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bp.libs.excel.ExcelReader;
import com.bp.libs.excel.ExcelReaderImpl;
import com.bp.shipmanagement.DAO.UpdateShipDataDAO;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static ExcelReader excelReader = null;
	private static UpdateShipDataDAO updateShipDataDAO = null;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		System.setProperty("EXCEL_FILE", args[0]);
		System.out.println("------------------------------------------------------------------------------");
		System.out.printf( "-- Ship Data Update starting...\n");
		System.out.printf( "-- EXCEL_FILE=%s\n", System.getProperty("EXCEL_FILE"));
		System.out.println("------------------------------------------------------------------------------");
		
		System.out.printf("Connecting to the Database...");
		AbstractApplicationContext appCtx = new
		ClassPathXmlApplicationContext(new String[] {"AppCtx-Main.xml", "AppCtx-JDBC.xml"});
		appCtx.registerShutdownHook();
		System.out.printf("DONE!\n");
		
		System.out.printf("Creating the excel object...");
		excelReader = (ExcelReader)appCtx.getBean("excelReader");
		System.out.printf("DONE!\n");
		
		System.out.printf("Creating the database object...");
		updateShipDataDAO = (UpdateShipDataDAO)appCtx.getBean("updateShipDataDAO");		
		System.out.printf("DONE!\n");
		
		System.out.printf("Updating MainIndent Table...");
		List<List<String>> 	tbl = excelReader.getTableData(1);
		Set<String> 		PKs = updateShipDataDAO.getMainIndentPKs();
		for (List<String> row: tbl) {
			if (PKs.contains(row.get(13)))
				updateShipDataDAO.updateMainIndent(row);
			else
				updateShipDataDAO.insertMainIndent(row);
		}
		System.out.printf("DONE!\n");

		System.out.printf("Updating SubIndent Table...");
		tbl = excelReader.getTableData(2);
		PKs = updateShipDataDAO.getSubIndentPKs();
		for (List<String> row: tbl) {
			//System.out.printf("Iinco:%s\n", row.get(1));			
			if (PKs.contains(row.get(1)))
				updateShipDataDAO.updateSubIndent(row);
			else
				updateShipDataDAO.insertSubIndent(row);
		}
		System.out.printf("DONE!\n");
		System.out.printf("Ship Data Update DONE in %s [msec]!\n", (System.currentTimeMillis()-startTime));
	}

}
