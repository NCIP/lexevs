
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

public class TestGraphQueryMethods {
	private static final int HashMap = 0;
	static ArangoDatabase db;

	@BeforeClass
	public static void setUp() throws Exception {
	db =  LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getGraphingDatabaseService()
				.getRels2graph().getGraphSourceMgr().getDataSource("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b").getDbInstance();
	}

	@Test
	public void queryAllVertices() throws ArangoDBException {
		String queryString = "FOR v IN 1..10 INBOUND @id GRAPH @graph "
				+ "OPTIONS {bfs: true, uniqueVertices: 'global'} RETURN {code: v._key, namespace: v.namespace}";
		 Map<String, Object> bindVars = new java.util.HashMap<String,Object>();
		 bindVars.put("id", "V_subClassOf/C2916");
		 bindVars.put("graph", "subClassOf");
		 ArangoCursor<String> cursor = db.query(queryString, bindVars, null, String.class);
		List<String> result = cursor.asListRemaining();
		assertEquals(result.size(), 3173);
	}

}