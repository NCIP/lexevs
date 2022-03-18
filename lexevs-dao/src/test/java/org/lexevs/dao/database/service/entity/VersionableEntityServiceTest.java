
package org.lexevs.dao.database.service.entity;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.relations.AssociationEntity;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.service.codingscheme.VersionableEventCodingSchemeService;
import org.lexevs.dao.database.service.event.registry.ExtensionLoadingListenerRegistry;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * The Class VersionableEntityServiceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEntityServiceTest extends LexEvsDbUnitTestBase {

	/** The service. */
	@Resource
	private VersionableEventEntityService service;
	
	/** The coding schemeservice. */
	@Resource
	private VersionableEventCodingSchemeService codingSchemeservice;
	
	@Resource
	private AuthoringService authoringService;
	
	@Resource
	private Registry registry;
	
	@Resource
	private ExtensionLoadingListenerRegistry extensionLoadingListenerRegistry;

	@Before
	public void enableListeners() {
		extensionLoadingListenerRegistry.setEnableListeners(true);
	}
	/**
	 * Insert entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void insertEntity() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("ns");
		
		service.insertEntity("uri", "v1", entity);
	}
	
	@Test
	public void insertBatchEntityWithoutCodingNamespace() throws Exception {
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		Entity entity1 = new Entity();
		entity1.setEntityCode("c1");
		Entity entity2 = new Entity();
		entity2.setEntityCode("c2");
		
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(entity1);
		entities.add(entity2);
				
		service.insertBatchEntities("uri", "v1", entities);
		
		for(Entity en : entities) {
			Assert.assertEquals("testName", en.getEntityCodeNamespace());
		}
	
	}
	
	@Test
	public void insertEntityWithoutCodingNamespace() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		
			service.insertEntity("uri", "v1", entity);
		Assert.assertEquals("testName", entity.getEntityCodeNamespace());
	}
	
	@Test
	public void insertEntityWithDuplicatePropId() throws Exception {
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("c-ns");
		
		Text text = new Text();
		text.setContent("value");
		
		Property prop1 = new Property();
		prop1.setPropertyName("prop name");
		prop1.setValue(text);
		prop1.setPropertyId("propertyId");
		
		Property prop2 = new Property();
		prop2.setPropertyName("prop name");
		prop2.setValue(text);
		prop2.setPropertyId("propertyId");
		
		entity.addProperty(prop1);
		entity.addProperty(prop2);
		Assert.assertEquals(2, entity.getPropertyCount());
		
		service.insertEntity("uri", "v1", entity);
		Assert.assertEquals(1, entity.getPropertyCount());
	}
	
	@Test
	public void insertBatchEntityWithDuplicatePropId() throws Exception {
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Text text = new Text();
		text.setContent("value");

		Property prop1 = new Property();
		prop1.setPropertyName("prop name");
		prop1.setValue(text);
		prop1.setPropertyId("propertyId");
		
		Property prop2 = new Property();
		prop2.setPropertyName("prop name");
		prop2.setValue(text);
		prop2.setPropertyId("propertyid");
		
		Entity entity1 = new Entity();
		entity1.setEntityCode("c1");
		entity1.setEntityCodeNamespace("ns");
		entity1.addProperty(prop1);
		entity1.addProperty(prop2);
		
		Entity entity2 = new Entity();
		entity2.setEntityCode("c2");
		entity2.setEntityCodeNamespace("ns");
		entity2.addProperty(prop1);
		entity2.addProperty(prop2);
		
		List<Entity> enList = new ArrayList<Entity>();
		enList.add(entity1);
		enList.add(entity2);

		Assert.assertEquals(2, entity1.getPropertyCount());
		Assert.assertEquals(2, entity2.getPropertyCount());
		service.insertBatchEntities("uri", "v1", enList);
		Assert.assertEquals(1, entity1.getPropertyCount());
		Assert.assertEquals(1, entity2.getPropertyCount());
	}
	
	@Test
	public void insertEntityWithInvalidPropLink() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("entityCodeNamespace");
		
		PropertyLink pl = new PropertyLink();
		pl.setPropertyLink("propertyLink");
		pl.setSourceProperty("src1");
		pl.setTargetProperty("tgt1");
		List<PropertyLink> plList = new ArrayList<PropertyLink>();
		plList.add(pl);
		
		entity.setPropertyLink(plList);
		
		Assert.assertEquals(1, entity.getPropertyLink().length);
		service.insertEntity("uri", "v1", entity);
		Assert.assertEquals(0, entity.getPropertyLink().length);
	}
	
	@Test
	public void insertBatchEntityWithInvalidPropLink() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		PropertyLink pl = new PropertyLink();
		pl.setPropertyLink("propertyLink");
		pl.setSourceProperty("src1");
		pl.setTargetProperty("tgt1");
		
		List<PropertyLink> plList = new ArrayList<PropertyLink>();
		plList.add(pl);
		
		Entity entity1 = new Entity();
		entity1.setEntityCode("c1");
		entity1.setEntityCodeNamespace("entityCodeNamespace");
		entity1.setPropertyLink(plList);
		
		Entity entity2 = new Entity();
		entity2.setEntityCode("c2");
		entity2.setEntityCodeNamespace("entityCodeNamespace");
		entity2.setPropertyLink(plList);
		
		List<Entity> enList = new ArrayList<Entity>();
		enList.add(entity1);
		enList.add(entity2);
		
		Assert.assertEquals(1, entity1.getPropertyLink().length);
		Assert.assertEquals(1, entity2.getPropertyLink().length);
		service.insertBatchEntities("uri", "v1", enList);
		Assert.assertEquals(0, entity1.getPropertyLink().length);
		Assert.assertEquals(0, entity2.getPropertyLink().length);
	}
	
	@Test
	public void updateEntity() throws Exception{
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		CodingScheme cs = codingSchemeservice.getCodingSchemeByUriAndVersion("uri", "v1");
		System.out.println(cs);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("ns");
		entity.setIsDefined(false);
		
		EntityDescription ed = new EntityDescription();
		ed.setContent("pre-update");
		entity.setEntityDescription(ed);
		
		service.insertEntity("uri", "v1", entity);
		
		entity.getEntityDescription().setContent("post-update");
		
		try {
			service.updateEntity("uri", "v1", entity);
		} catch (Exception e) {
			fail("Expected -- throwing Exception on update with no Entity Type.");
		}

		Entity moddedEntity = service.getEntity("uri", "v1", "c1", "ns");
		
		Assert.assertEquals("post-update", moddedEntity.getEntityDescription().getContent());
	}
	
	@Test
	public void getAssociationEntity() throws Exception{
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, forwardName, reverseName, isNavigable, isTransitive) " +
			"values ('1', '1', 'ecode', 'ens', 'forwardName', 'reverseName', '1', '1')");
			
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'association')");
		
		AssociationEntity entity = service.getAssociationEntity("csuri", "csversion", "ecode", "ens");
		assertNotNull(entity);
		
		assertEquals(entity.getEntityCode(), "ecode");
		assertEquals(entity.getEntityCodeNamespace(), "ens");
		assertEquals(entity.getForwardName(), "forwardName");
		assertEquals(entity.getReverseName(), "reverseName");
		assertTrue(entity.getIsNavigable());
		assertTrue(entity.getIsTransitive());
	}

	public void getAssociationEntityWithWrongType() throws Exception{
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
			
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', 'csguid', 'ecode', 'ens')");
			
		template.execute("Insert into entitytype (entityGuid, entityType) " +
			"values ('1', 'someOtherType')");
		
		AssociationEntity entity = service.getAssociationEntity("csuri", "csversion", "ecode", "ens");	
		
		assertNull(entity);
	}
	
	/**
	 * Insert batch entity.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void insertBatchEntity() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
		
		Entity entity = new Entity();
		entity.setEntityCode("c1");
		entity.setEntityCodeNamespace("ns");
		
		service.insertEntity("uri", "v1", entity);
	}

	@Test
	public void testInsertDependentChanges() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('1', '1', 'ecode', 'ens', '1')");
			
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid1', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('1', '1', 'entity', 'NEW', '0', '1')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('2', 'rid2', NOW() )");
		
		Entity entity = new Entity();
		entity.setEntityCode("ecode");
		entity.setEntityCodeNamespace("ens");
		
		entity.setEntryState(new EntryState());
		entity.getEntryState().setChangeType(ChangeType.DEPENDENT);
		entity.getEntryState().setContainingRevision("rid2");
		entity.getEntryState().setRelativeOrder(0l);
		entity.getEntryState().setPrevRevision("rid1");
		
		service.revise("csuri", "csversion", entity);
		
		assertEquals(1, template.queryForInt("select count(*) from h_entity"));
	}
	
	@Test
	public void testInsertDependentChangesWithOneInHistory() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
	
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, entryStateGuid) " +
			"values ('1', '1', 'ecode', 'ens', '1')");
			
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('1', 'rid1', NOW() )");

		template.execute("Insert into entrystate (entrystateguid, entryguid, entrytype, changetype, relativeorder, revisionguid) " +
			"values ('1', '1', 'entity', 'NEW', '0', '1')");
		
		template.execute("Insert into revision (revisionguid, revisionId, revAppliedDate) " +
			"values ('2', 'rid2', NOW() )");
		
		Entity entity = new Entity();
		entity.setEntityCode("ecode");
		entity.setEntityCodeNamespace("ens");
		
		entity.setEntryState(new EntryState());
		entity.getEntryState().setChangeType(ChangeType.DEPENDENT);
		entity.getEntryState().setContainingRevision("rid2");
		entity.getEntryState().setRelativeOrder(0l);
		entity.getEntryState().setPrevRevision("rid1");
		
		service.revise("csuri", "csversion", entity);
		
		assertEquals(1, template.queryForInt("select count(*) from h_entity"));
	}
}