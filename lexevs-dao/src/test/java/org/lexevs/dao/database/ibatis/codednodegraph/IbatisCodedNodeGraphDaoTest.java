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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.junit.Test;
import org.lexevs.dao.database.access.codednodegraph.CodedNodeGraphDao.TripleNode;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.model.CountConceptReference;
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
	public void testListCodeRelationshipsNoTransitive() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, null, null, false);
			
		
		assertEquals(1, rels.size());
		assertTrue(rels.contains("ap-guid"));
	}
	
	@Test
	public void testListCodeRelationshipsEntityTypeRestriction() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
	
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1')");

		template.execute("Insert into entitytype (entityGuid, entityType) " +
		"values ('eguid2', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, DaoUtility.createNonTypedList("concept"), null, true);
			
		
		assertEquals(1, rels.size());
		assertTrue(rels.contains("ap-guid"));
	}

	@Test
	public void testListCodeRelationshipsEntityTypeRestrictionOneWrong() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
	
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'definition')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1')");

		template.execute("Insert into entitytype (entityGuid, entityType) " +
		"values ('eguid2', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, DaoUtility.createNonTypedList("concept"), null, true);
			
		
		assertEquals(0, rels.size());
	}
	
	@Test
	public void testListCodeRelationshipsEntityTypeRestrictionBothWrong() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
	
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'definition')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1')");

		template.execute("Insert into entitytype (entityGuid, entityType) " +
		"values ('eguid2', 'instance')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, DaoUtility.createNonTypedList("concept"), null, true);
			
		
		assertEquals(0, rels.size());
	}
	
	@Test
	public void testListCodeRelationshipsEntityTypeRestrictionTwoTypes() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
	
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'definition')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1')");

		template.execute("Insert into entitytype (entityGuid, entityType) " +
		"values ('eguid2', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, DaoUtility.createNonTypedList("concept", "definition"), null, true);
			
		
		assertEquals(1, rels.size());
	}
	
	@Test
	public void testListCodeRelationshipsAnonymousRestrictionFalse() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns', false)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1', false)");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, null, false, true);
			
		assertEquals(1, rels.size());
	}
	
	@Test
	public void testListCodeRelationshipsAnonymousRestrictionTrue() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns', true)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1', true)");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, null, true, true);
			
		assertEquals(1, rels.size());
	}
	
	@Test
	public void testListCodeRelationshipsAnonymousRestrictionOneWrong() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns', true)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
		"values ('eguid2', 'cs-guid', 't-code1', 't-ns1', false)");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, null, true, true);
			
		assertEquals(0, rels.size());
	}
	
	@Test
	public void testListCodeRelationshipsWithTransitive() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid2', 'rel-guid', 'apname2')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentitytr" +
				" values ('eaetr-guid1'," +
				" 'ap-guid2'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1')");
		
		List<String> rels = ibatisCodedNodeGraphDao.
			listCodeRelationships("cs-guid", null, "s-code", "s-ns", "t-code1", "t-ns1", null, null, null, null, null, null, null, null, true);
			
		assertEquals(2, rels.size());
		assertTrue(rels.contains("ap-guid"));
		assertTrue(rels.contains("ap-guid2"));
	}

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
	
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, null, null, null, null, null);

		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(2), uids.get("apname"));
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
	
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingObjectCount(
					"cs-guid", null, "t-code1", "t-ns1", null, null, null, null, null, null);
		
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
	
	
		List<? extends AssociatedConcept> associatedConcepts = 
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
	
		List<? extends AssociatedConcept> associatedConcepts = ibatisCodedNodeGraphDao.getAssociatedConceptsFromUid(
				"cs-guid", 
				DaoUtility.createNonTypedList("eae-guid1"), TripleNode.OBJECT);
		
		assertEquals(1,associatedConcepts.size());
		AssociatedConcept associatedConcept = associatedConcepts.get(0);
		
		assertEquals("t-code",associatedConcept.getCode());
		assertEquals("t-ns",associatedConcept.getCodeNamespace());
	}

	@Test
	public void testGetTripleUidsContainingSubjectCountWithAnonymousRestrictionWithNone() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		Map<String,Integer> uids = 
			ibatisCodedNodeGraphDao.
				getTripleUidsContainingSubjectCount("cs-guid", null, "s-code", "s-ns", null, null, null, null, null, false);
	
		assertTrue(uids.isEmpty());
	}

	@Test
	public void testGetTripleUidsContainingSubjectCountWithAnonymousRestrictionOnlyAnonymous() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns', true)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
			"values ('eguid2', 'cs-guid', 't-code', 't-ns', true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		Map<String,Integer> uids = 
			ibatisCodedNodeGraphDao.
				getTripleUidsContainingSubjectCount("cs-guid", null, "s-code", "s-ns", null, null, null, null, null, true);
	
		assertEquals(1,uids.size());
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithAnonymousRestrictionOnlyNonAnonymous() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isAnonymous) " +
			"values ('eguid2', 'cs-guid', 't-code', 't-ns', false)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid2', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		Map<String,Integer> uids = 
			ibatisCodedNodeGraphDao.
				getTripleUidsContainingSubjectCount("cs-guid", null, "s-code", "s-ns", null, null, null, null, null, false);
	
		assertEquals(1,uids.size());
	}

	@Test
	public void testGetTripleUidsContainingSubjectCountWithEntityTypeRestriction() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 't-code', 't-ns')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		Map<String,Integer> uids = 
			ibatisCodedNodeGraphDao.
				getTripleUidsContainingSubjectCount("cs-guid", null, "s-code", "s-ns", null, null, null, null, DaoUtility.createNonTypedList("concept"), null);
	
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithWrongEntityTypeRestriction() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		Map<String,Integer> uids = 
			ibatisCodedNodeGraphDao.
				getTripleUidsContainingSubjectCount("cs-guid", null, "s-code", "s-ns", null, null, null, null, DaoUtility.createNonTypedList("WRONG_ENTITY_TYPE"), null);
	
		assertTrue(uids.isEmpty());
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithMultipleEntityTypeRestriction() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 't-code', 't-ns')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'some_other_type')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		Map<String,Integer> uids = 
			ibatisCodedNodeGraphDao.
				getTripleUidsContainingSubjectCount("cs-guid", null, "s-code", "s-ns", null, null, null, null, DaoUtility.createNonTypedList("some_other_type"), null);
	
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
				"'es' )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("qualName", null));
		
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, list, null, null, null, null);
		
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
				"'es' )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("BAD_qualName", null));
		
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, list, null, null, null, null);
		
		assertEquals(0, uids.keySet().size());
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
				"'es'  )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("qualName", "qualValue"));
		
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, list, null, null, null, null);
		
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
				"'es' )");
		List<QualifierNameValuePair> list = new ArrayList<QualifierNameValuePair>();
		list.add(new QualifierNameValuePair("qualName", "BAD_qualValue"));
		
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, list, null, null, null, null);
		
		assertEquals(0, uids.keySet().size());
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
				"'es' )");
		
		CodeNamespacePair pair = new CodeNamespacePair("t-code2", "t-ns2");
	
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, null, DaoUtility.createNonTypedList(pair), null, null, null);
		
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectCountWithRestrictToCodesystem() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
	
	
		Map<String,Integer> uids = ibatisCodedNodeGraphDao.
			getTripleUidsContainingSubjectCount(
					"cs-guid", null, "s-code", "s-ns", null, null, null, DaoUtility.createNonTypedList("t-ns2"), null, null);
		
		assertEquals(1, uids.keySet().size());
		assertEquals(new Integer(1), uids.get("apname"));
	}
	
	@Test
	public void testGetTripleUidsContainingSubjectWithRestrictToCodesystem() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
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
					"cs-guid", "ap-guid", "s-code", "s-ns", null, null, null, DaoUtility.createNonTypedList("t-ns2"), null, null, 0, -1);
		
		assertEquals(1, uids.size());
		assertEquals("eae-guid2", uids.get(0));
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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", null, TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", DaoUtility.createNonTypedList("ap-guid"), TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.
		getRootNodes("cs-guid", DaoUtility.createNonTypedList("INVALID"),
				TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getRootNodes("cs-guid", 
				DaoUtility.createNonTypedList("INVALID", "ap-guid"), TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", null, TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", DaoUtility.createNonTypedList("ap-guid"), TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", DaoUtility.createNonTypedList("INVALID"), TraverseAssociations.INDIVIDUALLY);

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
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");

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


		List<ConceptReference> uids = ibatisCodedNodeGraphDao.getTailNodes("cs-guid", DaoUtility.createNonTypedList("INVALID", "ap-guid"), TraverseAssociations.INDIVIDUALLY);

		assertEquals(1, uids.size());

		assertEquals("t-code2", uids.get(0).getCode());
	}
	
	@Test
	public void testGetCountConceptReferencesContainingSubject() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		ConceptReference ref = new ConceptReference();
		ref.setCode("s-code");
		ref.setCodeNamespace("s-ns");
		
		List<ConceptReference> codeList = Arrays.asList(ref);
		
		List<CountConceptReference> refs = 
			ibatisCodedNodeGraphDao.
				getCountConceptReferencesContainingSubject("cs-guid", null, codeList, null, null, null, null, null, null);
	
		assertEquals(1,refs.size());
		
		assertEquals("s-code", refs.get(0).getCode());
		assertEquals("s-ns", refs.get(0).getCodeNamespace());
	}
	
	@Test
	public void testGetCountConceptReferencesContainingObject() throws SQLException{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid1', 'cs-guid', 's-code', 's-ns')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('eguid1', 'concept')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code'," +
				" 't-ns'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
	
		ConceptReference ref = new ConceptReference();
		ref.setCode("t-code");
		ref.setCodeNamespace("t-ns");
		
		List<ConceptReference> codeList = Arrays.asList(ref);
		
		List<CountConceptReference> refs = 
			ibatisCodedNodeGraphDao.
				getCountConceptReferencesContainingObject("cs-guid", null, codeList, null, null, null, null, null, null);
	
		assertEquals(1,refs.size());
		
		assertEquals("t-code", refs.get(0).getCode());
		assertEquals("t-ns", refs.get(0).getCodeNamespace());
	}
}
