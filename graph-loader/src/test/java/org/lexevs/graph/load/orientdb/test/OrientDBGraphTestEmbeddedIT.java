package org.lexevs.graph.load.orientdb.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.graphdb.GraphDbTriple;
import org.lexevs.graph.load.connect.OrientDbGraphDbConnect;

import com.orientechnologies.orient.core.db.graph.OGraphDatabase;
import com.orientechnologies.orient.core.metadata.schema.OClass;
//import com.orientechnologies.orient.server.OServer;
//import com.orientechnologies.orient.server.OServerMain;

public class OrientDBGraphTestEmbeddedIT {
//	static OServer server;
//	public static OrientDbGraphDbConnect  graphdb;
//	public static Properties p;
//	public boolean setUpIsDone = false;
//	public static final String EDGE_TABLE = "Edges";
//	public static final String VERTEX_TABLE = "Nodes";
//	
//	
//	@Before
//	public void setUp() throws Exception {
//
//		    if (setUpIsDone) {
//		        return;
//		    }
//			 p = new Properties();
//				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("test.properties");
//				p.load(inputStream);
//				graphdb = new OrientDbGraphDbConnect(
//						p.getProperty("user"), 
//						p.getProperty("password"), 
//						p.getProperty("path"));
//				try{
//
//					graphdb.createEdgeTable(EDGE_TABLE, graphdb.getFieldNamesForEdge());
//					graphdb.createVertexTable(VERTEX_TABLE, graphdb.getFieldNamesForVertex());
//					graphdb.initVerticesAndEdge();
//				graphdb.initVerticesAndEdge();
//				}
//				catch(Exception e){
//					fail();
//				}
//		    setUpIsDone = true;
//		
//	}
//	
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//		server = OServerMain.create();
//		server.startup(
//		   "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//		   + "<orient-server>"
//		   + "<network>"
//		   + "<protocols>"
//		   + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
//		   + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
//		   + "</protocols>"
//		   + "<listeners>"
//		   + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
//		   + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
//		   + "</listeners>"
//		   + "</network>"
//		   + "<users>"
//		   + "<user name=\"root\" password=\"ThisIsA_TEST\" resources=\"*\"/>"
//		   + "</users>"
//		   + "<properties>"
//		   + "<entry name=\"orientdb.www.path\" value=\"/Users/m029206/software/orientdb-1.3.0/www/\"/>"
//		   + "<entry name=\"orientdb.config.file\" value=\"orientdb-server-config.xml\"/>"
//		   + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
//		   + "<entry name=\"log.console.level\" value=\"info\"/>" + "<entry name=\"log.file.level\" value=\"fine\"/>"
//		   + "</properties>" + "</orient-server>");
//		 server.activate();	
//
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//		assertTrue(graphdb.close());
//		try{
//		graphdb.delete(p.getProperty("path"));
//		}
//		catch(Exception e){
//			fail();
//		}
//		server.shutdown();
//	}
//
//	@Test
//	public void testStoreTriple() {
//		List<GraphDbTriple> triple = generateTriples(1);
//		
//		graphdb.storeGraphTriple(triple.get(0), VERTEX_TABLE, EDGE_TABLE);
//		OGraphDatabase instance = graphdb.getGraphDbFromPool(p.getProperty("path"), "admin", "admin");
//		
//	}

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
	
	

}
