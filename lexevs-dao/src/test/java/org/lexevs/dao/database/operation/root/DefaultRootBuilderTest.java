package org.lexevs.dao.database.operation.root;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.RootOrTail;
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
	public void testBuildRootFromOneElement() throws Exception {
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
		
		template.execute("insert into entityassnstoentity" +
				" values ('eae-guid1'," +
				" 'ap-guid'," +
				" 's-code', " +
				" 's-ns'," +
				" 't-code1'," +
				" 't-ns1'," +
				" 'ai-id', null, null, null, null, null, null, null, null)");
		
		rootBuilder.addRootRelationNode("csuri", "csversion", DaoUtility.createNonTypedList("test-assoc"), "c-name", RootOrTail.ROOT);
	
		int count = template.queryForInt("Select count(*) from entityassnstoentity" +
				" where sourceEntitycode = '@' and targetEntityCode = 's-code'");
		
		assertEquals(1,count);
	}
}
