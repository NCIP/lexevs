
package org.lexevs.dao.database.operation.root;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.utility.RegistryUtility;
import org.lexevs.system.service.LexEvsResourceManagingService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DefaultRootBuilderTest extends LexEvsDbUnitTestBase {

	@Resource
	private DefaultRootBuilder rootBuilder;
	
	@Resource
	private Registry registry;
	
	@Resource
	private LexEvsResourceManagingService lexEvsResourceManagingService;
	
	@Test
	public void testBuildRootFromOneElementRoot() throws Exception {
	JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('1', 'csname', 'csuri', 'csversion')");
		
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
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		rootBuilder.addRootRelationNode("csuri", "csversion", DaoUtility.createNonTypedList("test-assoc"), "c-name", RootOrTail.ROOT, TraverseAssociations.TOGETHER);
	
		int count = template.queryForObject("Select count(*) from entityassnstoentity" +
				" where sourceEntitycode = '@' and targetEntityCode = 's-code'", Integer.class).intValue();
		
		assertEquals(1,count);
	}
	
	@Test
	public void testBuildRootFromOneTailRoot() throws Exception {
	JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		
		registry.addNewItem(RegistryUtility.codingSchemeToRegistryEntry("csuri", "csversion"));
		
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
				"values ('1', 'csname', 'csuri', 'csversion')");
		
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
		
		template.execute("insert into entityassnstoentity" +
				" values ('1'," +
				" '1'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		rootBuilder.addRootRelationNode("csuri", "csversion", DaoUtility.createNonTypedList("test-assoc"), "c-name", RootOrTail.TAIL, TraverseAssociations.TOGETHER);
	
		int count = template.queryForObject("Select count(*) from entityassnstoentity" +
				" where sourceEntitycode = 't-code1' and targetEntityCode = '@@'", Integer.class).intValue();
		
		assertEquals(1,count);
	}
}