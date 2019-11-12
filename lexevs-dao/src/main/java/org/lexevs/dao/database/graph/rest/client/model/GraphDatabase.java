package org.lexevs.dao.database.graph.rest.client.model;

import java.util.List;

public class GraphDatabase {
	
	public GraphDatabase(){
		super();
	}

	public GraphDatabase(String graphDbName, List<String> graphs) {
		super();
		this.graphDbName = graphDbName;
		this.graphs = graphs;
	}

	/**
	 * @return the graphDbName
	 */
	public String getGraphDbName() {
		return graphDbName;
	}

	/**
	 * @param graphDbName the graphDbName to set
	 */
	public void setGraphDbName(String graphDbName) {
		this.graphDbName = graphDbName;
	}

	/**
	 * @return the graphs
	 */
	public List<String> getGraphs() {
		return graphs;
	}

	/**
	 * @param graphs the graphs to set
	 */
	public void setGraphs(List<String> graphs) {
		this.graphs = graphs;
	}

	public String graphDbName;
	
	public List<String> graphs;

}
