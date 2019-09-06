package org.lexevs.dao.database.service.graphdb;

public interface GraphingDataBaseService {

	public void loadGraphsForTerminologyURIAndVersion(String uri, String version);

	public void loadGraphsForTerminolgyProductionTerminologyUri(String uri);

}
