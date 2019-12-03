package org.lexevs.dao.database.graph.rest.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph.GraphDbValidateConnnection;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.graph.rest.client.model.GraphDatabase;
import org.lexevs.dao.database.graph.rest.client.model.SystemMetadata;

public class LexEVSSpringRestClientImplTest {
	
	static String uri = "http://localhost:8080/graph-resolve";

	
	@BeforeClass
	public static void checkConnection() throws FileNotFoundException, IOException{
		Properties p = new Properties();
		p.load(new FileReader(new File("resources/testData/test.properties")));
		uri = p.getProperty("grapdbURL");
		Assume.assumeTrue(new GraphDbValidateConnnection(uri).connect());
	}

	@Test
	public void testDbExists() {
		String json = new LexEVSSpringRestClientImpl(uri).databases();
		assertNotNull(json);
		assertTrue(json.contains("owl2lexevs"));
	}
	
	@Test
	public void testDbExistsInList() {
		SystemMetadata sm = new LexEVSSpringRestClientImpl(uri).systemMetadata();
		assertNotNull(sm);
		assertTrue(sm.getDataBases().contains("owl2lexevs"));
	}
	
	@Test
	public void getGraphsForDbTest(){
		GraphDatabase db = new LexEVSSpringRestClientImpl(uri).getGraphDatabaseMetadata("owl2lexevs");
		assertNotNull(db.getGraphDbName());
		assertEquals("owl2lexevs", db.getGraphDbName());
		assertNotNull(db.graphs);
		assertTrue(db.graphs.size() > 0);
		assertTrue(db.graphs.contains("subPropertyOf"));
		assertTrue(db.graphs.contains("AssociationURI"));
		assertTrue(db.graphs.contains("AllDifferent"));
		assertTrue(db.graphs.contains("sameAs"));
		assertTrue(db.graphs.contains("IAO_0000116"));
		assertTrue(db.graphs.contains("IAO_0000136"));
	}
	
	@Test
	public void getInboundEdgesForCodePopulated(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getVertexesForGraphNode( "getInbound", -1,
				 "owl2lexevs", "subClassOf", "C123");
		assertNotNull(vertexes);
		assertTrue(vertexes.size() > 0);
		assertTrue(vertexes.stream().anyMatch(x -> x.getCode().equals("BRaf")));
		assertTrue(vertexes.stream().anyMatch(x -> x.getCode().equals("Mefv")));
		assertTrue(vertexes.stream().anyMatch(x -> x.getCode().equals("SOS")));
		assertTrue(vertexes.stream().anyMatch(x -> x.getCode().equals("Brca1")));
		assertTrue(vertexes.stream().anyMatch(x -> x.getCode().equals("actin")));
	}
	
	@Test
	public void getOutboundEdgesForCodeEmpty(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getVertexesForGraphNode( "getOutbound", -1, "owl2lexevs", "subClassOf", "C123");
		assertNotNull(vertexes);
		assertEquals(0, vertexes.size());
	}
	
	@Test
	public void getOutboundEdgesForCodeSize2(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getVertexesForGraphNode( "getOutbound", -1,"owl2lexevs", "Concept_In_Subset", "C48323");
		assertNotNull(vertexes);
		assertEquals(2 , vertexes.size());
	}
	
	@Test
	public void getInboundEdgesForCodeSize4(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getVertexesForGraphNode( "getInbound", -1,"owl2lexevs", "Concept_In_Subset", "C48323");
		assertNotNull(vertexes);
		assertEquals(4, vertexes.size());
	}
	
	@Test
	public void getInboundEdgesForCodeSize5(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getVertexesForGraphNode( "getInbound", -1, "owl2lexevs", "Concept_In_Subset", "C117743");
		assertNotNull(vertexes);
		assertEquals(6, vertexes.size());
	}
	
	@Test
	public void getOutoundEdgesForCodeNone(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getVertexesForGraphNode( "getOutbound", -1, "owl2lexevs", "Concept_In_Subset", "C117743");
		assertNotNull(vertexes);
		assertEquals(0, vertexes.size());
	}
	
	@Test
	public void getValidatedNameForDbName(){
		String name = new LexEVSSpringRestClientImpl(uri).getServiceDataBaseNameForCanonicalTerminologyName( "owl2lexevs");
		assertNotNull(name);
		assertEquals("owl2lexevs", name);
	}
	


}
