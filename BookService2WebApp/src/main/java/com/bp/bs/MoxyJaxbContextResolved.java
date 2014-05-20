package com.bp.bs;

import javax.ws.rs.ext.ContextResolver;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.persistence.jaxb.JAXBContextFactory;

public class MoxyJaxbContextResolved implements ContextResolver<JAXBContext> {
	//private org.eclipse.persistence.jaxb.JAXBContextFactory factory = new JAXBContextFactory(); 
	@Override
	public JAXBContext getContext(Class<?> cls) {
		try {
			return JAXBContextFactory.createContext(new Class[] {cls.getClass()}, null);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return null;
	}

}
