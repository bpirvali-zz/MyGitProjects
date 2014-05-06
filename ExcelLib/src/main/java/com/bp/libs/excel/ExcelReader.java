package com.bp.libs.excel;

import java.util.List;

//import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public interface ExcelReader {
	// Global 
	public int getNumberOfSheets();
	public List<String> getSheetNames();
	//public HSSFWorkbook getWorkBook(String excelFile);
	
	// sheet level
	public String getSheetName(int sheetNo);
	
	public List<String> getTableFields(int sheetNo, int rowNo, int colNo, int length);
	public List<String> getTableFields(int sheetNo);
	
	//public List<Map<String, String>> getTableData(int sheetNo);
	public List<List<String>> getTableData(int sheetNo);
	//public Map<String>
	// cell level	
}
