package com.bp.samples.graphs.dependency;

import java.util.List;
import java.util.Map;

public interface CmdProcessorDAO {
	public int insertComponent(String[] component);
	public List<Map<String,Object>> getStateTable();
	public List<String> getDependencyChains(String obj);
	public boolean isObjRegistered(String obj);
	public void installUninstallBaseObj(String obj, boolean bInstall, boolean bInstallExplicit);
	public boolean isObjInstalled(String obj);
	public boolean isObjExplicitlyInstalled(String obj);
	public boolean isObjDependentFree(String obj);
	public List<String> getInstalledComponents();
}
