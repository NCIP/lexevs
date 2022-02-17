
package org.lexevs.dao.database.service.graphdb;

import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;

public interface GraphingDataBaseService {

	public void loadGraphsForTerminologyURIAndVersion(String uri, String version);

	public void loadGraphsForTerminolgyProductionTerminologyUri(String uri);

	public LexEVSRelsToGraphDao getRels2graph();

	public void loadGraph(String graphName, String uri, String version);

}