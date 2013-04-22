package org.lexevs.graph.load.orientdb.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;

public class OrientDBGraphTestInMemoryIT {

	public static OrientDbGraphDbConnect  graphdb;
	public static Properties p;
	public boolean setUpIsDone = false;
	public static final String EDGE_TABLE = "edgeTable";
	public static final String VERTEX_TABLE = "vertexTable";
	
	
	@Before
	public void setUp() throws Exception {

		    if (setUpIsDone) {
		        return;
		    }
			 p = new Properties();
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test.properties");
				p.load(inputStream);
				graphdb = new OrientDbGraphDbConnect(
						p.getProperty("user"), 
						p.getProperty("password"), 
						p.getProperty("path"));
				try{
				graphdb.createEdgeTable(EDGE_TABLE, graphdb.getFieldNamesForEdge());
				graphdb.createVertexTable(VERTEX_TABLE, graphdb.getFieldNamesForVertex());
				graphdb.initVerticesAndEdge();
				}
				catch(Exception e){
					fail();
				}
		    setUpIsDone = true;
		
	}


	
	@Test
	public void testOrientDbGraphDbConnect() {
		assertTrue(graphdb.verifyDatabase());
	}

	@Test
	public void testGetSource() {
	assertNotNull(graphdb.getSource());
	}

	@Test
	public void testGetTarget() {
		assertNotNull(graphdb.getTarget());
	}

	@Test
	public void testGetEdge() {
		assertNotNull(graphdb.getEdge());
	}


	@Test
	public void testOpenForWrite() {
		assertNotNull(graphdb.openForWrite(p.getProperty("path")));
	}

	@Test
	public void testOpenForRead() {
		assertNotNull(graphdb.openForRead(p.getProperty("path")));
	}

	@Test
	public void testGetGraphDbFromPool() {
		assertNotNull(graphdb.getGraphDbFromPool(p.getProperty("path"), "admin", "admin"));
	}
	
	@Test
	public void testCreateVertexTable() {
		List<String> fieldnames = graphdb.getFieldNamesForVertex();
		OClass o = graphdb.createVertexTable(VERTEX_TABLE , fieldnames);
		assertNotNull(o);
//		OGraphDatabase db = graphdb.getGraphDbFromPool(p.getProperty("path"), "admin", "admin");

	}

	@Test
	public void testCreateEdgeTable() {
		List<String> fieldnames = graphdb.getFieldNamesForEdge();
		assertNotNull(graphdb.createVertexTable(EDGE_TABLE , fieldnames));
	}
	

	@Test
	public void testStoreTriple() {
		List<GraphDbTriple> triple = generateTriples(1);
		
		graphdb.storeGraphTriple(triple.get(0), VERTEX_TABLE, EDGE_TABLE);
		OGraphDatabase instance = graphdb.getGraphDbFromPool(p.getProperty("path"), "admin", "admin");
		
	}

	public List<GraphDbTriple> generateTriples(int count){
	String sourceEntityCode = "sourceCode_";
	String sourceEntityCodeNamespace = "sourceNs_";
	String sourceSchemeUri = "1.11.111.11111.1";
	String sourceSchemeVersion = "1.0";
	String targetEntityCode = "targetCode_";
	String targetEntityCodeNamespace = "targetNs_";
	String targetSchemeUri = "1.11.111.11111.1";
	String targetSchemeVersion = "1.0";
	String associationPredicateId = "predicateId_";
	String associationName = "associationName_";
	String associationInstanceId = "@34erflajf";
	String entityAssnsGuid = "1234";
	String sourceDescription = "source description";
	String targetDescription = "target description";
	
	List<GraphDbTriple> list = new ArrayList<GraphDbTriple>();
	for(int i=0;i<count;i++){
		GraphDbTriple t = new GraphDbTriple();
		t.setAnonymousStatus(true);
		t.setAssociationInstanceId(associationInstanceId);
		t.setEntityAssnsGuid(entityAssnsGuid);
		t.setSourceDescription(sourceDescription);
		t.setTargetDescription(targetDescription);
		t.setAssociationPredicateId(associationPredicateId + i);
		t.setSourceEntityCode(sourceEntityCode + i);
		t.setSourceEntityNamespace(sourceEntityCodeNamespace + i);
		t.setSourceSchemeUri(sourceSchemeUri);
		t.setSourceSchemeVersion(sourceSchemeVersion);
		t.setTargetEntityCode(targetEntityCode+ i);
		t.setTargetEntityNamespace(targetEntityCodeNamespace + i);
		t.setTargetSchemeUri(targetSchemeUri);
		t.setTargetSchemeVersion(targetSchemeVersion);
		t.setAssociationName(associationName);
		list.add(t);
	}
	
	return list;
}

	
	@AfterClass
	public static void tearDownAfterClass(){
		assertTrue(graphdb.close());
		try{
		graphdb.delete(p.getProperty("path"));
		}
		catch(Exception e){
			fail();
		}
	}

}
