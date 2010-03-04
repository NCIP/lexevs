package org.lexevs.dao.database.service.association;

import javax.annotation.Resource;

import org.LexGrid.relations.Relations;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;
import org.springframework.jdbc.core.JdbcTemplate;

public class VersionableEventAssociationServiceTest extends LexEvsDbUnitTestBase {

	@Resource
	private VersionableEventAssociationService versionableEventAssociationService;
	
	@Before
	public void insertCodingScheme() {
		JdbcTemplate template = new JdbcTemplate(this.getDataSource());
		template.execute("Insert into codingScheme (codingSchemeGuid, codingSchemeName, codingSchemeUri, representsVersion) " +
		"values ('csguid', 'csname', 'csuri', 'csversion')");
	}
	
	@Test
	public void testInsertRelations() {
		Relations relations = new Relations();
		relations.setContainerName("containerName");
		
		fail("TODO:");
	}
}
