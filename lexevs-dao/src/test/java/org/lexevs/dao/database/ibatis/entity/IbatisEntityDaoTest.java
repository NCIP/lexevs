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
package org.lexevs.dao.database.ibatis.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.ibatis.codingscheme.IbatisCodingSchemeDao;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisEntityDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class IbatisEntityDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis entity dao. */
	@Autowired
	private IbatisEntityDao ibatisEntityDao;
	
	/** The ibatis coding scheme dao. */
	@Autowired
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;
	
	/** The cs id. */
	private String csId;
	
	/**
	 * Insert coding scheme.
	 */
	@Before
	public void insertCodingScheme() {
	CodingScheme cs = new CodingScheme();
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		csId = ibatisCodingSchemeDao.insertCodingScheme(cs);
	}
	
	/**
	 * Insert entity.
	 */
	@Test
	public void insertEntity(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		entity.addEntityType("type");
		
		entity.setOwner("entity owner");
		
		entity.setStatus("testing");
		
		entity.setEffectiveDate(effectiveDate);
		entity.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.DEPENDENT);
		es.setRelativeOrder(23l);
		entity.setEntryState(es);
		
		final String id = ibatisEntityDao.insertEntity(csId, entity);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Entity", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertTrue(rs.getString(2).equals(csId));
				assertTrue(rs.getString(3).equals("code"));
				assertTrue(rs.getString(4).equals("namespace"));
				assertTrue(rs.getBoolean(5) == true);
				assertTrue(rs.getBoolean(6) == true);
				assertTrue(rs.getString(7).equals("a description"));
				assertTrue(rs.getBoolean(8) == false);
				assertTrue(rs.getString(9).equals("entity owner"));
				assertTrue(rs.getString(10).equals("testing"));
				assertTrue(rs.getTimestamp(11).equals(effectiveDate));
				assertTrue(rs.getTimestamp(12).equals(expirationDate));
				
				String entryStateId = rs.getString(13);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "Entity");
				assertEquals(rs.getString(4), ChangeType.DEPENDENT.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
		
		template.queryForObject("Select * from EntityType", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), id);
				assertEquals(rs.getString(2), "type");
				
				return null;
			}
		});
	}
	
	/**
	 * Insert entity.
	 */
	@Test
	public void insertHistoryEntity(){
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		
		ibatisEntityDao.insertHistoryEntity(csId, entity);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("select count(*) from h_entity"));
	}
	
	/**
	 * Insert entity.
	 */
	@Test
	public void insertHistoryEntityWithProperty(){
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		
		Property prop = new Property();
		prop.setPropertyId("someId");
		prop.setPropertyName("name");
		prop.setValue(DaoUtility.createText("content"));
		
		entity.addProperty(prop);
		
		ibatisEntityDao.insertHistoryEntity(csId, entity);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("select count(*) from h_entity"));
		assertEquals(1, template.queryForInt("select count(*) from h_property"));
	}
	
	/**
	 * Insert entity.
	 */
	@Test
	public void insertHistoryEntityWithPropertyAndQualifier(){
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("namespace");
		entity.setIsDefined(true);
		entity.setIsAnonymous(true);
		entity.setIsActive(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("a description");
		entity.setEntityDescription(ed);
		
		Property prop = new Property();
		prop.setPropertyId("someId");
		prop.setPropertyName("name");
		prop.setValue(DaoUtility.createText("content"));
		
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName("qualName");
		qual.setPropertyQualifierType("qualType");
		qual.setValue(DaoUtility.createText("text"));
		prop.addPropertyQualifier(qual);
		
		entity.addProperty(prop);
		
		ibatisEntityDao.insertHistoryEntity(csId, entity);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("select count(*) from h_entity"));
		assertEquals(1, template.queryForInt("select count(*) from h_property"));
		assertEquals(1, template.queryForInt("select count(*) from h_propertymultiattrib"));
	}
	
	/**
	 * Test get all entities of coding scheme.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesOfCodingScheme() {
		int limit = 1000;
		
		for(int i=0;i<limit;i++) {
			Entity entity = new Entity();
			entity.setEntityCode("code" + String.valueOf(i));
			entity.setEntityCodeNamespace("namespace");
			entity.setIsDefined(true);
			entity.setIsAnonymous(true);
			entity.setIsActive(false);
			
			this.ibatisEntityDao.insertEntity(csId, entity);
		}
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 0, -1);
		
		assertEquals(limit, entities.size());
		
	}
	
	/**
	 * Test get all entities of coding scheme with limit.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesOfCodingSchemeWithLimit() {
		int limit = 1000;
		
		for(int i=0;i<limit;i++) {
			Entity entity = new Entity();
			entity.setEntityCode("code" + String.valueOf(i));
			entity.setEntityCodeNamespace("namespace");
			entity.setIsDefined(true);
			entity.setIsAnonymous(true);
			entity.setIsActive(false);
			
			this.ibatisEntityDao.insertEntity(csId, entity);
		}
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 0, 10);
		
		assertEquals(10, entities.size());
	}
	
	/**
	 * Test get all entities of coding scheme with limit and start.
	 */
	@Test
	@Transactional
	public void testGetAllEntitiesOfCodingSchemeWithLimitAndStart() {
		int limit = 1000;
		
		for(int i=0;i<limit;i++) {
			Entity entity = new Entity();
			entity.setEntityCode("code" + String.valueOf(i));
			entity.setEntityCodeNamespace("namespace");
			entity.setIsDefined(true);
			entity.setIsAnonymous(true);
			entity.setIsActive(false);
			
			this.ibatisEntityDao.insertEntity(csId, entity);
		}
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme(csId, 100, 100);
		
		assertEquals(100, entities.size());
	}
	
	/**
	 * Test lazy load presentations.
	 */
	@Test
	@Transactional
	public void testLazyLoadPresentations() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		List<? extends Entity> entities = ibatisEntityDao.getAllEntitiesOfCodingScheme("csguid", 0, -1);
		
		assertEquals(1, entities.size());
		
		Entity entity = entities.get(0);
		
		Presentation pres = entity.getPresentation()[0];
		
		assertNotNull(pres);
	}
	
	/**
	 * Test entity count.
	 */
	@Test
	@Transactional
	public void testEntityCount() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid2', 'csguid', 'ecode2', 'ens2')");
		
		int count = ibatisEntityDao.getEntityCount("csguid");
		
		assertEquals(2, count);
		
		int count2 = ibatisEntityDao.getEntityCount("BOGUScsguid");
		
		assertEquals(0, count2);
	}
}
