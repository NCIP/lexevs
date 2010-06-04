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
package org.lexevs.dao.database.ibatis.ncihistory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.LexGrid.versions.SystemRelease;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisEntityDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional
public class IbatisNciHistoryDaoTest extends LexEvsDbUnitTestBase {

	/** The ibatis entity dao. */
	@Autowired
	private IbatisNciHistoryDao ibatisNciHistoryDao;
	
	@Test
	@Transactional
	public void testGetBaseLinesNullDates() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', 'csguid', 'releaseuri', 'releaseid',  ' " + date + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
	
		List<SystemRelease> systemReleases = ibatisNciHistoryDao.getBaseLines("csguid", null, null);
		
		assertEquals(1,systemReleases.size());
		
		SystemRelease systemRelease = systemReleases.get(0);
		
		assertEquals("basedonsomerelease", systemRelease.getBasedOnRelease() );
		assertEquals("testsystemrelease", systemRelease.getEntityDescription().getContent() );
		assertEquals("testagency",  systemRelease.getReleaseAgency() );
		assertEquals(date, new Timestamp(systemRelease.getReleaseDate().getTime()));
		assertEquals("releaseid", systemRelease.getReleaseId() );
		assertEquals("releaseuri", systemRelease.getReleaseURI() );
	}
	
	@Test
	@Transactional
	public void testGetSystemReleaseForUri() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', 'csguid', 'releaseuri', 'releaseid',  ' " + date + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
	
		SystemRelease systemRelease = ibatisNciHistoryDao.getSystemReleaseForReleaseUri("csguid", "releaseuri");

		assertEquals("basedonsomerelease", systemRelease.getBasedOnRelease() );
		assertEquals("testsystemrelease", systemRelease.getEntityDescription().getContent() );
		assertEquals("testagency",  systemRelease.getReleaseAgency() );
		assertEquals(date, new Timestamp(systemRelease.getReleaseDate().getTime()));
		assertEquals("releaseid", systemRelease.getReleaseId() );
		assertEquals("releaseuri", systemRelease.getReleaseURI() );
	}
	
	
	@Test
	@Transactional
	public void testGetBaseLinesNullDatesWithTwoResults() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date = new Timestamp(new Date().getTime());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('csguid', 'csname', 'csuri', 'csversion')");
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', 'csguid', 'releaseuri', 'releaseid',  ' " + date + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', 'csguid', 'releaseuri', 'releaseid2',  ' " + date + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		List<SystemRelease> systemReleases = ibatisNciHistoryDao.getBaseLines("csguid", null, null);
		
		assertEquals(2,systemReleases.size());
	}
	
	@Test
	@Transactional
	public void testGetEarliestBaseline() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(new Date().getTime());
		Thread.sleep(1000);
		Timestamp date2 = new Timestamp(new Date().getTime());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('csguid', 'csname', 'csuri', 'csversion')");
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', 'csguid', 'releaseuri', 'releaseid',  ' " + date1 + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', 'csguid', 'releaseuri', 'releaseid2',  ' " + date2 + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		SystemRelease systemRelease = ibatisNciHistoryDao.getEarliestBaseLine("csguid");
		
		assertEquals("releaseid" ,systemRelease.getReleaseId());
	}
	
	@Test
	@Transactional
	public void testGetLatestBaseline() throws InterruptedException {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		Timestamp date1 = new Timestamp(new Date().getTime());
		Thread.sleep(1000);
		Timestamp date2 = new Timestamp(new Date().getTime());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('csguid', 'csname', 'csuri', 'csversion')");
	
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('123', 'csguid', 'releaseuri', 'releaseid',  ' " + date1 + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease')");
		
		template.execute("insert into nciHistSystemRelease (releaseGuid, codingSchemeGuid, releaseURI, releaseId, releaseDate, basedOnRelease, releaseAgency, description)" +
				" values ('1234', 'csguid', 'releaseuri', 'releaseid2',  ' " + date2 + "' , 'basedonsomerelease', 'testagency', 'testsystemrelease2')");
	
		SystemRelease systemRelease = ibatisNciHistoryDao.getLatestBaseLine("csguid");
		
		assertEquals("releaseid2" ,systemRelease.getReleaseId());
	}
}
