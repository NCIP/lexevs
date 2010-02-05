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
package org.lexgrid.loader.data;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DataUtilsTest {
	
	private EntityAssnsToEntity assoc;
	
	@Before
	public void setup() {
		assoc = new EntityAssnsToEntity();
		assoc.setCodingSchemeName("cs");
		assoc.setContainerName("container");
		assoc.setEntityCode("testRelation");
		assoc.setEntityCodeNamespace("NS");
		assoc.setSourceEntityCode("sourceCode");
		assoc.setSourceEntityCodeNamespace("sourceNS");
		assoc.setTargetEntityCode("targetCode");
		assoc.setTargetEntityCodeNamespace("targetNS");
		assoc.setMultiAttributesKey("12345");
	}

	@Test
	public void deepCloneEntityAssnsToEntity() throws Exception {
		EntityAssnsToEntity cloned = DataUtils.deepCloneEntityAssnsToEntity(assoc);
		assertNotNull(cloned);
	}
	
	/**
	 * Test deep clones are equal.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testDeepClonesAreEqual() throws Exception {
		EntityAssnsToEntity cloned = DataUtils.deepCloneEntityAssnsToEntity(assoc);
		assertTrue(cloned.getCodingSchemeName().equals("cs"));
		assertTrue(cloned.getContainerName().equals("container"));
		assertTrue(cloned.getEntityCode().equals("testRelation"));
		assertTrue(cloned.getEntityCodeNamespace().equals("NS"));
		assertTrue(cloned.getSourceEntityCode().equals("sourceCode"));
		assertTrue(cloned.getSourceEntityCodeNamespace().equals("sourceNS"));
		assertTrue(cloned.getTargetEntityCode().equals("targetCode"));
		assertTrue(cloned.getTargetEntityCodeNamespace().equals("targetNS"));
		assertTrue(cloned.getMultiAttributesKey().equals("12345"));
	}
	
	@Test
	public void testDeepClonesAreNotTheSame() throws Exception {
		EntityAssnsToEntity cloned = DataUtils.deepCloneEntityAssnsToEntity(assoc);
		assertFalse(cloned == assoc);
	}
}
