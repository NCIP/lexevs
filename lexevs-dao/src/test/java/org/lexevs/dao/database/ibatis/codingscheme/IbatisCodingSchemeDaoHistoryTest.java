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

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
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
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithHistoryFromHistoryWithMultiple() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
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
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid,  relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rid2");
		
		assertEquals("csversion2", cs.getRepresentsVersion());	
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithLocalName() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('csmaguid', 'csguid', 'localName', 'local name', 'esguid2')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid2')");
		
		template.execute("Insert into h_csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('csmaguid', 'csguid', 'localName', 'local name', 'esguid2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid1', 'csguid', 'cs', 'NEW', 'rguid1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rid2");
		
		assertEquals("local name", cs.getLocalName(0));
	}
	
	@Test
	public void getHistoryCodingSchemeByRevisionWithSource() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('csmaguid', 'csguid', 'source', 'a source', 'esguid2')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion1', 'esguid1')");
		
		template.execute("Insert into h_codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entrystateguid) " +
			"values ('csguid', 'csname', 'csuri', 'csversion2', 'esguid2')");
		
		template.execute("Insert into h_csmultiattrib (csMultiAttribGuid, codingSchemeGuid, attributetype, attributevalue, entrystateguid) " +
			"values ('csmaguid', 'csguid', 'source', 'a source', 'esguid2')");
	
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid1', 'rid1', NOW() )");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('rguid2', 'rid2', NOW() )");
	
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid1', 'csguid', 'cs', 'NEW', 'rguid1', '0')");
		
		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, revisionguid, relativeorder) " +
			"values ('esguid2', 'csguid', 'cs', 'MODIFY', 'rguid2', '0')");
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rid2");
		
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
		
		CodingScheme cs = ibatisCodingSchemeDao.getHistoryCodingSchemeByRevision("csguid", "rid2");
		
		assertNotNull(cs);
		
	}
}
