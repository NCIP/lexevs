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
package org.lexgrid.loader.database;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.batch.support.DatabaseType;

/**
 * The Class DatabaseTypeFactoryTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DatabaseTypeFactoryTest {

	/**
	 * Gets the database type oracle.
	 * 
	 * @return the database type oracle
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void getDatabaseTypeOracle() throws Exception {
		DatabaseTypeFactory factory = new DatabaseTypeFactory();

		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con);  
		expect(con.getMetaData()).andReturn(dmd);  
		expect(dmd.getDatabaseProductName()).andReturn("Oracle");  
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		DatabaseType type = (DatabaseType)factory.getObject();
		assertTrue(type.equals(DatabaseType.ORACLE));
	}
	
	/**
	 * Gets the database type mysql.
	 * 
	 * @return the database type mysql
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void getDatabaseTypeMysql() throws Exception {
		DatabaseTypeFactory factory = new DatabaseTypeFactory();

		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con);  
		expect(con.getMetaData()).andReturn(dmd);  
		expect(dmd.getDatabaseProductName()).andReturn("MySQL");  
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		DatabaseType type = (DatabaseType)factory.getObject();
		assertTrue(type.equals(DatabaseType.MYSQL));
	}
	
	/**
	 * Gets the database type hsql.
	 * 
	 * @return the database type hsql
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void getDatabaseTypeHsql() throws Exception {
		DatabaseTypeFactory factory = new DatabaseTypeFactory();

		DataSource dataSource = createMock(DataSource.class);
		
		factory.setDataSource(dataSource);

		DatabaseMetaData dmd = createMock(DatabaseMetaData.class);  
		Connection con = createMock(Connection.class);  
		expect(dataSource.getConnection()).andReturn(con);  
		expect(con.getMetaData()).andReturn(dmd);  
		expect(dmd.getDatabaseProductName()).andReturn("HSQL Database Engine");  
		replay(dataSource, con, dmd);  
		factory.afterPropertiesSet();  
	
		DatabaseType type = (DatabaseType)factory.getObject();
		assertTrue(type.equals(DatabaseType.HSQL));
	}
}
