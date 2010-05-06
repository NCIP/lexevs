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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.junit.Test;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.CodeNamespacePair;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.utility.DaoUtility;
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
	
		List<String> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubject(
					"cs-guid", 
					"ap-guid", 
					"s-code", 
					"s-ns", 
					null,
					null,
					null,
					0, 
					-1);
		
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
	
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", "ap-guid", "s-code", "s-ns", null, null, null);
		
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
	
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingObjectCount(
					"cs-guid", "ap-guid", "t-code1", "t-ns1", null, null, null);
		
		assertEquals(1, uids);
	}
	
	@Test
	public void testAssociatedConceptsSourceOf() throws SQLException{
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
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid2'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
	
		List<AssociatedConcept> associatedConcepts = 
			ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
					"cs-guid", 
					DaoUtility.createNonTypedList("eae-guid1", "eae-guid2"),
					TripleNode.SUBJECT);
		
		assertEquals(2,associatedConcepts.size());
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
	
	
		List<AssociatedConcept> associatedConcepts = ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
				"cs-guid", 
				DaoUtility.createNonTypedList("eae-guid1"), TripleNode.OBJECT);
		
		assertEquals(1,associatedConcepts.size());
		AssociatedConcept associatedConcept = associatedConcepts.get(0);
		
		assertEquals("t-code",associatedConcept.getCode());
		assertEquals("t-ns",associatedConcept.getCodeNamespace());
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithQualifierName() throws SQLException{
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
	
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'eae-quals-guid', " +
				"'eae-guid1'," +
				"'qualName'," +
				"'qualValue'," +
				"null )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("qualName", null));
		
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", "ap-guid", "s-code", "s-ns", null, list, null);
		
		assertEquals(1, uids);
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithBadQualifierName() throws SQLException{
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
	
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'eae-quals-guid', " +
				"'eae-guid1'," +
				"'qualName'," +
				"'qualValue'," +
				"null )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("BAD_qualName", null));
		
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", "ap-guid", "s-code", "s-ns", null, list, null);
		
		assertEquals(0, uids);
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithQualifierNameAndValue() throws SQLException{
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
	
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'eae-quals-guid', " +
				"'eae-guid1'," +
				"'qualName'," +
				"'qualValue'," +
				"null )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("qualName", "qualValue"));
		
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", "ap-guid", "s-code", "s-ns", null, list, null);
		
		assertEquals(1, uids);
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithQualifierNameAndBadValue() throws SQLException{
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
	
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'eae-quals-guid', " +
				"'eae-guid1'," +
				"'qualName'," +
				"'qualValue'," +
				"null )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("qualName", "BAD_qualValue"));
		
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", "ap-guid", "s-code", "s-ns", null, list, null);
		
		assertEquals(0, uids);
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithRestrictToCode() throws SQLException{
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
	
		template.execute("insert into " +
				"entityassnquals values ( " +
				"'eae-quals-guid', " +
				"'eae-guid1'," +
				"'qualName'," +
				"'qualValue'," +
				"null )");
		
		CodeNamespacePair pair = new CodeNamespacePair("t-code2", "t-ns2");
	
		int uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", "ap-guid", "s-code", "s-ns", null, null, DaoUtility.createNonTypedList(pair));
		
		assertEquals(1, uids);
	}
	
	@Test
	public void testGetRootsWithNoAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", null);

		assertEquals(1, uids.size());

		assertEquals("s-code", uids.get(0).getCode());
	}
	
	@Test
	public void testGetRootsWithAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", DaoUtility.createNonTypedList("ap-guid"));

		assertEquals(1, uids.size());

		assertEquals("s-code", uids.get(0).getCode());
	}
	
	@Test
	public void testGetRootsWithWrongAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", DaoUtility.createNonTypedList("INVALID"));

		assertEquals(0, uids.size());
	}
	
	@Test
	public void testGetRootsWithOneWrongOneRightAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", DaoUtility.createNonTypedList("INVALID", "ap-guid"));

		assertEquals(1, uids.size());

		assertEquals("s-code", uids.get(0).getCode());
	}
	
	@Test
	public void testGetTailsWithNoAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", null);

		assertEquals(1, uids.size());

		assertEquals("t-code2", uids.get(0).getCode());
	}
	
	@Test
	public void testGetTailsWithAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", DaoUtility.createNonTypedList("ap-guid"));

		assertEquals(1, uids.size());

		assertEquals("t-code2", uids.get(0).getCode());
	}
	
	@Test
	public void testGetTailsWithWrongAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", DaoUtility.createNonTypedList("INVALID"));

		assertEquals(0, uids.size());
	}
	
	@Test
	public void testGetTailsWithOneWrongOneRightAssociationRestriction() throws SQLException{
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
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", DaoUtility.createNonTypedList("INVALID", "ap-guid"));

		assertEquals(1, uids.size());

		assertEquals("t-code2", uids.get(0).getCode());
	}
}
