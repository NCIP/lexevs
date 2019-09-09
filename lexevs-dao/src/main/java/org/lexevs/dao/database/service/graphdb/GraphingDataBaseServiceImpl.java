package org.lexevs.dao.database.service.graphdb;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.graph.LexEVSRelsToGraphDao;
import org.lexevs.locator.LexEvsServiceLocator;

import com.arangodb.ArangoDatabase;
import com.arangodb.entity.GraphEntity;

public class GraphingDataBaseServiceImpl implements GraphingDataBaseService {
	
	private LexEVSRelsToGraphDao rels2graph;
	private LgLoggerIF logger;
	
	
	@Override
	public void loadGraphsForTerminologyURIAndVersion(String uri, String version) {
		try {
			if (version == null) {
				loadGraphsForTerminolgyProductionTerminologyUri(uri);
			}
			List<String> assos = rels2graph.getSupportedAssociationNamesForScheme(uri, version);
			assos.stream().forEach(associationName -> loadGraph(associationName, uri, version));
			rels2graph.getGraphSourceMgr().getDataSource(uri).getArangoDb().shutdown();
		} catch (Exception e) {
			rels2graph.getGraphSourceMgr().getDataSource(uri).getArangoDb().shutdown();
		}
	}

	@Override
	public void loadGraphsForTerminolgyProductionTerminologyUri(String uri) {
		loadGraphsForTerminologyURIAndVersion(uri, getVersionForProductionTaggedTerminology(uri));
	}
	
	private void loadGraph(String graphName, String uri, String version) {
		long start = System.currentTimeMillis();
		List<Triple> triples = rels2graph.getValidTriplesForAssociationNames(graphName, uri, version);
		System.out.println("Starting load of : " + triples.size() + " edges for graph " + graphName);
		ArangoDatabase db = rels2graph.getGraphSourceMgr().getDataSource(uri).getDbInstance();
		GraphEntity graph = rels2graph.createGraphFromDataBaseAndCollections(db, graphName,
				rels2graph.getAssociationEdgeNameForRow(graphName), rels2graph.getVertexCollectionName(graphName));
		triples.stream().forEach(triple -> rels2graph.processEdgeAndVertexToGraphDb(triple, graphName, db));
		System.out.println("Load Time including edge retrieval from source: "
				+ ((System.currentTimeMillis() - start) / 1000) + " seconds\n");
	}
	
	private String getVersionForProductionTaggedTerminology(final String uri) {
		try {
			return LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalVersionStringForTag(uri,
					"PRODUCTION");
		} catch (LBParameterException e) {
			logger.error("There was a problem getting a version" 
					+ " string for the PRODUCTION tagged "
					+ "terminology identified by url: " + uri);
			throw new RuntimeException("There was a problem getting a version"
					+ " string for the PRODUCTION tagged "
					+ "terminology identified by url: " + uri);
		}

	}

	/**
	 * @return the rels2graph
	 */
	@Override
	public LexEVSRelsToGraphDao getRels2graph() {
		return rels2graph;
	}

	/**
	 * @param rels2graph the rels2graph to set
	 */
	public void setRels2graph(LexEVSRelsToGraphDao rels2graph) {
		this.rels2graph = rels2graph;
	}

	/**
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}


}
