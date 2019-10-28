package org.lexevs.dao.database.graph.rest.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.graph.rest.client.errorhandler.LexEVSGraphClientResponseErrorHandler;
import org.lexevs.dao.database.graph.rest.client.model.GraphDatabase;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class LexEVSSpringRestClientImpl {



	public static final String GET_INBOUND = "getInbound";
	public static final String GET_OUTBOUND = "getOutbound";
	public static final String DBS = "databases";
	public static final String GRAPH_DBS = "graphDbs";
	public static final String CORRECTED_URL = "/{direction}/{scheme}/{graph}/{code}";
	public static final String CORRECTED_URL_FOR_DBS = "/{dbs}";
	public static final String CORRECTED_URL_FOR_GRAPHDBS = "/{gDbs}/{database}";
	private String url;
	
	
	public LexEVSSpringRestClientImpl(String url) {
		super();
		this.url = url;
	}
	
	public String databases(final String uri){
	    return new RestTemplate().getForObject(uri + CORRECTED_URL_FOR_DBS, String.class, DBS);
	}
	
	
	public GraphDatabase getGraphDatabaseMetadata(String uri, final String database){
		return new RestTemplate().getForObject(uri + CORRECTED_URL_FOR_GRAPHDBS, GraphDatabase.class, GRAPH_DBS, database);
	}
	
	private List<LexVertex> getVertexesForGraphNode(String url, String direction, String scheme, String graph, String code){
		RestTemplate restTemplate = new RestTemplate();
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		LexEVSGraphClientResponseErrorHandler handler = new LexEVSGraphClientResponseErrorHandler(scheme, graph, code);
		restTemplate.setErrorHandler(handler);
		ResponseEntity<LexVertex[]> response = restTemplate.exchange(url + CORRECTED_URL, HttpMethod.GET, null, LexVertex[].class, direction, scheme, graph, code);
		List<LexVertex> vertexes = Arrays.asList(response.getBody());
		return vertexes;
	}
	
	public List<LexVertex> getInBoundForGraphNode(String url, final String scheme, final String graph, final String code){
		return getVertexesForGraphNode(url, GET_INBOUND, scheme, graph, code);
	}

	public List<LexVertex> getOutBoundForGraphNode(String url, final String scheme, final String graph, final String code){
		return getVertexesForGraphNode(url, GET_OUTBOUND, scheme, graph, code);
	}
	public static void main(String...strings){
		System.out.println(new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").databases( "http://localhost:8080/graph-resolve"));
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getVertexesForGraphNode( 
				"http://localhost:8080/graph-resolve", GET_OUTBOUND, "NCI_Thesaurus", "subClassOf", "C12434")
 		.forEach(x -> System.out.println(x.getCode() + ":" + x.getNamespace()));
//		new LexEVSSpringRestClientImpl().getInBoundForGraphNode(
//				"http://localhost:8080/graph-resolve", "NCI_Thesaurus", "subClassOf", "C12434")
// 		.forEach(x -> System.out.println(x.getCode() + ":" + x.getNamespace()));
//		
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getOutBoundForGraphNode(
				"http://localhost:8080/graph-resolve", "NCI_Thesaurus", "subClassOf", "C61410")
 		.forEach(x -> System.out.println(x.getCode() + ":" + x.getNamespace()));
		
		new LexEVSSpringRestClientImpl("http://localhost:8080/graph-resolve").getGraphDatabaseMetadata(
				"http://localhost:8080/graph-resolve", "NCI_Thesaurus")
					.getGraphs().forEach(x -> System.out.println(x));
	}
	
	public String getServiceDataBaseNameForCanonicalTerminologyName(String name){
	    
		return null;
	}


	public String getBaseUrl() {
		return url;
	}
	

}
