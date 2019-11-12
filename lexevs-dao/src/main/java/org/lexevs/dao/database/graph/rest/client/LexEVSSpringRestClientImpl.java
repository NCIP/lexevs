package org.lexevs.dao.database.graph.rest.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.graph.rest.client.errorhandler.LexEVSGraphClientResponseErrorHandler;
import org.lexevs.dao.database.graph.rest.client.model.GraphDatabase;
import org.lexevs.dao.database.graph.rest.client.model.SystemMetadata;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.lexevs.dao.database.utility.GraphingDatabaseUtil;
import org.lexevs.logging.LoggerFactory;

public class LexEVSSpringRestClientImpl {
	
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }



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
	
	public String databases(){
	    return new RestTemplate().getForObject(url + CORRECTED_URL_FOR_DBS, String.class, DBS);
	}
	
	public SystemMetadata systemMetadata(){
	    return new RestTemplate().getForObject(url + CORRECTED_URL_FOR_DBS, SystemMetadata.class, DBS);
	}
	
	
	public GraphDatabase getGraphDatabaseMetadata( final String database){
		return new RestTemplate().getForObject(url + CORRECTED_URL_FOR_GRAPHDBS, GraphDatabase.class, GRAPH_DBS, database);
	}
	
	List<LexVertex> getVertexesForGraphNode(String direction, String scheme, String graph, String code){
		RestTemplate restTemplate = new RestTemplate();
		
		//Setting message converter to accept all kinds of results including JSON
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
		MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
		converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
		messageConverters.add(converter);
		restTemplate.setMessageConverters(messageConverters);
		
		//Adding an error handler that will manage some of the custom exceptions
		LexEVSGraphClientResponseErrorHandler handler = new LexEVSGraphClientResponseErrorHandler(scheme, graph, code);
		restTemplate.setErrorHandler(handler);
		
		//Getting response entities and resulting list
		ResponseEntity<LexVertex[]> response = null;
		List<LexVertex> vertexes = null;
		try{
		response = restTemplate.exchange(url + CORRECTED_URL, HttpMethod.GET, null, LexVertex[].class, direction, scheme, graph, code);
		vertexes = Arrays.asList(response.getBody());
		}catch(Exception i){
			getLogger().warn(i.getMessage());
			System.out.println(i.getMessage());
			return new ArrayList<LexVertex>();
		}
		return vertexes;
	}
	
	public List<LexVertex> getInBoundForGraphNode(String url, final String scheme, final String graph, final String code){
		return getVertexesForGraphNode( GET_INBOUND, scheme, graph, code);
	}

	public List<LexVertex> getOutBoundForGraphNode(String url, final String scheme, final String graph, final String code){
		return getVertexesForGraphNode(GET_OUTBOUND, scheme, graph, code);
	}
	
	public String getServiceDataBaseNameForCanonicalTerminologyName(String name){
		RestTemplate template = new RestTemplate();
			SystemMetadata svcMeta = template.getForObject(url + CORRECTED_URL_FOR_DBS, SystemMetadata.class, DBS);
		String dbName = GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(name);
		if(svcMeta.getDataBases().contains(dbName))
			{return dbName;}
		else
			{return null;}
	}

	public String getBaseUrl() {
		return url;
	}
	
}
