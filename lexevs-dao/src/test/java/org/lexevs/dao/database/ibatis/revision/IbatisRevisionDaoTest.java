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
package org.lexevs.dao.database.ibatis.revision;

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
public class IbatisRevisionDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis property dao. */
	@Autowired
	private IbatisRevisionDao ibatisRevisionDao;
	
	@Test
	public void getRevisionIdForFirstDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r1",this.ibatisRevisionDao.getRevisionIdForDate(date1));
		
	}
	
	@Test
	public void getRevisionIdForSecondDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r2",this.ibatisRevisionDao.getRevisionIdForDate(date2));
		
	}
	
	@Test
	public void getRevisionIdForThirdDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r3",this.ibatisRevisionDao.getRevisionIdForDate(date3));
		
	}
	
	@Test
	public void getRevisionIdForAfterDate(){
		final Timestamp date1 = new Timestamp(1l);
		final Timestamp date2 = new Timestamp(2l);
		final Timestamp date3 = new Timestamp(3l);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('1', 'r1', '" + date1.toString() + "')");
		
		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('2', 'r2', '" + date2.toString() + "')");

		template.execute("Insert into revision (revisionGuid,  revisionId, revAppliedDate) " +
				"values ('3', 'r3', '" + date3.toString() + "')");
		
		
		assertEquals("r3",this.ibatisRevisionDao.getRevisionIdForDate(new Timestamp(4l)));
		
	}
}