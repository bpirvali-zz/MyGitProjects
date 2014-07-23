package com.bp.samples.graphs.dependency;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component("cmdProcessorDAO")
public class CmdProcessorDAOImpl implements CmdProcessorDAO {
	private static final Logger logger = LoggerFactory.getLogger(CmdProcessorDAOImpl.class);
	
	@Autowired
	@Qualifier("npJdbcTemplate")
	private NamedParameterJdbcTemplate npJdbcTemplate;

	@Autowired
	@Qualifier("jdbcTemplate")
	private JdbcTemplate jdbcTemplate;
	
	
	private final Set<String> insertedComponents = new HashSet<String>();
	private final Set<String> insertedObj = new HashSet<String>();
	private final Map<String,Integer> objIDMap = new HashMap<String,Integer>();
	private Integer obj=null;
	private int index = -1;
	private int objIndex = -1;
	
	public int insertComponent(String[] component) {
		String sComp = Util.componentToString(component);
		if (insertedComponents.contains(sComp))
			return 0;
		if (component[1].equals("")) {
			if (insertedObj.contains(component[0])) {
				logger.debug("component already inserted:{}", sComp);
				return 0;
			}
		}
		
		obj = objIDMap.get(component[0]);
		if (obj!=null)
			objIndex = obj.intValue();
		else  {
			index++;
			objIndex = index;
		}
		logger.info("Inserting:{}, {}", objIndex, sComp);
		// INSERT INTO obj_dependency(ID, INSTALLED, OBJ, DEPENDENT_ON) VALUES(0, false, 'NETCARD', '');
		String sql = "INSERT INTO obj_dependency"
				+ "(ID, INSTALLED, OBJ, DEPENDENT_ON)"
				+ "VALUES(:ID, :INSTALLED, :OBJ, :DEPENDENT_ON)";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ID", objIndex);
		parameters.put("INSTALLED", new Boolean(false));
		parameters.put("OBJ", component[0]);
		parameters.put("DEPENDENT_ON", component[1]);
		

		npJdbcTemplate.update(sql, parameters);
		
		insertedComponents.add(sComp);
		insertedObj.add(component[0]);
		objIDMap.put(component[0], index);
		
		return 0;
	}
	
	@Override
	public List<Map<String, Object>> getStateTable() {
		String sql = "SELECT * FROM OBJ_DEPENDENCY";
		return jdbcTemplate.queryForList(sql);
	}

	@Override
	public List<String> getDependencyChains(String obj) {
		String sql = 
			"WITH dep_tree(ID, installed, obj, dep, route, LEVEL) AS ( " +
				"SELECT id, installed, obj, dependent_on, " +
					"CAST(CONCAT(obj, '-->', dependent_on) AS VARCHAR(128)) , 1 " +
				"FROM obj_dependency " +
				"WHERE obj='" + obj + "' " +
				"UNION ALL " +
				"SELECT a.id, a.installed, a.obj, b.dependent_on, " +
				"    CAST(CONCAT(a.route, '-->', b.dependent_on) AS VARCHAR(128)) , a.LEVEL + 1 " +
				"FROM dep_tree a INNER JOIN obj_dependency b ON b.obj = a.dep) " +
				"SELECT route from dep_tree " +
				"WHERE dep!='' " +
				"AND dep IN (SELECT obj FROM obj_dependency WHERE dependent_on='')";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public boolean isObjRegistered(String obj) {
		String sql = "SELECT COUNT(*) FROM obj_dependency WHERE obj='" + obj + "'";
		long cnt = jdbcTemplate.queryForObject(sql, Long.class);
		if (cnt>0)
			return true;
		
		return false;
	}
	
	@Override
	public void installUninstallBaseObj(String obj, boolean bInstall, boolean bInstallExplicit) {
		if (bInstall)
			System.out.printf("\tInstalling  %s\n", obj);
		else 
			System.out.printf("\tRemoving  %s\n", obj);
		String sql = "UPDATE obj_dependency SET " 
				+ "INSTALLED=:INSTALLED, " 
				+ "INSTALLED_EXPLICIT=:INSTALLED_EXPLICIT " 
				+ "WHERE OBJ=:OBJ";
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("INSTALLED", new Boolean(bInstall));
		parameters.put("INSTALLED_EXPLICIT", new Boolean(bInstallExplicit));
		parameters.put("OBJ", obj);
		
		npJdbcTemplate.update(sql, parameters);
	}

	@Override
	public boolean isObjInstalled(String obj) {
		String sql = "SELECT COUNT(*) FROM obj_dependency WHERE obj='" + obj + "' AND INSTALLED=true";
		long cnt = jdbcTemplate.queryForObject(sql, Long.class);
		if (cnt>0)
			return true;
		
		return false;
	}

	@Override
	public boolean isObjDependentFree(String obj) {
		String sql = "SELECT COUNT(*) FROM obj_dependency WHERE dependent_on='" + obj + "' AND INSTALLED=true";
		long cnt = jdbcTemplate.queryForObject(sql, Long.class);
		if (cnt>0)
			return false;
		
		return true;
	}

	@Override
	public List<String> getInstalledComponents() {
		String sql = 
				"SELECT DISTINCT(obj) FROM obj_dependency WHERE installed=true";
		return jdbcTemplate.queryForList(sql, String.class);
	}

	@Override
	public boolean isObjExplicitlyInstalled(String obj) {
		String sql = "SELECT COUNT(*) FROM obj_dependency WHERE obj='" + obj + "' AND installed_explicit=true";
		long cnt = jdbcTemplate.queryForObject(sql, Long.class);
		if (cnt>0)
			return true;
		
		return false;
	}
}
