package org.lexevs.dao.database.operation;

import java.util.List;

public interface LexEVSGraphingDataBaseOperations {
	
	public void createDatabase(String uri);
	public void addVertexCollection(String collectionName);
	public void addEdgeCollection(String collectionName);
	public void dropDatabase(String uri);
	public List<String> getDatabaseLables();

}
