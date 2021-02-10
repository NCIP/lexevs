
package org.lexevs.dao.database;

import javax.annotation.Resource;

import org.junit.Test;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class DatabaseSchemaInstallationTest extends LexEvsDbUnitTestBase{
	
	@Resource
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	@Test
	public void testInstallCommonTables() throws Exception {
		lexEvsDatabaseOperations.dropAllTables();
		lexEvsDatabaseOperations.createAllTables();
	}
}