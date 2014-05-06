package com.bp.libs.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class ExcelReaderImpl implements ExcelReader {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ExcelReaderImpl.class);
	private HSSFWorkbook wb = null;
	
	public ExcelReaderImpl(String excelFile) throws FileNotFoundException, IOException {
		File file = new File(excelFile);
		try (FileInputStream fis = new FileInputStream(file)) {
			wb = new HSSFWorkbook(fis);
			// BUG: Missing Cell Policy is not honored!!!
			wb.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
		}
	}

	@Override
	public int getNumberOfSheets() {
		return wb.getNumberOfSheets();
	}

	@Override
	public List<String> getSheetNames() {
		List<String> l = new ArrayList<String>();
		int nSheets = wb.getNumberOfSheets();
		Sheet sheet;
		for (int i=0; i<nSheets; i++) {
			sheet = wb.getSheetAt(i);
			l.add(sheet.getSheetName());
		}
		return l;
	}

	@Override
	public String getSheetName(int sheetNo) {
		Sheet sheet = wb.getSheetAt(sheetNo);
		return sheet.getSheetName();
	}

	@Override
	public List<String> getTableFields(int sheetNo, int rowNo, int colNo, int length) {
		throw new RuntimeException("Not implemented yet!");
		// return null;
	}

	@Override
	public List<String> getTableFields(int sheetNo) {
		List<String> l = null;
		Sheet sheet = wb.getSheetAt(sheetNo);
		Iterator<Row> rowIterator = sheet.iterator();
		if (rowIterator.hasNext()) {
			HSSFRow row = (HSSFRow) rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			l = new ArrayList<String>();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				l.add(cell.getStringCellValue());
			}
		}
		return l;
	}

	@Override
	public List<List<String>> getTableData(int sheetNo) {
		List<List<String>> l = null;
		List<String> keys = new ArrayList<String>();
		
		// get rows
		Sheet sheet = wb.getSheetAt(sheetNo);
		Iterator<Row> rowIterator = sheet.iterator();
		
		HSSFRow row = null;
		Iterator<Cell> cellIterator = null;
		Cell cell = null;
		// get keys
		if (rowIterator.hasNext()) {
			row = (HSSFRow) rowIterator.next();
			cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				cell = cellIterator.next();
				keys.add(cell.getStringCellValue());
			}
		}
		
		List<String> list = null;
		l = new ArrayList<List<String>>();
		
		// get table data
		while (rowIterator.hasNext()) {
			row = (HSSFRow) rowIterator.next();
			//cellIterator = row.cellIterator();			
			list = new ArrayList<String>();
			//int i=0;
			int nLastCell = row.getLastCellNum();
			for (int cn=0; cn<nLastCell; cn++) {
			//while (cellIterator.hasNext()) {
				cell = row.getCell(cn, Row.CREATE_NULL_AS_BLANK);
				//map.put(keys.get(i++), cell.getStringCellValue());
				int type = cell.getCellType();
				if (type==Cell.CELL_TYPE_STRING)
					//list.add("'" + cell.getStringCellValue() + "'");
					list.add(cell.getStringCellValue());
				else if (type==Cell.CELL_TYPE_BOOLEAN)
					list.add("" + cell.getBooleanCellValue());
				else if (type==Cell.CELL_TYPE_BLANK)
					list.add("null");
				else if (type==Cell.CELL_TYPE_ERROR)
					list.add("+++ ERROR +++");
				else if (type==Cell.CELL_TYPE_NUMERIC)
					list.add("" + cell.getNumericCellValue());
				else if (type==Cell.CELL_TYPE_FORMULA)
					list.add("" + cell.getCellFormula());
			}
			l.add(list);
		}
		return l;
	}

//	@Override
//	public List<Map<String, String>> getTableData(int sheetNo) {
//		List<Map<String, String>> l = null;
//		List<String> keys = new ArrayList<String>();
//		
//		// get rows
//		Sheet sheet = wb.getSheetAt(sheetNo);
//		Iterator<Row> rowIterator = sheet.iterator();
//		
//		HSSFRow row = null;
//		Iterator<Cell> cellIterator = null;
//		Cell cell = null;
//		// get keys
//		if (rowIterator.hasNext()) {
//			row = (HSSFRow) rowIterator.next();
//			cellIterator = row.cellIterator();
//			while (cellIterator.hasNext()) {
//				cell = cellIterator.next();
//				keys.add(cell.getStringCellValue());
//			}
//		}
//		
//		Map<String,String> map = null;
//		l = new ArrayList<Map<String,String>>();
//		
//		// get table data
//		while (rowIterator.hasNext()) {
//			row = (HSSFRow) rowIterator.next();
//			cellIterator = row.cellIterator();
//			map = new HashMap<String,String>();
//			int i=0;
//			while (cellIterator.hasNext()) {
//				cell = cellIterator.next();
//				map.put(keys.get(i++), cell.getStringCellValue());
//			}
//			l.add(map);
//		}
//		return l;
//	}

}
