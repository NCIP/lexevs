package org.lexevs.graph.load.service.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.graph.load.service.LexEVSTripleService;
import org.lexevs.graph.load.service.LexEVSTripleService.GraphIterator;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSTripleServiceImpleTestIT {
	LexEVSTripleService service;
	List<String> predicateIds;
	@Before
	public void setUp() throws Exception {
		service = new LexEVSTripleService(GLTestConstants.THES_SCHEME_URI, GLTestConstants.THES_SCHEME_VERSION);
		predicateIds = service.getAssociationPredicateIds(GLTestConstants.THES_SCHEME_URI, GLTestConstants.THES_SCHEME_VERSION);
	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void testgetDatabaseServiceManager(){
		DatabaseServiceManager mgr = service.getDatabaseServiceManager();
		assertNotNull(mgr);
	}
	
	@Test
	public void testgetService(){
		LexEvsServiceLocator locator = service.getService();
		assertNotNull(locator);
	}
	
	@Test
	public void testGetPredicateIds() {
		
		assertTrue(predicateIds.size() > 0);
	}
	
	@Test
	public void testGetPredicateNameForID(){
		String name = service.getPredicateName(
				GLTestConstants.THES_SCHEME_URI, 
				GLTestConstants.THES_SCHEME_VERSION, 
				predicateIds.get(0));
		assertNotNull(name);
	}
	
	@Test
	public void testGetTripleIteratorforPredicate(){
		String predid = null;

//		for(String s: predicateIds){
//			String name = service.getPredicateName(GLTestConstants.THES_SCHEME_URI, 
//					GLTestConstants.THES_SCHEME_VERSION,s );
//				if(name.equals("subClassOf")){
//					predid = s;
//		}
//		}
		GraphIterator iterator = service.getGraphIterator(
				GLTestConstants.THES_SCHEME_URI, 
				GLTestConstants.THES_SCHEME_VERSION);
		assertNotNull(iterator);
		assertTrue(iterator.hasNext());
		Triple triple = iterator.next();
		assertNotNull(triple.getAssociationPredicateId());
		assertNotNull(triple.getSourceEntityCode());
		assertNotNull(triple.getSourceEntityNamespace());
		assertNotNull(triple.getTargetEntityCode());
		assertNotNull(triple.getTargetEntityNamespace());
	}


}
