package org.lexevs.dao.database.graph.rest.client;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.graph.rest.client.model.GraphDatabase;

public class LexEVSSpringRestClientImplTest {
	String uri = "http://localhost:8080/graph-resolve";

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testDbExists() {
		String json = new LexEVSSpringRestClientImpl(uri).databases( "http://localhost:8080/graph-resolve");
		assertNotNull(json);
		assertTrue(json.contains("owl2lexevs"));
	}
	
	@Test
	public void getGraphsForDbTest(){
		GraphDatabase db = new LexEVSSpringRestClientImpl(uri).getGraphDatabaseMetadata(uri, "owl2lexevs");
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
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getInBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "owl2lexevs", "subClassOf", "C123");
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
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getOutBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "owl2lexevs", "subClassOf", "C123");
		assertNotNull(vertexes);
		assertEquals(0, vertexes.size());
	}
	
	@Test
	public void getOutboundEdgesForCodeSize2(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getOutBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "owl2lexevs", "Concept_In_Subset", "C48323");
		assertNotNull(vertexes);
		assertEquals(2 , vertexes.size());
	}
	
	@Test
	public void getInboundEdgesForCodeSize4(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getInBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "owl2lexevs", "Concept_In_Subset", "C48323");
		assertNotNull(vertexes);
		assertEquals(4, vertexes.size());
	}
	
	@Test
	public void getInboundEdgesForCodeSize5(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getInBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "owl2lexevs", "Concept_In_Subset", "C117743");
		assertNotNull(vertexes);
		assertEquals(6, vertexes.size());
	}
	
	@Test
	public void getOutoundEdgesForCodeNone(){
		List<LexVertex> vertexes = new LexEVSSpringRestClientImpl(uri).getOutBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "owl2lexevs", "Concept_In_Subset", "C117743");
		assertNotNull(vertexes);
		assertEquals(0, vertexes.size());
	}
	


}
