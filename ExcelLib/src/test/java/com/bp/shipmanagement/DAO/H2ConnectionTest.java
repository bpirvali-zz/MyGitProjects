package com.bp.shipmanagement.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.jdbcx.JdbcConnectionPool;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class H2ConnectionTest {
  @Test
  public void testH2Connection() throws SQLException, ClassNotFoundException {
	  JdbcConnectionPool cp = JdbcConnectionPool.create( "jdbc:h2:tcp://localhost:9092/~/test_db;MODE=MSSQLServer", "sa", "");
	  Connection conn = cp.getConnection();
	  Statement stmt = conn.createStatement();
	  Assert.assertNotNull(stmt);
	  ResultSet rs = stmt.executeQuery("SELECT INCO FROM MAININDENT");
	  while(rs.next()) {
		  System.out.println(rs.getString("INCO"));
	  }
	  rs.close();	  
	  conn.close();
	  cp.dispose();
	  //"jdbc:h2:tcp://localhost/~/test_db;MODE=MSSQLServer", 	  "sa","");
	  
  }

  @Test
  public void testH2Connection2() throws SQLException, ClassNotFoundException {
	  Class.forName("org.h2.Driver");
	  Connection conn = 
	  DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/~/test_db;MODE=MSSQLServer", "sa", "");
	  Statement stmt = conn.createStatement();
	  Assert.assertNotNull(stmt);
	  
	  ResultSet rs = stmt.executeQuery("SELECT INCO FROM MAININDENT");
	  while(rs.next()) {
		  System.out.println(rs.getString("INCO"));
	  }
	  rs.close();
	  conn.close();
	  
  }
  
  @BeforeClass
  public void beforeClass() {
  }

  @AfterClass
  public void afterClass() {
  }

}
