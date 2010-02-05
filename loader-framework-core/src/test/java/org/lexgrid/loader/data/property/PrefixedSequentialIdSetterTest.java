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
package org.lexgrid.loader.data.property;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.persistence.model.EntityProperty;
import org.LexGrid.persistence.model.EntityPropertyId;
import org.junit.Test;
import org.lexgrid.loader.data.DataUtils;

/**
 * The Class PrefixedSequentialIdSetterTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PrefixedSequentialIdSetterTest {

	/**
	 * Test add ids.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testAddIds() throws Exception {
		EntityProperty prop1 = new EntityProperty();
		EntityPropertyId propId1 = new EntityPropertyId();		
		propId1.setCodingSchemeName("test");
		propId1.setEntityCode("test");
		propId1.setEntityCodeNamespace("test");
		propId1.setPropertyId("test");	
		prop1.setLanguage("en");
		prop1.setId(propId1);
		prop1.setPropertyType("test");
		prop1.setPropertyName("test");
		prop1.setPropertyValue("test");
		prop1.setIsPreferred(false);
		
		EntityProperty prop2 = DataUtils.deepCloneEntityProperty(prop1);
		
		EntityProperty prop3 = DataUtils.deepCloneEntityProperty(prop1);
		prop3.setLanguage("FR");
		
		EntityProperty prop4 = DataUtils.deepCloneEntityProperty(prop1);
		prop4.setLanguage("SP");
		
		List<EntityProperty> props = new ArrayList<EntityProperty>();
		
		props.add(prop1);
		props.add(prop2);
		props.add(prop3);
		props.add(prop4);
		
		PrefixedSequentialIdSetter idSetter = new PrefixedSequentialIdSetter();
		idSetter.setPrefix("TEST_PREFIX");
		idSetter.addIds(props);
		
		assertTrue(props.get(0).getId().getPropertyId().equals("TEST_PREFIX-1"));
		assertTrue(props.get(1).getId().getPropertyId().equals("TEST_PREFIX-2"));
		assertTrue(props.get(2).getId().getPropertyId().equals("TEST_PREFIX-3"));
		assertTrue(props.get(3).getId().getPropertyId().equals("TEST_PREFIX-4"));		
	}
}
