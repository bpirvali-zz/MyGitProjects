package com.bp.shipmanagement.DAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.bp.libs.excel.ExcelReader;
import com.bp.shipmanagement.DAO.UpdateShipDataDAO;

//@ContextConfiguration(locations = { "classpath:AppCtx-Test.xml", "classpath:AppCtx-Main.xml" })
//public class JdbcTemplateTest extends AbstractTestNGSpringContextTests {
public class JdbcDAOTest {
	//@Inject private JdbcTemplate jdbcTemplate;
	AbstractApplicationContext appCtx = null;
	private JdbcTemplate jdbcTemplate = null;
	private NamedParameterJdbcTemplate npJdbcTemplate = null;
	
	private ExcelReader excelReader = null;
	private UpdateShipDataDAO updateShipDataDAO = null;
	protected static class TestRow {
		int ID;
		String NAME;

		public TestRow() {
			ID = 0;
			NAME = "";
		}

		public TestRow(int iD, String nAME) {
			super();
			ID = iD;
			NAME = nAME;
		}

		public int getID() {
			return ID;
		}

		public void setID(int iD) {
			ID = iD;
		}

		public String getNAME() {
			return NAME;
		}

		public void setNAME(String nAME) {
			NAME = nAME;
		}

		public String toString() {
			return "TestTable[" + ID + "," + NAME + "]";
		}
	}
	@BeforeClass
	public void beforeClass() {
		System.setProperty("EXCEL_FILE", "./src/test/resources/TestExcelFile-97-2004.xls");
		appCtx = new
		ClassPathXmlApplicationContext(new String[] {"AppCtx-Main.xml", "AppCtx-Test.xml"});
		excelReader = (ExcelReader)appCtx.getBean("excelReader");
		jdbcTemplate = (JdbcTemplate)appCtx.getBean("jdbcTemplate");
		npJdbcTemplate = (NamedParameterJdbcTemplate)appCtx.getBean("npJdbcTemplate");
		updateShipDataDAO = (UpdateShipDataDAO)appCtx.getBean("updateShipDataDAO");
		appCtx.registerShutdownHook();
	}
	
	@Test
	public void testInsertNamedParameter() {
		String sql = "INSERT INTO dbo.MainIndent " +
				"(ShipId, BDesc, Inco, Lstd, cancel) VALUES (:ShipId, :BDesc, :Inco, :Lstd, :cancel)";
	 
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ShipId", "Behzad");
			parameters.put("BDesc", "Pirvali");
			parameters.put("Inco", "2013_1214_001_B");
			parameters.put("Lstd", 2);
			parameters.put("cancel", false);
	 
			npJdbcTemplate.update(sql, parameters);
			AssertStringField("dbo.MainIndent", "BDesc", "Pirvali", "Inco='2013_1214_001_B'");
	}

	@Test()
	public void testUpdatetNamedParameter() {
		String sql = "UPDATE dbo.MainIndent SET " +
				"ShipId=:ShipId, Lstd=:Lstd WHERE inco='2013_1214_001_A'";
	 
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("ShipId", "Behzad");
			parameters.put("Lstd", 3);
	 
			npJdbcTemplate.update(sql, parameters);
			
			sql = "SELECT ShipId FROM dbo.MainIndent WHERE inco='2013_1214_001_A'";
			String sShipID = jdbcTemplate.queryForObject(sql, String.class);
			Assert.assertEquals(sShipID, "Behzad");

			sql = "SELECT Lstd FROM dbo.MainIndent WHERE inco='2013_1214_001_A'";
			int Lstd = jdbcTemplate.queryForObject(sql, Integer.class);
			Assert.assertEquals(Lstd, 3);
	}
	
	@Test
	public void testInsertMainIndent() {
		List<List<String>> tbl = excelReader.getTableData(1);
		List<String> row0 = tbl.get(0);
		row0.set(13, "Behzad-01");
		updateShipDataDAO.insertMainIndent(row0);
		AssertRowCount("dbo.MainIndent", 2);
	}
	
	@Test
	public void testUpdatetMainIndent() {
		List<List<String>> tbl = excelReader.getTableData(1);
		List<String> row0 = tbl.get(0);
		row0.set(0, "Behzad");
		updateShipDataDAO.updateMainIndent(row0);
		AssertStringField("dbo.MainIndent", "ShipId", "Behzad", "Inco='2013_1214_001_A'");
	}
	
	@Test
	public void testgetMainIndentPKs() {
		Set<String> incos = updateShipDataDAO.getMainIndentPKs();
		for (String s: incos)
			System.out.println(s);
		//AssertStringField("dbo.MainIndent", "ShipId", "Behzad", "Inco='2013_1214_001_A'");
	}
	
	@Test
	public void testgetSubIndentPKs() {
		Set<String> incos = updateShipDataDAO.getSubIndentPKs();
		for (String s: incos)
			System.out.println(s);
		//AssertStringField("dbo.MainIndent", "ShipId", "Behzad", "Inco='2013_1214_001_A'");
	}
	
	@Test
	public void testJdbcTemplate() {
		String query = "SELECT * FROM TEST";
		List<TestRow> testRows = jdbcTemplate.query(query,
				new BeanPropertyRowMapper<TestRow>(TestRow.class));
		for (TestRow row : testRows)
			System.out.println(row);
	}

	private void AssertRowCount(String tbl, int count) {
		String sql = "SELECT COUNT(*) FROM " + tbl;
		int cnt = jdbcTemplate.queryForObject(sql, Integer.class);
		Assert.assertEquals(cnt, count);
	}

	private void AssertStringField(String tbl, String field, String value, String condition) {
		String sql = "SELECT " + field + " FROM " + tbl + " WHERE " + condition;
		String s = jdbcTemplate.queryForObject(sql, String.class);
		Assert.assertEquals(s, value);
	}
}
