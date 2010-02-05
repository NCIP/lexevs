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
package org.LexGrid.persistence.dao;

import java.util.List;

import org.LexGrid.persistence.LexEVSTestBase;
import org.LexGrid.persistence.model.Association;
import org.LexGrid.persistence.model.AssociationId;
import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * The Class LexEvsDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsDaoTest extends LexEVSTestBase {
	
	/** The dao. */
	private LexEvsDao dao = (LexEvsDao)getCtx().getBean("lexEvsDao");
	
	/** The assoc1. */
	private Association assoc1;
	
	/** The assoc2. */
	private Association assoc2;
	
	private EntityAssnsToEntity assocToEntity;
	
	/**
	 * Sets the up.
	 */
	@Before
	public void setUp(){
		
		assoc1 = new Association();
		AssociationId assocId1 = new AssociationId();
		assocId1.setCodingSchemeName("1");
		assocId1.setContainerName("2");
		assocId1.setEntityCode("TestAssoc1");
		assocId1.setEntityCodeNamespace("4");
		assoc1.setAssociationName("5");
		assoc1.setForwardName("6");
		assoc1.setReverseName("7");
		assoc1.setId(assocId1);
		
		assoc2 = new Association();
		AssociationId assocId2 = new AssociationId();
		assocId2.setCodingSchemeName("1");
		assocId2.setContainerName("2");
		assocId2.setEntityCode("TestAssoc2");
		assocId2.setEntityCodeNamespace("4");
		assoc2.setAssociationName("5");
		assoc2.setForwardName("6");
		assoc2.setReverseName("7");
		assoc2.setId(assocId2);
		
		assocToEntity = new EntityAssnsToEntity();
		assocToEntity.setMultiAttributesKey("12345");
		assocToEntity.setCodingSchemeName("csName");
		assocToEntity.setContainerName("containerName");
		assocToEntity.setEntityCodeNamespace("NS");
		assocToEntity.setEntityCode("code");
		assocToEntity.setSourceEntityCode("sourceCode");
		assocToEntity.setSourceEntityCodeNamespace("sourceNS");
		assocToEntity.setTargetEntityCode("targetCode");
		assocToEntity.setTargetEntityCodeNamespace("targetNS");
	}
	
	/**
	 * Test insert1.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsert1() throws Exception {
		dao.insert(assoc1);
		
		List<Association> assocList = dao.query(assoc1);
		assertTrue(assocList.get(0).getId().getEntityCode().equals(assoc1.getId().getEntityCode()));
		assertTrue(assocList.size() == 1);
	}
	
	/**
	 * Test insert2.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsert2() throws Exception {
		dao.insert(assoc2);
		
		List<Association> assocList = dao.query(assoc2);
		System.out.println(assocList.size());
		assertTrue(assocList.get(0).getId().getEntityCode().equals(assoc2.getId().getEntityCode()));
		assertTrue(assocList.size() == 1);
	}
	
	@Test
	public void testInsertNoId() throws Exception {
		dao.insert(assocToEntity);
		EntityAssnsToEntity assoc = dao.findById(EntityAssnsToEntity.class, "12345");
		assertNotNull(assoc);
		assertTrue(assoc.getMultiAttributesKey().equals("12345"));
	}
	
	/**
	 * Test find by id.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testFindById() throws Exception {
		Association foundAssoc = dao.findById(Association.class, assoc1.getId());
		assertTrue(foundAssoc != null);	
		assertTrue(foundAssoc.getId().getEntityCode().equals(assoc1.getId().getEntityCode()));		
	}
	
	/**
	 * Test detached criteria query limit.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testDetachedCriteriaQueryLimit() throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Association.class);
		List<Association> assocList = dao.query(Association.class, dc, 1);
		assertTrue(assocList != null);	
		assertTrue(assocList.size() == 1);		
	}
	
	/**
	 * Test query.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testQuery() throws Exception {
		Association assoc = new Association(new AssociationId(), null, null);
		List<Association> assocList = dao.query(assoc);
		assertTrue(assocList != null);	
		assertTrue(assocList.size() == 2);		
	}
	
	@Test
	public void testQueryNoIdField() throws Exception {
		List<EntityAssnsToEntity> results = dao.query(assocToEntity);
		assertTrue(results.size() == 1);
		EntityAssnsToEntity result = results.get(0);
		assertTrue(result.getMultiAttributesKey().equals("12345"));
	}
	
	/**
	 * Test detached criteria query.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testDetachedCriteriaQuery() throws Exception {
		DetachedCriteria dc = DetachedCriteria.forClass(Association.class);
		List<Association> assocList = dao.query(Association.class, dc, -1);
		assertTrue(assocList != null);	
		assertTrue(assocList.size() == 2);		
	}
}
