
package org.lexevs.dao.database.graph.rest.client;

import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension;
import org.junit.Before;
import org.junit.Test;

public class LexEVSSpringRestClientImplPerformanceITTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		System.out.println("Getting databaseses: ");
		long start0 = System.currentTimeMillis();
		System.out.println(new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve")
				.databases());
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start0));
		
		System.out.println("\nGetting outbound for vertex 'blood': ");
		long start = System.currentTimeMillis();
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getVertexesForGraphNode( 
				 NodeGraphResolutionExtension.GET_OUTBOUND, -1, "NCI_Thesaurus", "subClassOf", "C12434")
 		.forEach(x -> System.out.println(x.getCode() + ":" + x.getNamespace()));
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start));
		
		System.out.println("\nGetting inbound for vertex 'blood': ");
		long start1 = System.currentTimeMillis();
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getVertexesForGraphNode( "getInbound", -1, "NCI_Thesaurus", "subClassOf", "C12434")
 		.forEach(x -> System.out.println(x.getCode() + ":" + x.getNamespace()));
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start1));
		
		System.out.println("\nGetting outbound for vertex 'C61410': ");
		long start2 = System.currentTimeMillis();
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getVertexesForGraphNode( "getOutbound", -1, "NCI_Thesaurus", "subClassOf", "C61410")
 		.forEach(x -> System.out.println(x.getCode() + ":" + x.getNamespace()));
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start2));
		
		System.out.println("\nGetting graphs for NCI Thesarus: ");
		long start3 = System.currentTimeMillis();
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getGraphDatabaseMetadata(
				"NCI_Thesaurus")
					.getGraphs().forEach(x -> System.out.println(x));
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start3));
		
		System.out.println("\nGetting databases as list: ");
		long start4 = System.currentTimeMillis();
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").systemMetadata()
					.getDataBases().forEach(x -> System.out.println(x));
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start4));
		
		System.out.println("\nNormalizing and validating a database name against the service: ");
		long start5 = System.currentTimeMillis();
		System.out.println(new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve")
				.getServiceDataBaseNameForCanonicalTerminologyName("NCI Thesaurus"));
		System.out.println("Millisecond executiontime: " + (System.currentTimeMillis() - start5));
	}

}