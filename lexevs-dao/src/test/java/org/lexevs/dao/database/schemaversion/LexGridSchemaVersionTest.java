/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.schemaversion;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

/**
 * The Class LexGridSchemaVersionTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexGridSchemaVersionTest extends LexEvsDbUnitTestBase {

	/**
	 * Test parse database version.
	 */
	@Test
	public void testParseDatabaseVersion(){
		LexGridSchemaVersion version = LexGridSchemaVersion.parseStringToVersion("1.2");
		assertTrue(version.getMajorVersion() == 1);
		assertTrue(version.getMinorVersion() == 2);
	}
	
	/**
	 * Test equals.
	 */
	@Test
	public void testEquals(){
		LexGridSchemaVersion version1 = LexGridSchemaVersion.parseStringToVersion("1.2");
		LexGridSchemaVersion version2 = LexGridSchemaVersion.parseStringToVersion("1.2");
		assertTrue(version1.isEqualVersion(version2));
		assertTrue(version2.isEqualVersion(version1));
	}
	
	/**
	 * Test not equals.
	 */
	@Test
	public void testNotEquals(){
		LexGridSchemaVersion version1 = LexGridSchemaVersion.parseStringToVersion("1.2");
		LexGridSchemaVersion version2 = LexGridSchemaVersion.parseStringToVersion("2.2");
		assertFalse(version1.isEqualVersion(version2));
		assertFalse(version2.isEqualVersion(version1));
	}
}