package com.bp.samples.graphs.dependency;

import java.util.List;

public interface CmdProcessor {
	public int registerComponents(String[] objs);
	public int installComponent(String component);
	public int removeComponent(String component);
	public List<String>listInstalledComponents();
}
