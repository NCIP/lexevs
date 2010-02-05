/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.persistence.spring;

import org.hibernate.dialect.Oracle10gDialect;
import org.junit.Test;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import static org.junit.Assert.*;

/**
 * The Class DialectSettingPostProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DialectSettingPostProcessorTest {

	/**
	 * Test set dialect oracle.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSetDialectOracle() throws Exception {
		DialectSettingPostProcessor processor = new DialectSettingPostProcessor();
		processor.setDialect(Oracle10gDialect.class);
		
		LocalSessionFactoryBean session = new LocalSessionFactoryBean();
		
		assertTrue(session.getHibernateProperties().size() == 0);	
		
		session = (LocalSessionFactoryBean)processor.postProcessBeforeInitialization(session, "session");
		
		assertTrue(session.getHibernateProperties().size() == 1);
		
		String dialect = (String)session.getHibernateProperties().get("hibernate.dialect");
		
		assertTrue(dialect.equals(Oracle10gDialect.class.getName()));
	}
	
	/**
	 * Test set dialect autodetect.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testSetDialectAutodetect() throws Exception {
		DialectSettingPostProcessor processor = new DialectSettingPostProcessor();
		processor.setDialect(null);
		
		LocalSessionFactoryBean session = new LocalSessionFactoryBean();
		
		assertTrue(session.getHibernateProperties().size() == 0);	
		
		session = (LocalSessionFactoryBean)processor.postProcessBeforeInitialization(session, "session");
		
		assertTrue(session.getHibernateProperties().size() == 0);
	
	}
}
