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
package org.lexevs.dao.database.ibatis.codingscheme;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class IbatisCodingSchemeDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Transactional
public class IbatisCodingSchemeDaoHistoryTest extends LexEvsDbUnitTestBase {

	/** The ibatis coding scheme dao. */
	@Resource
	private IbatisCodingSchemeDao ibatisCodingSchemeDao;

	/**
	 * Test insert coding scheme.
	 * 
	 * @throws SQLException the SQL exception
	 */
	@Test
	@Transactional
	public void testInsertHistoryCodingScheme() throws SQLException{
	CodingScheme cs = new CodingScheme();
		
		final Timestamp effectiveDate = new Timestamp(1l);
		final Timestamp expirationDate = new Timestamp(2l);
		
		cs.setCodingSchemeName("csName");
		cs.setCodingSchemeURI("uri");
		cs.setRepresentsVersion("1.2");
		cs.setFormalName("csFormalName");
		cs.setDefaultLanguage("lang");
		cs.setApproxNumConcepts(22l);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("cs Description");
		cs.setEntityDescription(ed);
		
		Text copyright = new Text();
		copyright.setContent("cs Copyright");
		cs.setCopyright(copyright);
		
		cs.setIsActive(false);
		
		cs.setOwner("cs owner");
		
		cs.setStatus("testing");
		
		cs.setEffectiveDate(effectiveDate);
		cs.setExpirationDate(expirationDate);
		
		cs.addLocalName("testHistoryLocalName");
		
		Source source = new Source();
		source.setContent("someSource");
		cs.addSource(source);

		EntryState es = new EntryState();
		es.setChangeType(ChangeType.REMOVE);
		es.setRelativeOrder(22l);
		cs.setEntryState(es);
		
		ibatisCodingSchemeDao.insertHistoryCodingScheme("csguid", cs);

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		int csCount = template.queryForInt("Select count(*) from h_codingScheme");
		assertEquals(1, csCount);
		
		int csMultiAttribCount = template.queryForInt("Select count(*) from h_csMultiAttrib");
		assertEquals(2, csMultiAttribCount);
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithHistoryFromHistoryWithMultiple() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid1', 'csguid', 'cs', 'NEW', 'rguid1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rguid2");
		
		assertEquals("rid2", cs.getEntryState().getContainingRevision());	
		assertEquals("csversion2", cs.getRepresentsVersion());	
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithLocalName() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid2')");
		
		template.execute("Insert into h_csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('csmaguid', 'csguid', 'LocalName', 'local name', 'esguid2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid1', 'csguid', 'cs', 'NEW', 'rguid1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rguid2");
		
		assertEquals(1, cs.getLocalNameCount());
		assertEquals("local name", cs.getLocalName(0));
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithSource() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid2')");
		
		template.execute("Insert into h_csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('csmaguid', 'csguid', 'Source', 'a source', 'esguid2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid1', 'csguid', 'cs', 'NEW', 'rguid1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rguid2");
		
		assertEquals(1, cs.getSourceCount());
		assertEquals("a source", cs.getSource(0).getContent());
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithHistoryFromHistory() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid1', 'csguid', 'cs', 'NEW', 'rguid1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rguid2");
		
		assertEquals("rid2", cs.getEntryState().getContainingRevision());
		
	}
}
