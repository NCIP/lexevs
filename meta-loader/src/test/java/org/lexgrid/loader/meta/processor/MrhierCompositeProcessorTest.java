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
package org.lexgrid.loader.meta.processor;

import static org.easymock.EasyMock.cmp;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.LexGrid.persistence.model.EntityAssnsToEquals;
import org.LexGrid.persistence.model.EntityPropertyMultiAttrib;
import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Test;
import org.lexgrid.loader.processor.support.MinimalMultiAttribResolver;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.rrf.model.Mrhier;
import org.lexgrid.loader.rrf.staging.MrconsoStagingDao;

import test.util.SupportHelpers;

/**
 * The Class MrhierAssocQualProcessorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrhierCompositeProcessorTest {
	
	/** The processor. */
	private MrhierCompositeProcessor processor;
	
	/** The mrhier. */
	private Mrhier mrhier;

	/**
	 * Sets the up.
	 * 
	 * @throws Exception the exception
	 */
	@Before
	public void setUp() throws Exception {
		processor = new MrhierCompositeProcessor();
		processor.setQualifierResolver(new QualifierResolver<Mrhier>() {

			public String getQualifierName() {
				return "testAssocQualifier";
			}

			public String getQualifierValue(Mrhier item) {
				return item.getHcd();
			}
		});
		
		List<MinimalMultiAttribResolver<Mrhier>> propQualsResolvers = new ArrayList<MinimalMultiAttribResolver<Mrhier>>();
		
		propQualsResolvers.add(new MinimalMultiAttribResolver<Mrhier>() {

			public String getAttributeValue(Mrhier item) {
				return "testHcdPropQualifier";
			}

			public String getTypeName() {
				return null;
			}

			public String getVal1(Mrhier item) {
				return item.getHcd();
			}

			public String getVal2(Mrhier item) {
				return null;
			}
			
			}
		);
		
		propQualsResolvers.add(new MinimalMultiAttribResolver<Mrhier>() {

			public String getAttributeValue(Mrhier item) {
				return "testPtrPropQualifier";
			}

			public String getTypeName() {
				return null;
			}

			public String getVal1(Mrhier item) {
				return item.getPtr();
			}

			public String getVal2(Mrhier item) {
				return null;
			}
			
			}
		);
		processor.setPropertyMultiAttribResolvers(propQualsResolvers);
		
		EntityAssnsToEntity rel1 = this.buildEntityAssnsToEntity("cui", "cui1");
		rel1.setMultiAttributesKey("1");
		EntityAssnsToEntity rel2 = this.buildEntityAssnsToEntity("cui1", "cui2");
		rel2.setMultiAttributesKey("2");
		EntityAssnsToEntity rel3 = this.buildEntityAssnsToEntity("cui2", "cui3");
		rel3.setMultiAttributesKey("3");
		
		LexEvsDao lexEvsDao = createMock(LexEvsDao.class);  
		expect(lexEvsDao.query(cmp(rel1, new EntityAssnsToEntityComparator(), LogicalOperator.EQUAL))).andReturn(Arrays.asList(rel1));  
		expect(lexEvsDao.query(cmp(rel2, new EntityAssnsToEntityComparator(), LogicalOperator.EQUAL))).andReturn(Arrays.asList(rel2));  
		expect(lexEvsDao.query(cmp(rel3, new EntityAssnsToEntityComparator(), LogicalOperator.EQUAL))).andReturn(Arrays.asList(rel3));  
		
		replay(lexEvsDao);  
		
		processor.setLexEvsDao(lexEvsDao);
		
		MrconsoStagingDao stagingDao = createMock(MrconsoStagingDao.class);
		
		expect(stagingDao.getCuiFromAui("aui1")).andReturn("cui1");
		expect(stagingDao.getCuiFromAui("aui2")).andReturn("cui2");
		expect(stagingDao.getCuiFromAui("aui3")).andReturn("cui3");
		replay(stagingDao);
		
		processor.setMrconsoStagingDao(stagingDao);
		
		processor.setCodingSchemeNameSetter(new SupportHelpers.TestCodingSchemeNameSetter());	
			
		mrhier = new Mrhier();
		mrhier.setPtr("aui1.aui2.aui3");
		mrhier.setCui("cui");
		mrhier.setAui("aui");
		mrhier.setSab("sab");
		mrhier.setHcd("hcd");
	}
	
	/**
	 * Test quals exist.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testExist() throws Exception {
		List<Object> quals = processor.process(mrhier);
		assertTrue(quals.size() > 0);
	}
	

	@Test
	public void testAssocQualSize() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityAssnsToEquals> equals = getQualifierTypes(quals, EntityAssnsToEquals.class);
		assertTrue("Size: " + equals.size(), equals.size() == 3);
	}
	
	@Test
	public void testAssocQual1() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityAssnsToEquals> equals = getQualifierTypes(quals, EntityAssnsToEquals.class);
		
		assertTrue(isEntityAssnsToEqualsEqual(equals, "testAssocQualifier", "hcd", "1"));
	}
	
	@Test
	public void testAssocQual2() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityAssnsToEquals> equals = getQualifierTypes(quals, EntityAssnsToEquals.class);
		
		assertTrue(isEntityAssnsToEqualsEqual(equals, "testAssocQualifier", "hcd", "2"));
	}
	
	@Test
	public void testAssocQual3() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityAssnsToEquals> equals = getQualifierTypes(quals, EntityAssnsToEquals.class);
		
		assertTrue(isEntityAssnsToEqualsEqual(equals, "testAssocQualifier", "hcd", "3"));
	}
	
	@Test
	public void testPropertyQualSize() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		assertTrue("Size: " + equals.size(), equals.size() == 8);
	}
	
	@Test
	public void testHcdPropertyQual() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testHcdPropQualifier", "hcd", "aui"));
	}
	
	@Test
	public void testHcdPropertyQual1() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testHcdPropQualifier", "hcd", "aui1"));
	}
	
	@Test
	public void testHcdPropertyQual2() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testHcdPropQualifier", "hcd", "aui2"));
	}
	
	@Test
	public void testHcdPropertyQual3() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testHcdPropQualifier", "hcd", "aui3"));
	}
	
	@Test
	public void testPtrPropertyQual() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testPtrPropQualifier", "aui1.aui2.aui3", "aui"));
	}
	
	@Test
	public void testPtrPropertyQual1() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testPtrPropQualifier", "aui1.aui2.aui3", "aui1"));
	}
	
	@Test
	public void testPtrPropertyQual2() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testPtrPropQualifier", "aui1.aui2.aui3", "aui2"));
	}
	
	@Test
	public void testPtrPropertyQual3() throws Exception {
		List<Object> quals = processor.process(mrhier);
		List<EntityPropertyMultiAttrib> equals = getQualifierTypes(quals, EntityPropertyMultiAttrib.class);
		
		assertTrue(isEntityPropertyMultiAttribEqual(equals, "testPtrPropQualifier", "aui1.aui2.aui3", "aui3"));
	}

	/**
	 * Test path to root.
	 */
	@Test
	public void testPathToRoot() {
		List<String> ptr = processor.getPathToRoot(mrhier.getPtr());
		assertTrue(ptr.size() == 3);
		assertTrue(ptr.get(0).equals("cui1"));
		assertTrue(ptr.get(1).equals("cui2"));
		assertTrue(ptr.get(2).equals("cui3"));
	}
	
	/**
	 * The Class EntityAssnsToEntityComparator.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	private class EntityAssnsToEntityComparator implements Comparator<EntityAssnsToEntity> {

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(EntityAssnsToEntity o1, EntityAssnsToEntity o2) {
			if(o1.getSourceEntityCode().equals(o2.getSourceEntityCode()) &&
					o1.getTargetEntityCode().equals(o2.getTargetEntityCode())){
				return 0;
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * Builds the entity assns to entity.
	 * 
	 * @param sourceCode the source code
	 * @param targetCode the target code
	 * 
	 * @return the entity assns to entity
	 */
	private EntityAssnsToEntity buildEntityAssnsToEntity(String sourceCode, String targetCode){
		EntityAssnsToEntity assoc = new EntityAssnsToEntity();
		assoc.setCodingSchemeName(new SupportHelpers.TestCodingSchemeNameSetter().getCodingSchemeName());
		assoc.setSourceEntityCode(sourceCode);
		assoc.setTargetEntityCode(targetCode);
		return assoc;		
	}
	
	private <T> List<T> getQualifierTypes(List<Object> objects, Class<T> clazz) {
		List<T> returnList = new ArrayList<T>();
		for(Object obj : objects) {
			if(obj.getClass() == clazz) {
				returnList.add((T)obj);
			}
		}
		return returnList;
	}
	
	private boolean isEntityAssnsToEqualsEqual(List<EntityAssnsToEquals> quals, String name, String value, String key) {
		for(EntityAssnsToEquals qual : quals) {
			if(qual.getId().getQualifierName().equals(name) &&
				qual.getId().getQualifierValue().equals(value) &&
				qual.getId().getMultiAttributesKey().equals(key)){
				return true;
			}
		}
		throw new RuntimeException("Didn't find qual.");
	}
	
	private boolean isEntityPropertyMultiAttribEqual(List<EntityPropertyMultiAttrib> quals, String name, String value, String key) {
		for(EntityPropertyMultiAttrib qual : quals) {
			if(qual.getId().getAttributeValue().equals(name) &&
				qual.getId().getVal1().equals(value) &&
				qual.getId().getPropertyId().equals(key)){
				return true;
			}
		}
		throw new RuntimeException("Didn't find qual.");
	}
}
