package org.lexevs.dao.index.indexer;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.service.IndexService;
import org.lexevs.locator.LexEvsServiceLocator;

public class EntityBatchingIndexCreatorTest  {

	@Resource
	private EntityBatchingIndexCreator entityBatchingIndexCreator;
	
	@Test
	public void testSetUp() {
		assertNotNull(this.entityBatchingIndexCreator);
	}
	
	@Test
	public void testCreateIndex() {
		IndexService service = LexEvsServiceLocator.getInstance().getIndexService();
		EntityService es = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
		List<Entity> entities = es.getEntities("urn:oid:2.16.840.1.113883.6.110", "1993.bvt", 0, 10);
		System.out.println(entities.size());
		
		AbsoluteCodingSchemeVersionReference ref = DaoUtility.createAbsoluteCodingSchemeVersionReference("urn:oid:2.16.840.1.113883.6.110", "1993.bvt");
		service.createIndex(ref);
	}
}
