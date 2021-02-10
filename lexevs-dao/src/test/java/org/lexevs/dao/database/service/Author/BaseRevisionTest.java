
package org.lexevs.dao.database.service.Author;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.dao.test.LexEvsDbUnitTestBase;

public class BaseRevisionTest extends LexEvsDbUnitTestBase {
	
	@Resource
	private EntityIndexService entityIndexService;
	
	@Before
	public void loadSystemRelease() throws Exception {
//		entityIndexService.optimizeAll();
	}
	
	@Test
	public void testSetUpIndexService(){
		assertNotNull(this.entityIndexService);
	}
}