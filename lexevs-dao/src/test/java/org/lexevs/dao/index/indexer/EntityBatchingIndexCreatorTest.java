package org.lexevs.dao.index.indexer;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.database.utility.DaoUtility;
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
		//TODO:
	}
}
