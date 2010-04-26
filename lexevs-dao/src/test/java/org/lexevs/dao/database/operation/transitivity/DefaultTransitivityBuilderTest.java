package org.lexevs.dao.database.operation.transitivity;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.springframework.jdbc.core.JdbcTemplate;

public class DefaultTransitivityBuilderTest extends LexEvsDbUnitTestBase {

	@Resource
	private DefaultTransitivityBuilder defaultTransitivityBuilder;
	
	@Resource
	private Registry registry;
	
	@Resource
	private LexEvsResourceManagingService lexEvsResourceManagingService;

	@Test
	public void getRegistryEntryForCodingSchemeName() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		RegistryEntry entry = 
			defaultTransitivityBuilder.getRegistryEntryForCodingSchemeName("csname");
		
		assertNotNull(entry);
		
		assertEquals("csuri", entry.getResourceUri());
		assertEquals("csversion", entry.getResourceVersion());
	}
	
	@Test
	public void getDistinctSourceTriples() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<Node> nodes = 
			defaultTransitivityBuilder.getDistinctSourceTriples("csuri", "csversion", "ap-guid");
		
		assertEquals(1, nodes.size());
	}
	
	@Test
	public void getTransitiveAssociationPredicateIds() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		List<String> transitiveAssocs = 
			defaultTransitivityBuilder.getTransitiveAssociationPredicateIds("csuri", "csversion");
		
		assertEquals(1, transitiveAssocs.size());
		assertEquals("ap-guid", transitiveAssocs.get(0));
	}
	
	@Test
	public void isTransitive() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		boolean isTransitive =
			defaultTransitivityBuilder.isTransitive("csuri", "csversion", "ae-code", "ae-codens");
		
		assertTrue(isTransitive);
	}
	
	@Test
	public void isNotTransitive() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', false)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		boolean isTransitive =
			defaultTransitivityBuilder.isTransitive("csuri", "csversion", "ae-code", "ae-codens");
		
		assertFalse(isTransitive);
	}
	
	@Test
	public void testOneLevel() throws Exception {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isNavigable, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true, true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");

		defaultTransitivityBuilder.computeTransitivityTable("csuri", "csversion");
		
		int count = template.queryForInt("select count(*) from entityassnstoentitytr");
		
		assertEquals(1, count);
	}
	
	@Test
	public void testTwoLevel() throws Exception {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));

		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('cs-guid', 'csname', 'csuri', 'csversion')");
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('cssa-guid', 'cs-guid', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('rel-guid', 'cs-guid', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('ap-guid', 'rel-guid', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isNavigable, isTransitive) " +
				"values ('eguid', 'cs-guid', 'ae-code', 'ae-codens', true, true)");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('eguid', 'association')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id1', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid2'," +
				" 'ap-guid'," +
				" 't-code1', " +
				" 't-ns1'," +
				" 't-code2'," +
				" 't-ns2'," +
				" 'ai-id2', null, null, null, null, null, null, null, null)");

		defaultTransitivityBuilder.computeTransitivityTable("csuri", "csversion");
		
		int count = template.queryForInt("select count(*) from entityassnstoentitytr");
		
		assertEquals(3, count);
	}
}
