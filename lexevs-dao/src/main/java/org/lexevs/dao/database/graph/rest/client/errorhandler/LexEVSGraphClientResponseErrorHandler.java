package org.lexevs.dao.database.graph.rest.client.errorhandler;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

public class LexEVSGraphClientResponseErrorHandler implements ResponseErrorHandler {
	
	private String graphDb;
	private String graph;
	private String code;
	
	public LexEVSGraphClientResponseErrorHandler(String graphDb, String graph, String code){
		this.graphDb = graphDb;
		this.graph = graph;
		this.code = code;
	}
 
    @Override
    public boolean hasError(ClientHttpResponse httpResponse) 
      throws IOException {
 
        return (
          httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR 
          || httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }
 

    @Override
    public void handleError(ClientHttpResponse httpResponse) 
      throws IOException {
 
        if (httpResponse.getStatusCode()
          .series() == HttpStatus.Series.SERVER_ERROR) {
            System.out.println("Server Error");
        } else if (httpResponse.getStatusCode()
          .series() == HttpStatus.Series.CLIENT_ERROR) {
        	System.out.println("Client side Error");
            if (httpResponse.getStatusCode() == HttpStatus.NOT_FOUND) {
					throw new IOException("Value or Vertex not found for "
							+ "database: " + graphDb 
							+ ", graph: " + graph 
							+ ", code: " + code);
            }
        }
    }

	/**
	 * @return the graphDb
	 */
	public String getGraphDb() {
		return graphDb;
	}

	/**
	 * @param graphDb the graphDb to set
	 */
	public void setGraphDb(String graphDb) {
		this.graphDb = graphDb;
	}

	/**
	 * @return the graph
	 */
	public String getGraph() {
		return graph;
	}

	/**
	 * @param graph the graph to set
	 */
	public void setGraph(String graph) {
		this.graph = graph;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
}
