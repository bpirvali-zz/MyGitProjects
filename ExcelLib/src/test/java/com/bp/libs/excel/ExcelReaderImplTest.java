package com.bp.libs.excel;

import java.util.List;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

//@ContextConfiguration(locations = { "classpath:AppCtx-Main.xml" })
//public class ExcelReaderImplTest extends AbstractTestNGSpringContextTests {
public class ExcelReaderImplTest {

	// private static final Logger logger =
	// LoggerFactory.getLogger(ExcelReaderImplTest.class);
	// private static final String testFile =
	// "./src/test/resources/TestExcelFile-97-2004.xls";
	/*
*/
	private AbstractApplicationContext appCtx;
	//@Inject
	private ExcelReader excelReader;

	private static final String[] sheetNames = { "VslCrtStsQ", "MainIndentQ",
			"SubIndentQ" };
	
	//(ShipId, CertDesc, DOIssue, DOExpiry, RngComnt, POIssue, IssueDate, Expr1)
	public static final String[] table0 = { "ShipId", "CertDesc", "DOIssue",
			"DOExpiry", "RngComnt", "POIssue", "IssueDate", "Expr1" };
	
	//(ShipId, BDesc, shipname, Code, SCode, Ordy, OrdM, OrdD, OnBr, DOI, Char, OrDt, Lstd, Inco, Mstr, Rnk, IsNm, RcvdDt, RcvdPrt, cancel, Suplr, extracted, extorder, Stts, SndDt1, Cpy2, Cpy41, Fin, DlvryRqrd, SttsDt, StsId)
	public static final String[] table1 = { "ShipId", "BDesc", "shipname",
			"Code", "SCode", "Ordy", "OrdM", "OrdD", "OnBr", "DOI", "Char",
			"OrDt", "Lstd", "Inco", "Mstr", "Rnk", "IsNm", "RcvdDt", "RcvdPrt",
			"cancel", "Suplr", "extracted", "extorder", "Stts", "SndDt1",
			"Cpy2", "Cpy41", "Fin", "DlvryRqrd", "SttsDt", "StsId" };

	//(Inco, Iinco, Impa, QttyRq, Item, Unit, StStk, ROB, QttyRcv, DRcvd, Rcvd, amount, prcvd, INo, OfRsd, BgIno)
	public static final String[] table2 = { "Inco", "Iinco", "Impa", "QttyRq",
			"Item", "Unit", "StStk", "ROB", "QttyRcv", "DRcvd", "Rcvd",
			"amount", "prcvd", "INo", "OfRsd", "BgIno" };

	@Test
	public void testGetSheetName() {
		Assert.assertEquals("MainIndentQ", excelReader.getSheetName(1));
	}

	@Test
	public void testGetTable0Fields() {
		List<String> l = excelReader.getTableFields(0);
		int i = 0;
		for (String s : l) {
			// System.out.println("Field[" + (i) + "]=" + s);
			Assert.assertEquals(s, table0[i++]);
		}
	}

	// Field[13]=Inco
	@Test
	public void testGetTable1Fields() {
		List<String> l = excelReader.getTableFields(1);
		int i = 0;
		for (String s : l) {
			System.out.println("Field[" + (i) + "]=" + s);
			Assert.assertEquals(s, table1[i++]);
		}
	}

	@Test
	public void testGetTable2Fields() {
		List<String> l = excelReader.getTableFields(2);
		int i = 0;
		for (String s : l) {
			// System.out.println("Field[" + (i) + "]=" + s);
			Assert.assertEquals(s, table2[i++]);
		}
	}

	@Test
	public void testGetTable0Data() {
		int i = 0;
		List<List<String>> l = excelReader.getTableData(0);
		StringBuilder sRow= new StringBuilder(256);
		for (List<String> list : l) {
			i = 0;
			sRow.setLength(0);
			sRow.append("(");
			for (String entry : list) {
				sRow.append(entry);
				if (i<list.size()-1)
					sRow.append(",");
				i++;
			}
			sRow.append(")");
			System.out.println(sRow.toString());
			Assert.assertEquals(i, 8);
		}
	}

