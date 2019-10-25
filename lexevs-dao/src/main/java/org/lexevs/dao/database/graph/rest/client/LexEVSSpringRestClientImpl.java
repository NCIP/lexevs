package org.lexevs.dao.database.graph.rest.client;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.lexevs.dao.database.access.association.model.LexVertex;

public class LexEVSSpringRestClientImpl {
	
	public String databases(final String uri){
	    return new RestTemplate().getForObject(uri, String.class);
	}
	
	public String getInBoundForGraphNode(String url, String scheme, String graph, String code ){
		return new RestTemplate().getForObject(url, String.class, scheme, graph, code);
//		Class responseType;
//		ResponseEntity<List<LexVertex>> response = new RestTemplate().exchange(url, HttpMethod.GET, null, responseType, scheme, graph, code)
//				List<Employee> employees = response.getBody();
	}
	
	public static void main(String...strings){
		System.out.println(new LexEVSSpringRestClientImpl().databases( "http://localhost:8080/graph-resolve/databases"));
		System.out.println(new LexEVSSpringRestClientImpl().getInBoundForGraphNode( "http://localhost:8080/graph-resolve/getInbound/{scheme}/{graph}/{code}", "NCI_Thesaurus", "subClassOf", "C61410"));
		
	}
	
	

}
