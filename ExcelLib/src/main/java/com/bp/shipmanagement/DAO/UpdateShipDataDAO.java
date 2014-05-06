package com.bp.shipmanagement.DAO;

import java.util.List;
import java.util.Set;

public interface UpdateShipDataDAO {
	public Set<String> getMainIndentPKs();
	public Set<String> getSubIndentPKs();
	public void insertMainIndent(List<String> row);
	public void updateMainIndent(List<String> row);
	public void insertSubIndent(List<String> row);
	public void updateSubIndent(List<String> row);
	//public 
}
