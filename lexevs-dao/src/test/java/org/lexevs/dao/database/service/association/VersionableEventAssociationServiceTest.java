package org.lexevs.dao.database.service.association;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.junit.Test;
import org.lexevs.dao.database.hibernate.registry.HibernateRegistryDao;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback=false)
public class VersionableEventAssociationServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private VersionableEventAssociationService versionableEventAssociationService;
	
	@Resource 
	private CodingSchemeService codingSchemeService;
	
	@Resource 
	private HibernateRegistryDao hibernateRegistryDao;
	
	@Test
	@Transactional
	public void testInsertRelations() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		codingSchemeService.insertCodingScheme(scheme);
		
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
		
		versionableEventAssociationService.insertRelation("uri", "v1", relations);
	
		JdbcTemplate template = new JdbcTemplate(dataSource);
		assertEquals(1, template.queryForObject("Select count(*) from associationpredicate", Integer.class));
		assertEquals(1, template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
		assertEquals(1, template.queryForObject("Select count(*) from relation", Integer.class));

	}
}
