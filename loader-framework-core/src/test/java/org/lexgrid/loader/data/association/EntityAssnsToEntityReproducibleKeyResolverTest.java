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
package org.lexgrid.loader.data.association;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.junit.Test;

/**
 * The Class EntityAssnsToEntityReproducibleKeyResolverTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityReproducibleKeyResolverTest {

	/**
	 * Test generate key.
	 */
	@Test
	public void testGenerateKey(){
		EntityAssnsToEntity assoc = new EntityAssnsToEntity();
		assoc.setCodingSchemeName("testCsName");
		assoc.setContainerName("testContainerName");
		assoc.setEntityCode("testEntityCode");
		assoc.setEntityCodeNamespace("testNS");
		assoc.setSourceEntityCode("source");
		assoc.setSourceEntityCodeNamespace("sourceNs");
		assoc.setTargetEntityCode("target");
		assoc.setTargetEntityCodeNamespace("targetNs");
		
		EntityAssnsToEntityReproducibleKeyResolver resolver = new EntityAssnsToEntityReproducibleKeyResolver();
		String key = resolver.resolveMultiAttributesKey(assoc);
		assertTrue(key != null);
	}
	
	/**
	 * Test retrieve key.
	 */
	@Test
	public void testRetrieveKey(){
		EntityAssnsToEntity assoc = new EntityAssnsToEntity();
		assoc.setCodingSchemeName("testCsName");
		assoc.setContainerName("testContainerName");
		assoc.setEntityCode("testEntityCode");
		assoc.setEntityCodeNamespace("testNS");
		assoc.setSourceEntityCode("source");
		assoc.setSourceEntityCodeNamespace("sourceNs");
		assoc.setTargetEntityCode("target");
		assoc.setTargetEntityCodeNamespace("targetNs");
		
	
		
		EntityAssnsToEntity assoc2 = new EntityAssnsToEntity();
		assoc2.setCodingSchemeName("testCsName");
		assoc2.setContainerName("testContainerName");
		assoc2.setEntityCode("testEntityCodeDIFFERENT");
		assoc2.setEntityCodeNamespace("testNS");
		assoc2.setSourceEntityCode("source");
		assoc2.setSourceEntityCodeNamespace("sourceNs");
		assoc2.setTargetEntityCode("target");
		assoc2.setTargetEntityCodeNamespace("targetNs");
		
		
		EntityAssnsToEntity assoc3 = new EntityAssnsToEntity();
		assoc3.setCodingSchemeName("testCsName");
		assoc3.setContainerName("testContainerName");
		assoc3.setEntityCode("testEntityCode");
		assoc3.setEntityCodeNamespace("testNS");
		assoc3.setSourceEntityCode("source");
		assoc3.setSourceEntityCodeNamespace("sourceNs");
		assoc3.setTargetEntityCode("target");
		assoc3.setTargetEntityCodeNamespace("targetNs");
		
		
		EntityAssnsToEntityReproducibleKeyResolver resolver = new EntityAssnsToEntityReproducibleKeyResolver();
		
		String key1 = resolver.resolveMultiAttributesKey(assoc);
		String key2 = resolver.resolveMultiAttributesKey(assoc2);
		String key3 = resolver.resolveMultiAttributesKey(assoc3);
		
		assertTrue(key1.equals(key3));
		assertFalse(key1.equals(key2));
		assertFalse(key2.equals(key3));
	}
}
