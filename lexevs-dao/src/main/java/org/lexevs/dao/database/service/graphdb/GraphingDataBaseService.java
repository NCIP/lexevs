package org.lexevs.dao.database.service.graphdb;

import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.access.association.model.NodeEdge;

public interface GraphingDataBaseService {
	
	public void insertEdge(NodeEdge edge);
	public void insertVertex(LexVertex vertex);
	public void getGraphingDataBaseForURI(String uri);

}
