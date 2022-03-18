
package org.lexevs.dao.database.service.association;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.relations.AssociationData;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.association.AssociationService.AssociationTriple;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
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
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;
	
    @Resource
    private AuthoringService authoringService;
    
    @Before
	public void enableListeners() {
		extensionLoadingListenerRegistry.setEnableListeners(true);
	}

	
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

		this.authoringService.loadRevision(scheme, null, null);
		
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
		assertEquals(new Integer(1), template.queryForObject("Select count(*) from associationpredicate", Integer.class));
		assertEquals(new Integer(1), template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
		assertEquals(new Integer(1), template.queryForObject("Select count(*) from relation", Integer.class));

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

		this.authoringService.loadRevision(scheme, null, null);
		
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
		assertEquals(new Integer(1), template.queryForObject("Select count(*) from associationpredicate", Integer.class));
		assertEquals(new Integer(2), template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
		assertEquals(new Integer(1), template.queryForObject("Select count(*) from relation", Integer.class));

	}
	
	@Test
	@Transactional
	public void testInsertAssociationSource() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		this.authoringService.loadRevision(scheme, null, null);
		
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
		assertEquals(new Integer(1), template.queryForObject("Select count(*) from entityassnstoentity", Integer.class));
	}
	
	@Test
	@Transactional
	public void testGetAssociationTripleTarget() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		this.authoringService.loadRevision(scheme, null, null);
		
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
		target.setAssociationInstanceId("testAssocId");
		target.setTargetEntityCode("target-code");
		target.setTargetEntityCodeNamespace("target-ns");
		
		source.addTarget(target);
		
		versionableEventAssociationService.insertAssociationSource("uri", "v1", "containerName", "aName", source);
	
		AssociationTriple triple = 
			versionableEventAssociationService.getAssociationTripleByAssociationInstanceId("uri", "v1", "testAssocId");
		
		assertEquals("containerName", triple.getRelationContainerName());
		assertEquals("aName", triple.getAssociationPredicateName());
		assertEquals("source-code", triple.getAssociationSource().getSourceEntityCode());
		assertEquals("source-ns", triple.getAssociationSource().getSourceEntityCodeNamespace());
		assertEquals(1,triple.getAssociationSource().getTargetCount());
		assertEquals(0,triple.getAssociationSource().getTargetDataCount());
		assertEquals("target-code", triple.getAssociationSource().getTarget(0).getTargetEntityCode());
		assertEquals("target-ns", triple.getAssociationSource().getTarget(0).getTargetEntityCodeNamespace());
		assertEquals("testAssocId", triple.getAssociationSource().getTarget(0).getAssociationInstanceId());
	}
	
	@Test
	@Transactional
	public void testGetAssociationTripleData() throws Exception {
	
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");

		this.authoringService.loadRevision(scheme, null, null);
		
		Relations relations = new Relations();
		relations.setContainerName("containerName");
		
		AssociationPredicate ap = new AssociationPredicate();
		ap.setAssociationName("aName");
		
		relations.addAssociationPredicate(ap);
		
		versionableEventRelationService.insertRelation("uri", "v1", relations);

		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("source-code");
		source.setSourceEntityCodeNamespace("source-ns");
		
		AssociationData data = new AssociationData();
		data.setAssociationInstanceId("testAssocId");
		Text text = new Text();
		text.setContent("test data");
		data.setAssociationDataText(text);
		
		source.addTargetData(data);
		
		versionableEventAssociationService.insertAssociationSource("uri", "v1", "containerName", "aName", source);
	
		AssociationTriple triple = 
			versionableEventAssociationService.getAssociationTripleByAssociationInstanceId("uri", "v1", "testAssocId");
		
		assertEquals("containerName", triple.getRelationContainerName());
		assertEquals("aName", triple.getAssociationPredicateName());
		assertEquals("source-code", triple.getAssociationSource().getSourceEntityCode());
		assertEquals("source-ns", triple.getAssociationSource().getSourceEntityCodeNamespace());
		assertEquals(0,triple.getAssociationSource().getTargetCount());
		assertEquals(1,triple.getAssociationSource().getTargetDataCount());
		assertEquals("test data", triple.getAssociationSource().getTargetData(0).getAssociationDataText().getContent());
		assertEquals("testAssocId", triple.getAssociationSource().getTargetData(0).getAssociationInstanceId());
	}
}