	@Test
	public void testGetTable1Data() {
		int i = 0;
		List<List<String>> l = excelReader.getTableData(1);
		StringBuilder sRow= new StringBuilder(256);
		for (List<String> list : l) {
			i = 0;
			sRow.setLength(0);
			sRow.append("(");
			for (String entry : list) {
				sRow.append(entry);
				if (i<list.size()-1)
					sRow.append(",");
				i++;
			}
			sRow.append(")");
			System.out.println(sRow.toString());
			Assert.assertEquals(i, 31);
		}
	}

	@Test
	public void testGetTable2Data() {
		int i = 0;
		List<List<String>> l = excelReader.getTableData(2);
		StringBuilder sRow= new StringBuilder(256);
		for (List<String> list : l) {
			i = 0;
			sRow.setLength(0);
			sRow.append("(");
			for (String entry : list) {
				sRow.append(entry);
				if (i<list.size()-1)
					sRow.append(",");
				i++;
			}
			sRow.append(")");
			System.out.println(sRow.toString());
			Assert.assertEquals(i, 16);
		}
	}

	@Test
	public void testGetNumberOfSheets() {
		int nSheets = excelReader.getNumberOfSheets();
		Assert.assertTrue(nSheets >= 3, "No of sheets:" + nSheets
				+ " is not bigger or equal to 4");
	}

	@Test
	public void testGetSheetNames() {
		List<String> l = excelReader.getSheetNames();
		int i = 0;
		for (String s : l) {
			System.out.println("sheet-" + i + ":" + s);
			i++;
		}
		Assert.assertEquals(l.get(0), sheetNames[0]);
		Assert.assertEquals(l.get(1), sheetNames[1]);
		Assert.assertEquals(l.get(2), sheetNames[2]);
	}

	@BeforeMethod
	public void beforeMethod() {
	}

	@AfterMethod
	public void afterMethod() {
	}

	@BeforeClass
	public void beforeClass() {
		System.setProperty("EXCEL_FILE", "./src/test/resources/TestExcelFile-97-2004.xls");
		appCtx = new
		ClassPathXmlApplicationContext(new String[] {"AppCtx-Main.xml", "AppCtx-Test.xml"});
		excelReader = (ExcelReader)appCtx.getBean("excelReader");
		appCtx.registerShutdownHook();
	}

	@AfterClass
	public void afterClass() {
	}

	@BeforeTest
	public void beforeTest() {
	}

	@AfterTest
	public void afterTest() {
	}
	// @Test
	// public void testExcelRead() {
	// InputStream in;
	// try {
	// in = ExcelReaderImplTest.class.getResourceAsStream(testFile);
	//
	// //Get the workbook instance for XLS file
	// HSSFWorkbook workbook = new HSSFWorkbook(in);
	//
	// //Get no of sheets
	// workbook.getNumberOfSheets();
	//
	// //Get first sheet from the workbook
	// HSSFSheet sheet = workbook.getSheetAt(0);
	//
	// // Get sheet name
	// sheet.getSheetName();
	//
	// //Get iterator to all the rows in current sheet
	// Iterator<Row> rowIterator = sheet.iterator();
	// while(rowIterator.hasNext()) {
	// HSSFRow row = (HSSFRow) rowIterator.next();
	//
	// //Get iterator to all cells of current row
	// Iterator<Cell> cellIterator = row.cellIterator();
	// while (cellIterator.hasNext()) {
	// Cell cell = cellIterator.next();
	// System.out.println("Cell(" + cell.getRowIndex() + "," +
	// cell.getColumnIndex() + ")="
	// + cell.getStringCellValue());
	// }
	// }
	//
	// } catch (FileNotFoundException e) {
	//
	// logger.error("File Not Found:'" + testFile +"'", e);
	// //e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
}
