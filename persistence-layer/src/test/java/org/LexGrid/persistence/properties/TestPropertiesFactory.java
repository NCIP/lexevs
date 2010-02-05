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
package org.LexGrid.persistence.properties;

import java.util.Properties;

import org.LexGrid.LexBIG.Impl.helpers.SQLConnectionInfo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A factory for creating TestProperties objects.
 */
public class TestPropertiesFactory {

	/**
	 * Test get properties.
	 */
	@Test
	public void testGetProperties(){
		PropertiesFactory propFactory = new PropertiesFactory();
		
		SQLConnectionInfo connection = new SQLConnectionInfo();
		connection.dbName = "testDbName";
		connection.driver = "testDriver";
		connection.password = "testPassword";
		connection.prefix = "testPrefix";
		connection.username = "testUsername";
		connection.server = "testUrl";
		
		Properties props = propFactory.getProperties(connection);
		
		assertTrue(props.get(PropertiesFactory.DATABASE).equals("testDbName"));
		assertTrue(props.get(PropertiesFactory.DRIVER).equals("testDriver"));
		assertTrue(props.get(PropertiesFactory.USERNAME).equals("testUsername"));
		assertTrue(props.get(PropertiesFactory.PASSWORD).equals("testPassword"));
		assertTrue(props.get(PropertiesFactory.URL).equals("testUrl"));
		assertTrue(props.get(PropertiesFactory.PREFIX).equals("testPrefix"));	
	}
}
