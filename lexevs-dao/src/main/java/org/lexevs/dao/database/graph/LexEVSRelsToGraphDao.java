package org.lexevs.dao.database.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.access.association.model.NodeEdge;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.datasource.ErrorReportingGraphDbDataSourceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.ArangoVertexCollection;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.GraphEntity;
import com.arangodb.entity.VertexEntity;
import com.arangodb.model.GraphCreateOptions;

public class LexEVSRelsToGraphDao implements InitializingBean {

	private ErrorReportingGraphDbDataSourceManager graphSourceMgr;
	private LgLoggerIF logger;
	private static final String VERTEX_COLLECTION_PREFIX = "V_";
	private static final String EDGE_COLLECTION_PREFIX = "E_";
	
	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("Starting Graph Dao");
	}

	public List<String> getSupportedAssociationNamesForScheme(String uri, String version) {

		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService()
				.getAssociationPredicateNamesForCodingScheme(uri, version, null);
	}

	public List<Triple> getValidTriplesForAssociationNames(String association, String codingSchemeUri, String version) {
		List<String> associationNames = new ArrayList<String>();
		associationNames.add(association);
		List<String> uids = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService()
				.getAssociationPredicateUidsForNames(codingSchemeUri, version, null, associationNames);
		String uid = uids.get(0);
		return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService()
				.getValidTriplesOfAssociation(codingSchemeUri, version, uid);
	}

	public void processEdgeAndVertexToGraphDb(Triple row, String associationName, ArangoDatabase db) {
		LexVertex A = new LexVertex(row.getSourceEntityCode(), row.getSourceEntityNamespace());
		LexVertex B = new LexVertex(row.getTargetEntityCode(), row.getTargetEntityNamespace());
		ArangoVertexCollection collection = db.graph(associationName)
				.vertexCollection(getVertexCollectionName(associationName));
		VertexEntity Aa = collection.getVertex(A.getCode(), VertexEntity.class);
		VertexEntity Bb = collection.getVertex(B.getCode(), VertexEntity.class);
		if (Aa == null) {
			Aa = storeVertex(A, db, associationName, getVertexCollectionName(associationName));
		}
		if (Bb == null) {
			Bb = storeVertex(B, db, associationName, getVertexCollectionName(associationName));
		}

		if (Aa == null || Bb == null) {
			return;
		}
		storeEdge(new NodeEdge(Aa.getId(), Bb.getId(), false, true, associationName), db, associationName,
				getAssociationEdgeNameForRow(associationName));
	}

	public GraphEntity createGraphFromDataBaseAndCollections(ArangoDatabase db, String associationName,
			String edgeCollectionName, String vertexCollectionName) {
		final EdgeDefinition edgeDefinition = new EdgeDefinition()
				.collection(edgeCollectionName)
				.from(vertexCollectionName)
				.to(vertexCollectionName);
		return db.createGraph(associationName, Arrays.asList(edgeDefinition), null);
	}

	public String getVertexCollectionName(String associationName) {
		return (VERTEX_COLLECTION_PREFIX + associationName).length() > 64 ? 
				(VERTEX_COLLECTION_PREFIX + associationName).substring(0, 63) :
					VERTEX_COLLECTION_PREFIX + associationName;
	}

	public String getAssociationEdgeNameForRow(String associationName) {
		return (EDGE_COLLECTION_PREFIX + associationName).length() > 64 ? 
				(EDGE_COLLECTION_PREFIX + associationName).substring(0, 63) :
					EDGE_COLLECTION_PREFIX + associationName;
	}

	private void storeEdge(NodeEdge edge, ArangoDatabase db, String graphName, String edgeCollectionName) {
		db.graph(graphName).edgeCollection(edgeCollectionName).insertEdge(edge);
	}

	private VertexEntity storeVertex(LexVertex vertex, ArangoDatabase db, String graphName,
			String vertexCollectionName) {
		return db.graph(graphName).vertexCollection(vertexCollectionName).insertVertex(vertex);
	}

	public ArangoDatabase getDataBaseConnectionForScheme(String nameOrUri, String version) {
		return graphSourceMgr.getDataSource(nameOrUri).getDbInstance();
	}

	/**
	 * @return the graphSourceMgr
	 */
	public ErrorReportingGraphDbDataSourceManager getGraphSourceMgr() {
		return graphSourceMgr;
	}

	/**
	 * @param graphSourceMgr
	 *            the graphSourceMgr to set
	 */
	public void setGraphSourceMgr(ErrorReportingGraphDbDataSourceManager graphSourceMgr) {
		this.graphSourceMgr = graphSourceMgr;
	}

	/**
	 * @return the logger
	 */
	public LgLoggerIF getLogger() {
		return logger;
	}

	/**
	 * @param logger
	 *            the logger to set
	 */
	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

}
