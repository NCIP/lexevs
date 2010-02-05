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
package org.LexGrid.persistence.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

import javax.sql.DataSource;

import org.junit.Test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

/**
 * The Class DatabaseDialectFactoryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseDialectFactoryTest {

	/**
	 * Test get dialect oracle11g.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetDialectOracle11g() throws Exception {
		DatabaseDialectFactory factory = new DatabaseDialectFactory();
		
		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con).anyTimes();  
 
		expect(dmd.getDatabaseProductName()).andReturn("Oracle").anyTimes();
		expect(dmd.getDatabaseMajorVersion()).andReturn(11).anyTimes();   
		
		expect(con.getMetaData()).andReturn(dmd).anyTimes();  
		
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		Class dialect = (Class)factory.getObject();
		
		assertTrue(dialect.equals(org.hibernate.dialect.Oracle10gDialect.class));
		
	}
	
	/**
	 * Test get dialect oracle10g.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetDialectOracle10g() throws Exception {
		DatabaseDialectFactory factory = new DatabaseDialectFactory();
		
		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con).anyTimes();  
 
		expect(dmd.getDatabaseProductName()).andReturn("Oracle").anyTimes();
		expect(dmd.getDatabaseMajorVersion()).andReturn(10).anyTimes();   
		
		expect(con.getMetaData()).andReturn(dmd).anyTimes();  
		
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		Class dialect = (Class)factory.getObject();
		
		assertTrue(dialect == null);
		
	}
	
	/**
	 * Test get dialect mysql.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetDialectMysql() throws Exception {
		DatabaseDialectFactory factory = new DatabaseDialectFactory();
		
		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con).anyTimes();  
 
		expect(dmd.getDatabaseProductName()).andReturn("MySQL").anyTimes();
		
		expect(con.getMetaData()).andReturn(dmd).anyTimes();  
		
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		Class dialect = (Class)factory.getObject();
		
		assertTrue(dialect == null);
		
	}
	
	/**
	 * Test get dialect any other autodetect.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testGetDialectAnyOtherAutodetect() throws Exception {
		DatabaseDialectFactory factory = new DatabaseDialectFactory();
		
		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con).anyTimes();  
 
		expect(dmd.getDatabaseProductName()).andReturn("Some_Other_Database").anyTimes();
		
		expect(con.getMetaData()).andReturn(dmd).anyTimes();  
		
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		Class dialect = (Class)factory.getObject();
		
		assertTrue(dialect == null);
		
	}
	
}
