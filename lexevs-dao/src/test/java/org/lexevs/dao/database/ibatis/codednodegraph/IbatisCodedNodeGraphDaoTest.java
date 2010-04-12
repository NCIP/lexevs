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
package org.lexevs.dao.database.ibatis.codednodegraph;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.junit.Test;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * The Class IbatisAssociationDaoTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@TransactionConfiguration
public class IbatisCodedNodeGraphDaoTest extends LexEvsDbUnitTestBase {
	
	/** The ibatis association dao. */
	@Resource
	private IbatisCodedNodeGraphDao ibatisCodedNodeGraphDao;
	
	@Test
	public void testGetTripleUids() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid) values " +
		"('ap-guid', 'rel-guid')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid2'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		List<String> uids = ibatisCodedNodeGraphDao.getTripleUidsContainingSubject("cs-guid", "ap-guid", "s-code", "s-ns", 0, -1);
		
		assertEquals(2, uids.size());
		assertTrue(uids.contains("eae-guid1"));
		assertTrue(uids.contains("eae-guid2"));
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCount() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid) values " +
		"('ap-guid', 'rel-guid')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid2'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		int uids = ibatisCodedNodeGraphDao.getTripleUidsContainingSubjectCount("cs-guid", "ap-guid", "s-code", "s-ns");
		
		assertEquals(2, uids);
	}
	
	@Test
	public void testGetTripleUidsContainingObjectCount() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid) values " +
		"('ap-guid', 'rel-guid')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid2'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		int uids = ibatisCodedNodeGraphDao.getTripleUidsContainingObjectCount("cs-guid", "ap-guid", "t-code1", "t-ns1");
		
		assertEquals(1, uids);
	}
	
	@Test
	public void testAssociatedConceptSourceOf() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'cs-guid', 's-code', 's-ns')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid) values " +
		"('ap-guid', 'rel-guid')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
	
		AssociatedConcept associatedConcept = ibatisCodedNodeGraphDao.getAssociatedConceptFromUid("cs-guid", "eae-guid1", TripleNode.SUBJECT);
		
		assertEquals("s-code",associatedConcept.getCode());
		assertEquals("s-ns",associatedConcept.getCodeNamespace());
	}
	
	@Test
	public void testAssociatedConceptTargetOf() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('eguid2', 'cs-guid', 't-code', 't-ns')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid) values " +
		"('ap-guid', 'rel-guid')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
	
		AssociatedConcept associatedConcept = ibatisCodedNodeGraphDao.getAssociatedConceptFromUid("cs-guid", "eae-guid1", TripleNode.OBJECT);
		
		assertEquals("t-code",associatedConcept.getCode());
		assertEquals("t-ns",associatedConcept.getCodeNamespace());
	}
}
