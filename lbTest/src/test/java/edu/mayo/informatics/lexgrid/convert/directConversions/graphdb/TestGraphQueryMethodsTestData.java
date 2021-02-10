
package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.locator.LexEvsServiceLocator;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDBException;
import com.arangodb.ArangoDatabase;

public class TestGraphQueryMethodsTestData {
	private static final int HashMap = 0;
	static ArangoDatabase db;

	@BeforeClass
	public static void setUp() throws Exception {
	db =  LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph().getGraphSourceMgr().getDataSource("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.5").getDbInstance();
	}

	@Test
	public void testqueryAllVertices() throws ArangoDBException {
		String queryString = "FOR v IN 1..10 INBOUND @id GRAPH @graph "
				+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN {code: v._key, namespace: v.namespace}";
		 Map<String, Object> bindVars = new java.util.HashMap<String,Object>();
		 bindVars.put("id", "V_subClassOf/Disease");
		 bindVars.put("graph", "subClassOf");
		 ArangoCursor<String> cursor = db.query(queryString, bindVars, null, String.class);
		List<String> result = cursor.asListRemaining();
		assertEquals(4, result.size());
	}
	
	@Test
	public void testqueryAllLevel1Vertices() throws ArangoDBException {
		String queryString = "FOR v IN 1..#depth INBOUND @id GRAPH @graph "
				+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN {code: v._key, namespace: v.namespace}";
		queryString = queryString.replaceAll("#depth", "1");
		 Map<String, Object> bindVars = new java.util.HashMap<String,Object>();
		 bindVars.put("id", "V_subClassOf/Disease");
		 bindVars.put("graph", "subClassOf");
		 ArangoCursor<String> cursor = db.query(queryString, bindVars, null, String.class);
		List<String> result = cursor.asListRemaining();
		assertEquals(2, result.size());
	}

}