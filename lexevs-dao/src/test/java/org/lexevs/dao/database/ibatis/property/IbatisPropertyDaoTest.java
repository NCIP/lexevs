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
import org.lexevs.dao.database.ibatis.entity.IbatisEntityDao;
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
	
	/** The ibatis entity dao. */
	@Autowired
	private IbatisEntityDao ibatisEntityDao;

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
