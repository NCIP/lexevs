package org.lexevs.dao.database.service.graphdb;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.access.association.model.NodeEdge;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.operation.LexEVSRelsToGraph;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.Logger;
import org.lexevs.logging.LoggerFactory;

import com.arangodb.ArangoDatabase;

public class GraphingDataBaseServiceImpl implements GraphingDataBaseService {
	LexEVSRelsToGraph rels2graph;
	
	@Override
	public void insertEdge(NodeEdge edge) {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertVertex(LexVertex vertex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getGraphingDataBaseForURI(String uri) {
		// TODO Auto-generated method stub

	}
	
	public void loadGraphsForTerminologyURIAndVersion(String uri, String version){
		if(version == null){
			loadGraphsForTerminolgyProductionTerminologyUri(uri);
		}
		List<String> assos = rels2graph.getSupportedAssociationNamesForScheme(uri, version);
		assos.parallelStream().forEach(associationName -> loadGraph(associationName, uri, version));
	}

	private void loadGraphsForTerminolgyProductionTerminologyUri(String uri) {
		loadGraphsForTerminologyURIAndVersion(uri, getVersionForProductionTaggedTerminology(uri));
	}
	
	private void loadGraph(String graphName, String uri, String version){
		List<Triple> triples = rels2graph.getValidTriplesForAssociationNames(graphName, uri, version);
		ArangoDatabase db = rels2graph.getGraphSourceMgr().getDataSource(uri).getDbInstance();
		triples.stream().forEach(triple -> rels2graph.processEdgeAndVertexToGraphDb(triple, graphName, db));		
	}
	
	private String getVersionForProductionTaggedTerminology(final String uri){
		try {
			return LexEvsServiceLocator.
					getInstance()
					.getSystemResourceService()
					.getInternalVersionStringForTag(uri, "PRODUCTION");
		} catch (LBParameterException e) {
			getLogger().error("There was a problem getting a version"
					+ " string for the PRODUCTION tagged "
					+ "terminology identified by url: " + uri);
			throw new RuntimeException("There was a problem getting a version"
					+ " string for the PRODUCTION tagged "
					+ "terminology identified by url: " + uri);
		}		
		
	}

	private LgLoggerIF getLogger() {
		return LoggerFactory.getLogger();
	}

}
