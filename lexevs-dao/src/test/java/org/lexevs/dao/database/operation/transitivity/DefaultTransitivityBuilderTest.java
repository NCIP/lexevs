
package org.lexevs.dao.database.operation.transitivity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Node;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Transactional;

public class DefaultTransitivityBuilderTest extends LexEvsDbUnitTestBase {

	@Resource
	private DefaultTransitivityBuilder defaultTransitivityBuilder;
	
	@Resource
	private Registry registry;
	
	@Resource
	private LexEvsResourceManagingService lexEvsResourceManagingService;
	
	@Before
	public void loadCodingScheme() throws Exception{
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('1', 'csname', 'csuri', 'csversion')");
	}
	
	@Test
	public void getRegistryEntryForCodingSchemeName() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, uri) " +
				"values ('2', '1', 'CodingScheme', 'csname', 'csuri')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		lexEvsResourceManagingService.refresh();
		
		RegistryEntry entry = 
			defaultTransitivityBuilder.getRegistryEntryForCodingSchemeName("csname", "csuri", "csversion");
		
		assertNotNull(entry);
		
		assertEquals("csuri", entry.getResourceUri());
		assertEquals("csversion", entry.getResourceVersion());
	}
	
	@Test
	public void getDistinctSourceTriples() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		lexEvsResourceManagingService.refresh();
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		List<Node> nodes = 
			defaultTransitivityBuilder.getDistinctSourceTriples("csuri", "csversion", "1");
		
		assertEquals(1, nodes.size());
	}

	@Test
	@Transactional
	public void getTransitiveAssociationPredicateIds() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
			
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('1', 'association')");
		
		this.lexEvsResourceManagingService.refresh();
		
		List<String> transitiveAssocs = 
			defaultTransitivityBuilder.getTransitiveAssociationPredicateIds("csuri", "csversion");
		
		assertEquals(1, transitiveAssocs.size());
		assertEquals("1", transitiveAssocs.get(0));
	}
	
	@Test
	public void isTransitive() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('1', 'association')");
		
		boolean isTransitive =
			defaultTransitivityBuilder.isTransitive("csuri", "csversion", "ae-code", "ae-codens");
		
		assertTrue(isTransitive);
	}
	
	@Test
	public void isNotTransitive() throws Exception {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnNamespace, assnEntityCode) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('1', 'association')");
		
		boolean isTransitive =
			defaultTransitivityBuilder.isTransitive("csuri", "csversion", "ae-code", "ae-codens");
		
		assertTrue(isTransitive);
	}
	
	@Test
	public void testOneLevel() throws Exception {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isNavigable, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1', '1')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('1', 'association')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		
		defaultTransitivityBuilder.computeTransitivityTable("csuri", "csversion");
		
		int count = template.queryForInt("select count(*) from entityassnstoentitytr");
		
		assertEquals(1, count);
		
		assertTrue((Boolean)template.queryForObject("Select * from entityassnstoentitytr", new RowMapper(){

			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				
				assertNotNull(rs.getString(1));
				assertEquals(rs.getString(2), "1");
				assertEquals(rs.getString(3), "s-code");
				assertEquals(rs.getString(4), "s-ns");
				assertEquals(rs.getString(5), "t-code1");
				assertEquals(rs.getString(6), "t-ns1");

				return true;
			}
		}));
	}
	
	@Test
	public void testTwoLevel() throws Exception {

		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
			
		template.execute("Insert into cssupportedattrib (csSuppAttribGuid, codingSchemeGuid, supportedAttributeTag, id, assnCodingScheme, assnEntityCode, assnNamespace) " +
				"values ('1', '1', 'Association', 'test-assoc', 'csname', 'ae-code', 'ae-codens')");

		template.execute("insert into " +
				"relation (relationGuid, codingSchemeGuid, containerName) " +
				"values ('1', '1', 'c-name')");
		
		template.execute("insert into " +
				"associationpredicate (associationPredicateGuid," +
				"relationGuid, associationName) values " +
				"('1', '1', 'test-assoc')");
		
		template.execute("Insert into entity (entityGuid, codingSchemeGuid, entityCode, entityCodeNamespace, isNavigable, isTransitive) " +
				"values ('1', '1', 'ae-code', 'ae-codens', '1', '1')");
		
		template.execute("Insert into entitytype (entityGuid, entityType) " +
				"values ('1', 'association')");
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id1', null, null, null, null, null, null, null, null)");
		
		template.execute("insert into entityassnstoentity" +
				" values ('2'," +
				" '1'," +
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