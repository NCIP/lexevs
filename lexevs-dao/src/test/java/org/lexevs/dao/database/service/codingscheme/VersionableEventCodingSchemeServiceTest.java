
package org.lexevs.dao.database.service.codingscheme;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.PropertyLink;
import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.relations.AssociationPredicate;
import org.LexGrid.relations.AssociationQualification;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.junit.Test;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.error.DatabaseError;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.dao.database.service.error.ErrorHandlingService;
import org.lexevs.dao.database.service.version.AuthoringService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventCodingSchemeServiceTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventCodingSchemeServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private DatabaseServiceManager databaseServiceManager;
	
	/** The service. */
	@Resource
	private VersionableEventCodingSchemeService service;
	
    @Resource
    private AuthoringService authoringService;
	
	@Resource
	private Registry registry;
	
	/**
	 * Insert coding scheme.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void insertCodingScheme() throws Exception{
		
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		authoringService.loadRevision(scheme, null, null);
	}
	
	/**
	 * Insert coding scheme with local name.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void insertCodingSchemeWithLocalName() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.addLocalName("localName");
		
		authoringService.loadRevision(scheme, null, null);
	}
	
	/**
	 * Test insert coding scheme with entity and property.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertCodingSchemeWithEntityAndProperty() throws Exception{

		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.setEntities(new Entities());
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("ns");
		
		Property prop = new Property();
		prop.setPropertyName("name");
		prop.setPropertyId("id");
		prop.setValue(DaoUtility.createText("value"));
		
		entity.addProperty(prop);
		
		scheme.getEntities().addEntity(entity);
		
		authoringService.loadRevision(scheme, null, null);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("Select count(*) from codingScheme"));
		assertEquals(1, template.queryForInt("Select count(*) from entity"));
		assertEquals(1, template.queryForInt("Select count(*) from property"));
			
	}
	
	/**
	 * Test insert coding scheme with everything.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	public void testInsertCodingSchemeWithEverything() throws Exception{
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		scheme.setRepresentsVersion("v1");
		
		scheme.setEntities(new Entities());
		
		Entity entity = new Entity();
		entity.setEntityCode("code");
		entity.setEntityCodeNamespace("ns");
		
		Entity entity2 = new Entity();
		entity2.setEntityCode("code2");
		entity2.setEntityCodeNamespace("ns");
		
		Property prop = new Property();
		prop.setPropertyName("name");
		prop.setPropertyId("id");
		prop.setValue(DaoUtility.createText("value"));
		
		Property prop2 = new Property();
		prop2.setPropertyName("name2");
		prop2.setPropertyId("id2");
		prop2.setValue(DaoUtility.createText("value2"));
		
		PropertyQualifier qual = new PropertyQualifier();
		qual.setPropertyQualifierName("pname");
		qual.setPropertyQualifierType("qualType");
		qual.setValue(DaoUtility.createText("test content"));
		prop.addPropertyQualifier(qual);
	
		entity.addProperty(prop);
		entity.addProperty(prop2);
		
		PropertyLink link = new PropertyLink();
		link.setSourceProperty("id");
		link.setTargetProperty("id2");
		link.setPropertyLink("prop-link");
		
		Relations rel = new Relations();
		rel.setContainerName("rel-name");
		
		AssociationPredicate pred = new AssociationPredicate();
		pred.setAssociationName("assoc-name");
		
		AssociationSource source = new AssociationSource();
		source.setSourceEntityCode("code");
		source.setSourceEntityCodeNamespace("ns");
		
		AssociationTarget target = new AssociationTarget();
		target.setTargetEntityCode("code2");
		target.setTargetEntityCodeNamespace("ns");
		source.addTarget(target);
		
		AssociationQualification assocQual = new AssociationQualification();
		assocQual.setAssociationQualifier("assoc-qual");
		assocQual.setQualifierText(DaoUtility.createText("qual-text"));
		
		target.addAssociationQualification(assocQual);
		
		entity.addPropertyLink(link);
		
		scheme.getEntities().addEntity(entity);
		
		scheme.addRelations(rel);
		rel.addAssociationPredicate(pred);
		pred.addSource(source);
		
		authoringService.loadRevision(scheme, null, null);
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(1, template.queryForInt("Select count(*) from codingScheme"));
		assertEquals(1, template.queryForInt("Select count(*) from entity"));
		assertEquals(2, template.queryForInt("Select count(*) from property"));
		assertEquals(1, template.queryForInt("Select count(*) from propertymultiattrib"));
		assertEquals(1, template.queryForInt("Select count(*) from propertylinks"));
		assertEquals(1, template.queryForInt("Select count(*) from entityassnstoentity"));
		assertEquals(1, template.queryForInt("Select count(*) from associationpredicate"));
		assertEquals(1, template.queryForInt("Select count(*) from relation"));
		
		
	}
	
	@Test
	@Transactional
	public void updateCodingSchemeWithMappingsInsert() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(0, template.queryForInt("Select count(*) from cssupportedattrib"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entryStateGuid) " +
			"values ('1', 'csname', 'csuri', 'csversion', '1')");
		
		template.execute("Insert into cssupportedattrib " +
			"values ('99', '1', 'CodingScheme', 'id', 'uri', " +
			"null, null, null, null, null, null, null, null, null, null, null)");
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setResourceUri("csuri");
		entry.setResourceVersion("csversion");
		entry.setDbSchemaVersion("2.0");
		
		registry.addNewItem(entry);
		
		CodingScheme cs = service.getCodingSchemeByUriAndVersion("csuri", "csversion");
		
		SupportedSource source = new SupportedSource();
		source.setContent("test");
		source.setLocalId("someId");
		cs.getMappings().addSupportedSource(source);
		
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.MODIFY);
		es.setRelativeOrder(1l);
		
		cs.setEntryState(es);
		
		service.updateCodingScheme(cs);
		
		assertEquals(2, template.queryForInt("Select count(*) from cssupportedattrib"));
	}
	
	@Test
	public void updateCodingSchemeWithMappingsUpdate() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		assertEquals(0, template.queryForInt("Select count(*) from cssupportedattrib"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion, entryStateGuid) " +
			"values ('1', 'csname', 'csuri', 'csversion', '1')");
		
		template.execute("Insert into cssupportedattrib " +
			"values ('1', '1', 'CodingScheme', 'id', 'uri', " +
			"null, null, null, null, null, null, null, null, null, null, null)");
		
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setResourceUri("csuri");
		entry.setResourceVersion("csversion");
		entry.setDbSchemaVersion("2.0");
		
		registry.addNewItem(entry);
		
		CodingScheme cs = service.getCodingSchemeByUriAndVersion("csuri", "csversion");
		
		EntryState es = new EntryState();
		es.setChangeType(ChangeType.MODIFY);
		es.setRelativeOrder(1l);
		
		cs.setEntryState(es);
		
		SupportedCodingScheme scs = new SupportedCodingScheme();
		scs.setLocalId("id");
		scs.setUri("uri");
		
		cs.getMappings().addSupportedCodingScheme(scs);
		
		service.updateCodingScheme(cs);
		
		assertEquals(1, template.queryForInt("Select count(*) from cssupportedattrib"));
	}
	
	/**
	 * Test insert coding scheme with everything.
	 * 
	 * @throws Exception the exception
	 */
	@Test
	@Transactional
	public void testErrorCallbackCodingSchemeService() throws Exception{
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		
		CachingCallback callback = new CachingCallback();
		
		CodingSchemeService service = databaseServiceManager.wrapServiceForErrorHandling(
				databaseServiceManager.getCodingSchemeService(), callback);
		
		service.insertCodingScheme(scheme, null);
		
		List<DatabaseError> errors = callback.errors;
		
		assertEquals(1, errors.size());	
		
		assertEquals(CodingSchemeService.INSERT_CODINGSCHEME_ERROR, errors.get(0).getErrorCode());
	}
	
	@Test
	public void testErrorCallbackCodingSchemeServiceNonAnnotatedException() throws Exception{
		CodingScheme scheme = new CodingScheme();
		scheme.setApproxNumConcepts(111l);
		scheme.setCodingSchemeName("testName");
		scheme.setCodingSchemeURI("uri");
		
		CachingCallback callback = new CachingCallback();
		
		CodingSchemeService service = databaseServiceManager.wrapServiceForErrorHandling(
				new TestAnnotatedCodingSchemeService(), callback);
		
		service.validatedSupportedAttribute(null, null, null, null);
		
		List<DatabaseError> errors = callback.errors;
		
		assertEquals(1, errors.size());	
		
		assertEquals(DatabaseError.UNKNOWN_ERROR_CODE, errors.get(0).getErrorCode());
	}

	@Test
	@Transactional
	public void testErrorCallbackCodingSchemeServiceWithRollback() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		CodingScheme goodScheme = new CodingScheme();
		goodScheme.setApproxNumConcepts(111l);
		goodScheme.setCodingSchemeName("testName");
		goodScheme.setCodingSchemeURI("uri");
		goodScheme.setRepresentsVersion("12");
		
		CodingScheme badScheme = new CodingScheme();
		badScheme.setApproxNumConcepts(111l);
		badScheme.setCodingSchemeName("testName");
		badScheme.setCodingSchemeURI("uri");
		
		CachingCallback callback = new CachingCallback();
		
		CodingSchemeService service = databaseServiceManager.wrapServiceForErrorHandling(
				databaseServiceManager.getCodingSchemeService(), callback);
		
		service.insertCodingScheme(goodScheme, null);
		service.insertCodingScheme(badScheme, null);
		
		List<DatabaseError> errors = callback.errors;
		
		assertEquals(1, errors.size());	
		
		assertEquals(CodingSchemeService.INSERT_CODINGSCHEME_ERROR, errors.get(0).getErrorCode());
		
		assertEquals(1, template.queryForInt("Select count(*) from codingscheme"));
	}
	
	private class CachingCallback implements ErrorCallbackListener {

		List<DatabaseError> errors = new ArrayList<DatabaseError>();
		
		@Override
		public void onDatabaseError(DatabaseError databaseError) {
			errors.add(databaseError);
		}
	}
	

	@ErrorHandlingService(matchAllMethods=true)
	public static class TestAnnotatedCodingSchemeService extends
	VersionableEventCodingSchemeService {

		public TestAnnotatedCodingSchemeService(){}
	}
}