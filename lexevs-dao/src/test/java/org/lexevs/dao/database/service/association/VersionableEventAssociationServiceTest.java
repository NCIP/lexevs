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
package org.lexevs.dao.database.service.association;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.relation.VersionableEventRelationService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventAssociationServiceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
//@TransactionConfiguration(defaultRollback=false)
public class VersionableEventAssociationServiceTest extends LexEvsDbUnitTestBase {

	/** The versionable event association service. */
	@Resource
	private VersionableEventAssociationService versionableEventAssociationService;
	
	@Resource
	private VersionableEventRelationService versionableEventRelationService;
	
	/** The coding scheme service. */
	@Resource 
	private CodingSchemeService codingSchemeService;
	
    @Resource
    private AuthoringService authoringService;
	
	/**
	 * Test insert relations.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testInsertRelations() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		this.authoringService.loadRevision(scheme, null);
		
		Relations relations = new Relations();
		relations.setContainerName("containerName");
		
		AssociationPredicate ap = new AssociationPredicate();
		ap.setAssociationName("aName");
		
		relations.addAssociationPredicate(ap);
		
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("source-code");
		source.setSourceEntityCodeNamespace("source-ns");
		
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode("target-code");
		target.setTargetEntityCodeNamespace("target-ns");
		
		source.addTarget(target);
		
		ap.addSource(source);
		
		versionableEventRelationService.insertRelation("uri", "v1", relations);
	
		JdbcTemplate template = new JdbcTemplate(dataSource);
		assertEquals(1, template.queryForObject("Select count(*) from associationpredicate", Integer.class));
		assertEquals(1, template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
		assertEquals(1, template.queryForObject("Select count(*) from relation", Integer.class));

	}
	
	/**
	 * Test insert relations with two association targets.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testInsertRelationsWithTwoAssociationTargets() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		this.authoringService.loadRevision(scheme, null);
		
		Relations relations = new Relations();
		relations.setContainerName("containerName");
		
		AssociationPredicate ap = new AssociationPredicate();
		ap.setAssociationName("aName");
		
		relations.addAssociationPredicate(ap);
		
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("source-code");
		source.setSourceEntityCodeNamespace("source-ns");
		
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode("target-code");
		target.setTargetEntityCodeNamespace("target-ns");
		
		AssociationTarget target2 = new AssociationTarget();
		target2.setTargetEntityCode("target-code2");
		target2.setTargetEntityCodeNamespace("target-ns2");
		
		source.addTarget(target);
		source.addTarget(target2);
		
		ap.addSource(source);
		
		versionableEventRelationService.insertRelation("uri", "v1", relations);
	
		JdbcTemplate template = new JdbcTemplate(dataSource);
		assertEquals(1, template.queryForObject("Select count(*) from associationpredicate", Integer.class));
		assertEquals(2, template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
		assertEquals(1, template.queryForObject("Select count(*) from relation", Integer.class));

	}
	
	@Test
	@Transactional
	public void testInsertAssociationSource() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		this.authoringService.loadRevision(scheme, null);
		
		Relations relations = new Relations();
		relations.setContainerName("containerName");
		
		AssociationPredicate ap = new AssociationPredicate();
		ap.setAssociationName("aName");
		
		relations.addAssociationPredicate(ap);
		
		versionableEventRelationService.insertRelation("uri", "v1", relations);

		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("source-code");
		source.setSourceEntityCodeNamespace("source-ns");
		
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode("target-code");
		target.setTargetEntityCodeNamespace("target-ns");
		
		source.addTarget(target);
		
		versionableEventAssociationService.insertAssociationSource("uri", "v1", "containerName", "aName", source);
	
		JdbcTemplate template = new JdbcTemplate(dataSource);
		assertEquals(1, template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
	}
}
