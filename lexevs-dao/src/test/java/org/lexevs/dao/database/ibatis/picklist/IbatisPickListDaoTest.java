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
package org.lexevs.dao.database.ibatis.picklist;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.valueSets.PickListDefinition;
import org.junit.Test;
import org.lexevs.dao.database.ibatis.valuesets.IbatisPickListDao;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisPickListDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional
public class IbatisPickListDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis pick list dao. */
	@Autowired
	private IbatisPickListDao ibatisPickListDao;
	
	/**
	 * Test pick list dao up.
	 */
	@Test
	public void testPickListDaoUp() {
		assertNotNull(this.ibatisPickListDao);
	}
	
	/**
	 * Test insert pick list definition.
	 */
	@Test
	public void testInsertPickListDefinition() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		PickListDefinition def = new PickListDefinition();
		def.setCompleteSet(false);
		def.setDefaultEntityCodeNamespace("ns");
		def.addDefaultPickContext("context");
		def.setDefaultSortOrder("asc");
		def.setEffectiveDate(effectiveDate);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("description");
		def.setEntityDescription(ed);
		def.setExpirationDate(expirationDate);
		def.setIsActive(true);
		def.setRepresentsValueSetDefinition("vd");
		def.setOwner("owner");
		def.setPickListId("plid");
		
		Source source = new Source();
		source.setContent("source");
		def.addSource(source);
		def.setStatus("testing");
		
		ibatisPickListDao.insertPickListDefinition("releaseuri", def);
		
		
		int count = template.queryForInt("Select count(*) from vdpicklist");
		assertEquals(1, count);
		
		template.queryForObject("Select * from vdpicklist", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				assertNotNull(rs.getString(1));
				assertEquals("plid", rs.getString(2));
				
				return null;
			}		
		});
	}
	
	/**
	 * Test get pick list ids.
	 */
	@Test
	public void testGetPickListIds() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl1', 'id1', 'vd')");
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl2', 'id2', 'vd')");
		
		List<String> ids = this.ibatisPickListDao.getPickListIds();
		
		assertEquals(2, ids.size());
		assertTrue(ids.contains("id1"));
		assertTrue(ids.contains("id2"));
	}
	
	/**
	 * Test get pick list definition by id.
	 */
	@Test
	public void testGetPickListDefinitionById() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl1', 'id1', 'vd')");
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl2', 'id2', 'vd')");
		
		PickListDefinition definition = this.ibatisPickListDao.getPickListDefinitionById("id1");
		
		assertEquals("id1", definition.getPickListId());
	}
	
	/**
	 * Test get guid from pick list id.
	 */
	@Test
	public void testGetGuidFromPickListId() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("insert into systemrelease (releaseGuid, releaseURI, releaseDate) values ('123', 'releaseuri', NOW())");
		
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl1', 'id1', 'vd')");
		template.execute("insert into vdPickList (vdPickListGuid, pickListId, representsvaluedomain) values ('pl2', 'id2', 'vd')");
		
		String guid = this.ibatisPickListDao.getPickListGuidFromPickListId("id2");
		
		assertEquals("pl2", guid);
	}
}
