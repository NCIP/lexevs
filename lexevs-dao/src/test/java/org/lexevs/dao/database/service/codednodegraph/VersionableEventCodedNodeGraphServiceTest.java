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
package org.lexevs.dao.database.service.codednodegraph;

import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.junit.Test;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The Class VersionableEventCodingSchemeServiceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventCodedNodeGraphServiceTest extends LexEvsDbUnitTestBase {

	/** The service. */
	@Resource
	private VersionableEventCodedNodeGraphService service;

	@Resource
	private Registry registry;
	
	@Test
	public void testListCodeRelationshipsWithTransitive() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

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
				" 't-ns1', " +
				" null)");
		
		List<String> rels = service.
			listCodeRelationships("csuri", "csversion", null, "s-code", "s-ns", "t-code1", "t-ns1", new GraphQuery(), true);
			
		assertEquals(2, rels.size());
		assertTrue(rels.contains("apname"));
		assertTrue(rels.contains("apname2"));
	}
	
	@Test
	public void testListCodeRelationshipsWithTransitiveWithContainerName() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

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
				" 't-ns1', " +
				" null)");
		
		List<String> rels = service.
			listCodeRelationships("csuri", "csversion", "c-name", "s-code", "s-ns", "t-code1", "t-ns1", new GraphQuery(), true);
			
		assertEquals(2, rels.size());
		assertTrue(rels.contains("apname"));
		assertTrue(rels.contains("apname2"));
	}
	
	@Test
	public void testListCodeRelationshipsWithTransitiveWithWrongContainerName() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

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
				" 't-ns1', " +
				" null");
		
		List<String> rels = service.
			listCodeRelationships("csuri", "csversion", "INVALID_CONTAINER_NAME", "s-code", "s-ns", "t-code1", "t-ns1", new GraphQuery(), true);
			
		assertEquals(0, rels.size());
	}
	
	@Test
	public void testGetAssociatedConceptResolveTrue() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'csguid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 's-code', 's-ns')");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource("csuri", "csversion", true, null, null, "eae-guid");
		
		assertNotNull(associatedConcept.getEntity());
	}
	
	@Test
	public void testGetAssociatedConceptResolveFalse() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('pguid', 'eguid', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'csguid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 's-code', 's-ns')");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource("csuri", "csversion", false, null, null, "eae-guid");
		
		assertNull(associatedConcept.getEntity());
	}
	
	@Test
	public void testGetAssociatedConceptResolveWithPropertyTypeRestriction() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('pguid1', 'eguid', 'entity', 'pid1', 'pvalue1', 'presentation')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('pguid2', 'eguid', 'entity', 'pid2', 'pvalue2', 'definition')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'csguid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 's-code', 's-ns')");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource(
					"csuri", 
					"csversion", 
					true, 
					null, 
					new PropertyType[] {PropertyType.DEFINITION}, 
					"eae-guid");
		
		assertEquals(1,associatedConcept.getEntity().getDefinitionCount());
		assertEquals(0,associatedConcept.getEntity().getPresentationCount());
		assertEquals(1,associatedConcept.getEntity().getAllProperties().length);
	}
	
	@Test
	public void testGetAssociatedConceptResolveWithPropertyNameRestriction() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('pguid1', 'eguid', 'entity', 'pid1', 'pname1', 'presentation')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('pguid2', 'eguid', 'entity', 'pname2', 'pvalue', 'definition')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('csguid', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('rel-guid', 'csguid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('ap-guid', 'rel-guid', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('eguid', 'csguid', 's-code', 's-ns')");
		
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry("pname2");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource(
					"csuri", 
					"csversion", 
					true, 
					lnl, 
					null, 
					"eae-guid");
		
		assertEquals(1,associatedConcept.getEntity().getDefinitionCount());
		assertEquals(0,associatedConcept.getEntity().getPresentationCount());
		assertEquals(1,associatedConcept.getEntity().getAllProperties().length);
	}
}
