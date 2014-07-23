package com.bp.samples.graphs.dependency;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("cmdProcessor")
public class CmdProcessorImpl implements CmdProcessor {
	private static final Logger logger = LoggerFactory.getLogger(CmdProcessorImpl.class);
	
	@Autowired
	@Qualifier("cmdProcessorDAO")
	private CmdProcessorDAO cmdProcessorDAO;
	
	@Override
	public int registerComponents(String[] objs) {
		String obj, dep_obj;
		for (int i=objs.length; i>=2; i--) {
			if (i==objs.length)
				dep_obj="";
			else
				dep_obj = objs[i];
			obj=objs[i-1];
			
			String[] component = {obj,dep_obj};
			
			cmdProcessorDAO.insertComponent(component);
			
		}
		return 0;
	}

	@Override
	public int installComponent(String component) {
		if (cmdProcessorDAO.isObjInstalled(component)) {
			System.out.printf("\t%s is already installed!\n", component);
			return 0;
		}
		
		List<String> l = cmdProcessorDAO.getDependencyChains(component);
		logger.debug("----------------------------");
		logger.debug(" -- component:{}", component);
		logger.debug("----------------------------");
		for (String s:l) {
			String[] tokens = s.split("-->");
			for(int i=tokens.length-1; i>=0; i--) {
				if (!cmdProcessorDAO.isObjInstalled(tokens[i].trim()))
					if (i==0)
						cmdProcessorDAO.installUninstallBaseObj(tokens[i], true, true);
					else
						cmdProcessorDAO.installUninstallBaseObj(tokens[i], true, false);
			}
			
		}
		
		// if list empty
		if (l.size()==0) {
			if (!cmdProcessorDAO.isObjRegistered(component)) {
				cmdProcessorDAO.insertComponent(new String[] {component,""});
			}
			cmdProcessorDAO.installUninstallBaseObj(component, true, true);
		}
		return 0;
	}

	@Override
	public int removeComponent(String component) {
		if (!cmdProcessorDAO.isObjInstalled(component)) {
			System.out.printf("\t%s is not installed!\n", component);
			return 0;
		}
		if (cmdProcessorDAO.isObjDependentFree(component)) {
			cmdProcessorDAO.installUninstallBaseObj(component, false, false);
			
			// removing all base components not installed explicitly
			List<String> l = cmdProcessorDAO.getDependencyChains(component);
			logger.debug("----------------------------");
			logger.debug(" -- component:{}", component);
			logger.debug("----------------------------");
			for (String s:l) {
				String[] tokens = s.split("-->");
				for(int i=0; i<tokens.length; i++) {
					if (cmdProcessorDAO.isObjInstalled(tokens[i].trim())) 
						if (!cmdProcessorDAO.isObjExplicitlyInstalled(tokens[i].trim())) 
							if (cmdProcessorDAO.isObjDependentFree(tokens[i].trim()))							
								cmdProcessorDAO.installUninstallBaseObj(tokens[i], false, false);
				}
			}
			
		} else 
			System.out.printf("\t%s is still needed!\n", component);
		
		return 0;
	}

	@Override
	public List<String> listInstalledComponents() {
		List<String> l =cmdProcessorDAO.getInstalledComponents();
		for (String s:l)
			System.out.printf("%s\n", s);
		return null;
	}

}
