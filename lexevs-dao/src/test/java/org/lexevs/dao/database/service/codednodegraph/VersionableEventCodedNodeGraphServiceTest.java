
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
import org.springframework.transaction.annotation.Transactional;

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
		"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('2', '1', 'apname2')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentitytr" +
				" values ('1'," +
				" '2'," +
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
		"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('2', '1', 'apname2')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentitytr" +
				" values ('1'," +
				" '2'," +
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
		"values ('1', 'csname', 'csuri', 'csversion')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('2', '1', 'apname2')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentitytr" +
				" values ('1'," +
				" '2'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1', " +
				" null)");
		
		List<String> rels = service.
			listCodeRelationships("csuri", "csversion", "INVALID_CONTAINER_NAME", "s-code", "s-ns", "t-code1", "t-ns1", new GraphQuery(), true);
			
		assertEquals(0, rels.size());
	}
	
	@Test
	public void testGetAssociatedConceptResolveTrue() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 's-code', 's-ns')");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource("csuri", "csversion", true, null, null, "1");
		
		assertNotNull(associatedConcept.getEntity());
	}
	
	@Test
	public void testGetAssociatedConceptResolveFalse() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
				"values ('1', '1', 'entity', 'pid', 'pvalue', 'presentation')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 's-code', 's-ns')");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource("csuri", "csversion", false, null, null, "1");
		
		assertNull(associatedConcept.getEntity());
	}
	
	@Test
	public void testGetAssociatedConceptResolveWithPropertyTypeRestriction() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('1', '1', 'entity', 'pid1', 'pvalue1', 'presentation')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('2', '1', 'entity', 'pid2', 'pvalue2', 'definition')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 's-code', 's-ns')");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource(
					"csuri", 
					"csversion", 
					true, 
					null, 
					new PropertyType[] {PropertyType.DEFINITION}, 
					"1");
		
		assertEquals(1,associatedConcept.getEntity().getDefinitionCount());
		assertEquals(0,associatedConcept.getEntity().getPresentationCount());
		assertEquals(1,associatedConcept.getEntity().getAllProperties().length);
	}
	
	@Test
	public void testGetAssociatedConceptResolveWithPropertyNameRestriction() throws Exception {
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('1', '1', 'entity', 'pid1', 'pname1', 'presentation')");
		
		template.execute("Insert into property (propertyGuid, referenceGuid, referenceType, propertyName, propertyValue, propertyType) " +
			"values ('2', '1', 'entity', 'pname2', 'pvalue', 'definition')");
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
			"values ('1', 'csname', 'csuri', 'csversion')");
		
		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
		"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
		"('1', '1', 'apname')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
		" 'ai-id', null, null, null, null, null, null, null, null)");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace) " +
			"values ('1', '1', 's-code', 's-ns')");
		
		LocalNameList lnl = new LocalNameList();
		lnl.addEntry("pname2");
		
		AssociatedConcept associatedConcept = this.service.
			getAssociatedConceptFromUidSource(
					"csuri", 
					"csversion", 
					true, 
					lnl, 
					null, 
					"1");
		
		assertEquals(1,associatedConcept.getEntity().getDefinitionCount());
		assertEquals(0,associatedConcept.getEntity().getPresentationCount());
		assertEquals(1,associatedConcept.getEntity().getAllProperties().length);
	}
}