package org.lexevs.graph.load.service.test;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.graph.load.service.LexEVSTripleService;
import org.lexevs.graph.load.service.LexEVSTripleService.TripleIterator;
import org.lexevs.locator.LexEvsServiceLocator;

public class LexEVSTripleServiceImpleTestIT {
	LexEVSTripleService service;
	List<String> predicateIds;
	@Before
	public void setUp() throws Exception {
		service = new LexEVSTripleService();
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

		for(String s: predicateIds){
			String name = service.getPredicateName(GLTestConstants.THES_SCHEME_URI, 
					GLTestConstants.THES_SCHEME_VERSION,s );
				if(name.equals("subClassOf")){
					predid = s;
		}
		}
		TripleIterator iterator = service.getTripleIteratorforPredicate(
				GLTestConstants.THES_SCHEME_URI, 
				GLTestConstants.THES_SCHEME_VERSION, 
				predid);
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
