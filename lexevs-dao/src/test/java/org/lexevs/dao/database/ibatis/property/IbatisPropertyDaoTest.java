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
package org.lexevs.dao.database.ibatis.property;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisPropertyDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration(transactionManager="transactionManager", defaultRollback=false)
@Transactional
public class IbatisPropertyDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis property dao. */
	@Autowired
	private IbatisPropertyDao ibatisPropertyDao;
	
	/**
	 * Insert presentation.
	 */
	@Test
	public void insertPresentation(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Presentation property = new Presentation();
		property.setPropertyId("pId");
		property.setPropertyName("propName");
		property.setPropertyType("propType");
		property.setLanguage("lang");
		property.setIsActive(true);
		property.setIsPreferred(false);
		property.setMatchIfNoContext(true);
		property.setDegreeOfFidelity("DOF");
		property.setRepresentationalForm("repForm");
		
		property.setOwner("property owner");
		
		property.setStatus("testing");
		Text text = new Text();
		text.setContent("prop value");
		text.setDataType("format");
		property.setValue(text);
		
		property.setEffectiveDate(effectiveDate);
		property.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.VERSIONABLE);
		es.setRelativeOrder(23l);
		property.setEntryState(es);
		
		String id = ibatisPropertyDao.insertProperty("fake-cs-id", "fake-entityCode-id", PropertyType.ENTITY, property);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertEquals(rs.getString(2),"fake-entityCode-id");
				assertEquals(rs.getString(3),"entity");
				assertEquals(rs.getString(4),"pId");
				assertEquals(rs.getString(5),"propType");
				assertEquals(rs.getString(6),"propName");
				assertEquals(rs.getString(7),"lang");
				assertEquals(rs.getString(8),"format");
				assertTrue(rs.getBoolean(9) == false);
				assertTrue(rs.getBoolean(10) == true);
				assertEquals(rs.getString(11),"DOF");
				assertEquals(rs.getString(12),"repForm");
				assertEquals(rs.getString(13),"prop value");
				assertTrue(rs.getBoolean(14) == true);
				assertTrue(rs.getString(15).equals("property owner"));
				assertTrue(rs.getString(16).equals("testing"));
				assertTrue(rs.getTimestamp(17).equals(effectiveDate));
				assertTrue(rs.getTimestamp(18).equals(expirationDate));
				String entryStateId = rs.getString(19);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "Property");
				assertEquals(rs.getString(4), ChangeType.VERSIONABLE.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
	}
	
	@Test
	public void updatePresentation(){
		final Timestamp timestamp1 = new Timestamp(1l);
		final Timestamp timestamp2 = new Timestamp(2l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'propId')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		Presentation prop = new Presentation();
		prop.setPropertyId("propId");
		prop.setPropertyName("pname");
		prop.setPropertyType("pType");
		prop.setValue(DaoUtility.createText("some updated value", "testFormat"));
		prop.setEffectiveDate(timestamp1);
		prop.setExpirationDate(timestamp2);
		prop.setIsActive(true);
		prop.setOwner("me");
		prop.setLanguage("Lang");
		prop.setStatus("testing");
		prop.setMatchIfNoContext(false);
		prop.setDegreeOfFidelity("dof");
		prop.setRepresentationalForm("testRepForm");
		prop.setIsPreferred(true);
		
		this.ibatisPropertyDao.updateProperty(
				"csguid", 
				"eguid", 
				"pguid", 
				PropertyType.ENTITY, 
				prop);
		
		template.queryForObject("Select * from property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertEquals(rs.getString(5), "pType");
				assertEquals(rs.getString(6), "pname");
				assertEquals(rs.getString(7), "Lang");
				assertEquals(rs.getString(8), "testFormat");
				assertEquals(rs.getBoolean(9), true);
				assertEquals(rs.getBoolean(10), false);
				assertEquals(rs.getString(11), "dof");
				assertEquals(rs.getString(12), "testRepForm");
				assertEquals(rs.getString(13), "some updated value");
				assertEquals(rs.getBoolean(14), true);
				assertEquals(rs.getString(15), "me");
				assertEquals(rs.getString(16), "testing");
				assertEquals(timestamp1.getTime(), rs.getTimestamp(17).getTime());
				assertEquals(timestamp2.getTime(), rs.getTimestamp(18).getTime());

				return null;
			}
		});
	}
	
	@Test
	public void updatePropert(){
		final Timestamp timestamp1 = new Timestamp(1l);
		final Timestamp timestamp2 = new Timestamp(2l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'propId')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		Property prop = new Property();
		prop.setPropertyId("propId");
		prop.setPropertyName("pname");
		prop.setPropertyType("pType");
		prop.setValue(DaoUtility.createText("some updated value", "testFormat"));
		prop.setEffectiveDate(timestamp1);
		prop.setExpirationDate(timestamp2);
		prop.setIsActive(true);
		prop.setOwner("me");
		prop.setLanguage("Lang");
		prop.setStatus("testing");
		
		this.ibatisPropertyDao.updateProperty(
				"csguid", 
				"eguid", 
				"pguid", 
				PropertyType.ENTITY, 
				prop);
		
		template.queryForObject("Select * from property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertEquals(rs.getString(5), "pType");
				assertEquals(rs.getString(6), "pname");
				assertEquals(rs.getString(7), "Lang");
				assertEquals(rs.getString(8), "testFormat");
				assertEquals(rs.getString(13), "some updated value");
				assertEquals(rs.getBoolean(14), true);
				assertEquals(rs.getString(15), "me");
				assertEquals(rs.getString(16), "testing");
				assertEquals(timestamp1.getTime(), rs.getTimestamp(17).getTime());
				assertEquals(timestamp2.getTime(), rs.getTimestamp(18).getTime());

				return null;
			}
		});
	}
	
	/**
	 * Insert generic property.
	 */
	@Test
	public void insertGenericProperty(){
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		Property property = new Property();
		property.setPropertyId("pId");
		property.setPropertyName("propName");
		property.setPropertyType("propType");
		property.setLanguage("lang");
		property.setIsActive(true);
		
		property.setOwner("property owner");
		
		property.setStatus("testing");
		Text text = new Text();
		text.setContent("prop value");
		text.setDataType("format");
		property.setValue(text);
		
		property.setEffectiveDate(effectiveDate);
		property.setExpirationDate(expirationDate);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.VERSIONABLE);
		es.setRelativeOrder(23l);
		property.setEntryState(es);
		
		String id = ibatisPropertyDao.insertProperty("fake-cs-id", "fake-entityCode-id", PropertyType.ENTITY, property);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		final String[] keys = (String[])template.queryForObject("Select * from Property", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				String id = rs.getString(1);
				assertEquals(rs.getString(2),"fake-entityCode-id");
				assertEquals(rs.getString(3),"entity");
				assertEquals(rs.getString(4),"pId");
				assertEquals(rs.getString(5),"propType");
				assertEquals(rs.getString(6),"propName");
				assertEquals(rs.getString(7),"lang");
				assertEquals(rs.getString(8),"format");
				assertNull(rs.getString(11));
				assertNull(rs.getString(12));
				assertEquals(rs.getString(13),"prop value");
				assertTrue(rs.getBoolean(14) == true);
				assertTrue(rs.getString(15).equals("property owner"));
				assertTrue(rs.getString(16).equals("testing"));
				assertTrue(rs.getTimestamp(17).equals(effectiveDate));
				assertTrue(rs.getTimestamp(18).equals(expirationDate));
				String entryStateId = rs.getString(19);
				
				String[] keys = new String[]{id, entryStateId};
				return keys;
			}
		});
		
		assertEquals(id,keys[0]);
		
		template.queryForObject("Select * from EntryState", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertEquals(rs.getString(1), keys[1]);
				assertEquals(rs.getString(2), keys[0]);
				assertEquals(rs.getString(3), "Property");
				assertEquals(rs.getString(4), ChangeType.VERSIONABLE.toString());
				assertEquals(rs.getLong(5), 23l);
				
				//TODO: Test with a Revision GUID
				//TODO: Test with a Previous Revision GUID
				//TODO: Test with a Previous EntryState GUID
				
				return null;
			}
		});
	}
	
	/**
	 * Insert generic property.
	 */
	@Test
	public void insertHistoryProperty(){
		
		Property property = new Property();
		property.setPropertyId("pId");
		property.setPropertyName("propName");
		property.setPropertyType("propType");
		property.setLanguage("lang");
		property.setIsActive(true);
		
		property.setOwner("property owner");
		
		property.setStatus("testing");
		Text text = new Text();
		text.setContent("prop value");
		text.setDataType("format");
		property.setValue(text);
		
		ibatisPropertyDao.insertHistoryProperty("fake-cs-id", "fake-entityCode-id", "pguid", PropertyType.ENTITY, property);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		assertEquals(1, template.queryForInt("select count(*) from h_property"));
	}
	
	@Test
	public void insertHistoryPropertyWithQualifier(){
		
		Property property = new Property();
		property.setPropertyId("pId");
		property.setPropertyName("propName");
		property.setPropertyType("propType");
		property.setLanguage("lang");
		property.setIsActive(true);
		
		property.setOwner("property owner");
		
		property.setStatus("testing");
		Text text = new Text();
		text.setContent("prop value");
		text.setDataType("format");
		property.setValue(text);
		
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName("qualName");
		qual.setPropertyQualifierType("qualType");
		qual.setValue(DaoUtility.createText("qualContent"));
		property.addPropertyQualifier(qual);
		
		ibatisPropertyDao.insertHistoryProperty("fake-cs-id", "fake-entityCode-id", "pguid", PropertyType.ENTITY, property);
	
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		assertEquals(1, template.queryForInt("select count(*) from h_property"));
		assertEquals(1, template.queryForInt("select count(*) from h_propertymultiattrib"));
	}
	
	@Test
	@Transactional
	public void testGetHistoryProperty() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation', 'esguid')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
				"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('esguid', 'eguid', 'entity', 'NEW', '0', 'rguid1')");
		
		List<Property> props = ibatisPropertyDao.getAllHistoryPropertiesOfParentByRevisionId("csguid", "eguid", "rguid1", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
	}
	
	@Test
	@Transactional
	public void testGetHistoryPropertyWithQualifier() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation', 'esguid')");
		
		template.execute("Insert into h_propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
				"values ('pmaguid', 'pguid', 'qualifier', 'aqual', 'some_qual', 'esguid')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
				"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('esguid', 'eguid', 'entity', 'NEW', '0', 'rguid1')");
		
		List<Property> props = ibatisPropertyDao.getAllHistoryPropertiesOfParentByRevisionId("csguid", "eguid", "rguid1", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
		
		assertEquals(1, props.get(0).getPropertyQualifierCount());
	}
	
	@Transactional
	@Test
	public void testGetHistoryPropertyWithTwoQualifiers() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation', 'esguid1')");
		
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation', 'esguid2')");

		template.execute("Insert into h_propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
				"values ('pmaguid', 'pguid', 'qualifier', 'aqual', 'some_qual1', 'esguid1')");
		
		template.execute("Insert into h_propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue, entryStateGuid) " +
				"values ('pmaguid', 'pguid', 'qualifier', 'aqual', 'some_qual2', 'esguid2')");

		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
				"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid1')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
				"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid2')");

		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('rguid2', 'rid2', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('esguid1', 'eguid', 'entity', 'NEW', '0', 'rguid1')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('esguid2', 'eguid', 'entity', 'MODIFY', '0', 'rguid2')");

		List<Property> props = ibatisPropertyDao.getAllHistoryPropertiesOfParentByRevisionId("csguid", "eguid", "rguid1", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
		
		assertEquals(1, props.get(0).getPropertyQualifierCount());
	
		assertEquals("some_qual1", props.get(0).getPropertyQualifier(0).getValue().getContent());
	
		props = ibatisPropertyDao.getAllHistoryPropertiesOfParentByRevisionId("csguid", "eguid", "rguid2", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
		
		assertEquals(1, props.get(0).getPropertyQualifierCount());
	
		assertEquals("some_qual2", props.get(0).getPropertyQualifier(0).getValue().getContent());

	}
	
	@Test
	@Transactional
	public void testGetHistoryPropertyWithTwoProperties() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue1', 'presentation', 'esguid1')");
		
		template.execute("Insert into h_property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType, entryStateGuid) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue2', 'presentation', 'esguid2')");
		
		template.execute("Insert into h_entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
				"values ('eguid', 'csguid', 'ecode', 'ens', 'esguid')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
				"values ('rguid2', 'rid2', NOW() )");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('esguid1', 'pguid', 'property', 'NEW', '0', 'rguid1')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
				"values ('esguid2', 'pguid', 'property', 'MODIFY', '0', 'rguid2')");
		
		List<Property> props = ibatisPropertyDao.getAllHistoryPropertiesOfParentByRevisionId("csguid", "eguid", "rguid2", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
		
		assertEquals(props.get(0).getValue().getContent(), "pvalue2");
	}
	
	/**
	 * Insert property qualifier.
	 */
	@Test
	public void insertPropertyQualifier(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");		
		
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName("qualName");
		qual.setPropertyQualifierType("qualType");
		Text text = new Text();
		text.setContent("qual text");
		qual.setValue(text);
		
		ibatisPropertyDao.insertPropertyQualifier("csguid", "pguid", qual);
		
		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "pguid");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_QUALIFIER);
				assertEquals(rs.getString(4), "qualName");
				assertEquals(rs.getString(5), "qual text");
				assertNull(rs.getString(6));
				assertNull(rs.getString(7));
				assertNull(rs.getString(8));
				
				return null;
			}
		});
	}
	
	/**
	 * Insert property source.
	 */
	@Test
	public void insertPropertySource(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");	
		
		Source source = new Source();
		source.setContent("test source");
		source.setSubRef("test subref");
		source.setRole("test role");
		
		ibatisPropertyDao.insertPropertySource("csguid","pguid", source);
		
		
		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "pguid");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_SOURCE);
				assertEquals(rs.getString(4), SQLTableConstants.TBLCOLVAL_SOURCE);
				assertEquals(rs.getString(5), "test source");
				assertEquals(rs.getString(6), "test subref");
				assertEquals(rs.getString(7), "test role");
				assertNull(rs.getString(8));
				
				return null;
			}
		});
	}
	
	/**
	 * Insert property usage context.
	 */
	@Test
	public void insertPropertyUsageContext(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");	
		
		String usageContext = "test usageContext";
		
		ibatisPropertyDao.insertPropertyUsageContext("csguid", "pguid", usageContext);
		
		
		template.queryForObject("Select * from propertymultiattrib", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "pguid");
				assertEquals(rs.getString(3), SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				assertEquals(rs.getString(4), SQLTableConstants.TBLCOLVAL_USAGECONTEXT);
				assertEquals(rs.getString(5), "test usageContext");
				assertNull(rs.getString(6));
				assertNull(rs.getString(7));
				assertNull(rs.getString(8));
				
				return null;
			}
		});
	}
	
	/**
	 * Insert property link.
	 */
	@Test
	public void insertPropertyLink(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
				"values ('pguid1', 'eguid', 'entity', 'pname', 'pvalue', '1')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
		"values ('pguid2', 'eguid', 'entity', 'pname', 'pvalue', '2')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
	
		
		assertEquals(1, template.queryForInt("Select count(*) from codingScheme"));
		assertEquals(1, template.queryForInt("Select count(*) from entity"));
		assertEquals(2, template.queryForInt("Select count(*) from property"));
		
		
		PropertyLink link = new PropertyLink();
		link.setSourceProperty("1");
		link.setPropertyLink("link");
		link.setTargetProperty("2");
		
		ibatisPropertyDao.insertPropertyLink("csgid", "eguid", link);
		
		
		template.queryForObject("Select * from propertylinks", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "pguid1");
				assertEquals(rs.getString(3), "link");
				assertEquals(rs.getString(4), "pguid2");
			
				return null;
			}
		});
	}

	/**
	 * Gets the property by parent.
	 * 
	 * @return the property by parent
	 */
	@Test
	public void getPropertyByParent(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		List<Property> props = ibatisPropertyDao.getAllPropertiesOfParent("csguid", "eguid", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
	}
	
	@Test
	public void getPropertyByParentWithEverything(){
		Timestamp ts1 = new Timestamp(1l);
		Timestamp ts2 = new Timestamp(2l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyId, propertyType, propertyName, language, format, isPreferred, matchIfNoContext, degreeOfFidelity, representationalForm, propertyValue, isActive, owner, status,    effectiveDate,         expirationDate, entryStateGuid) " +
			"values (                             'pguid',      'eguid',       'entity',       'pid',  'presentation', 'pname',      'lang',  'xml',    true,          false,             'dof',             'repForm',       'pvalue',        true,   'me', 'test', '"+ ts1.toString()+"', '"+ ts2.toString() + "',      'esguid')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId,  subRef, role, entryStateGuid) " +
			"values ('pmaguid1', 'pguid', 'source', 'aValue', 'aSubRef', 'aRole', 'esguid')");
	
		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId) " +
			"values ('pmaguid2', 'pguid', 'usageContext', 'usageContext1')");
		
		template.execute("Insert into propertymultiattrib (propMultiAttribGuid, propertyGuid, attributeType, attributeId, attributeValue) " +
			"values ('pmaguid3', 'pguid', 'qualifier', 'aQual', 'aQualValue')");
		
		template.execute("Insert into revision (revisionGuid, revisionId, revAppliedDate) " +
			"values (								'rguid', 	 'rid1',       NOW() )");
		
		template.execute("Insert into revision (revisionGuid, revisionId, revAppliedDate) " +
			"values (								'rguid2', 	 'rid2',       NOW() )");
		
		template.execute("Insert into entrystate (entryStateGuid, entryGuid, entryType, changeType, relativeOrder, revisionGuid, prevRevisionGuid) " +
			"values (								'esguid2', 	   'pguid2', 'property', 'DEPENDENT',     '1',        'rguid',    'rguid2')");
		
		template.execute("Insert into entrystate (entryStateGuid, entryGuid, entryType, changeType, relativeOrder, revisionGuid, prevRevisionGuid, prevEntryStateGuid) " +
			"values (								'esguid', 	   'pguid',  'property', 'MODIFY',     '1',            'rguid',        'rguid2',           'esguid2')");
		
		List<Property> props = ibatisPropertyDao.getAllPropertiesOfParent("csguid", "eguid", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
		
		Presentation prop = (Presentation)props.get(0);
		assertEquals("dof", prop.getDegreeOfFidelity());
		assertEquals("lang", prop.getLanguage());
		assertEquals("me", prop.getOwner());
		assertEquals("pid", prop.getPropertyId());
		assertEquals("pname", prop.getPropertyName());
		assertEquals("presentation", prop.getPropertyType());
		assertEquals("repForm", prop.getRepresentationalForm());
		assertEquals("test", prop.getStatus());
		
		assertEquals(ts2.getTime(), prop.getExpirationDate().getTime());
		assertEquals(ts1.getTime(), prop.getEffectiveDate().getTime());
		assertTrue(prop.getIsActive());
		assertTrue(prop.getIsPreferred());
		assertFalse(prop.getMatchIfNoContext());
		assertEquals("pvalue", prop.getValue().getContent());
		assertEquals("xml", prop.getValue().getDataType());
		
		assertEquals(1, prop.getUsageContextCount());
	    String usageContext = prop.getUsageContext(0);
	    assertEquals("usageContext1", usageContext);
		
		assertEquals(1, prop.getPropertyQualifierCount());
		PropertyQualifier qual = prop.getPropertyQualifier(0);
		assertEquals("aQual", qual.getPropertyQualifierName());
		assertEquals("aQualValue", qual.getValue().getContent());
		
		assertEquals(1, prop.getSourceCount());
		Source source = prop.getSource(0);
		assertEquals("aSubRef", source.getSubRef());
		assertEquals("aRole", source.getRole());
		assertEquals("aValue", source.getContent());
		
		EntryState es = prop.getEntryState();
		assertNotNull(es);
		assertEquals("rid1", es.getContainingRevision());
		assertEquals("rid2", es.getPrevRevision());
		assertEquals(ChangeType.MODIFY, es.getChangeType());
		assertTrue(1l == es.getRelativeOrder());
		
	}
	
	/**
	 * Gets the propert id.
	 * 
	 * @return the propert id
	 */
	@Test
	public void getPropertId(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyId) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'id1')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		String id = ibatisPropertyDao.getPropertyIdFromParentIdAndPropId("csguid", "eguid", "id1");
		
		assertEquals("pguid", id);
	}
	
	/**
	 * Gets the presentation property by parent.
	 * 
	 * @return the presentation property by parent
	 */
	@Test
	public void getPresentationPropertyByParent(){
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		List<Property> props = ibatisPropertyDao.getAllPropertiesOfParent("csguid", "eguid", PropertyType.ENTITY);
		
		assertEquals(1, props.size());
		
		assertTrue(props.get(0) instanceof Presentation);
	}

	/**
	 * Delete all entity properties of coding scheme.
	 */
	@Test
	public void deleteAllEntityPropertiesOfCodingScheme(){

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 'ecode', 'ens')");
		
		assertEquals(1, template.queryForInt("Select count(*) from codingScheme"));
		assertEquals(1, template.queryForInt("Select count(*) from entity"));
		assertEquals(1, template.queryForInt("Select count(*) from property"));
		
		ibatisPropertyDao.deleteAllEntityPropertiesOfCodingScheme("csguid");
		
		assertEquals(0, template.queryForInt("Select count(*) from property"));

	}
}
