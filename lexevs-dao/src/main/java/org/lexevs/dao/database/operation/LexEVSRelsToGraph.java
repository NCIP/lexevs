package org.lexevs.dao.database.operation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.access.association.model.NodeEdge;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.dao.database.datasource.ErrorReportingGraphDbDataSourceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.beans.factory.InitializingBean;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoGraph;
import com.arangodb.ArangoVertexCollection;
import com.arangodb.entity.EdgeDefinition;
import com.arangodb.entity.GraphEntity;
import com.arangodb.entity.VertexEntity;
import com.arangodb.model.GraphCreateOptions;

public class LexEVSRelsToGraph implements InitializingBean {
	
	ErrorReportingGraphDbDataSourceManager graphSourceMgr;

    
	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

    
    public List<String> getSupportedAssociationNamesForScheme(String uri, String version){

        return LexEvsServiceLocator.getInstance()
                .getDatabaseServiceManager()
                .getCodedNodeGraphService()
                .getAssociationPredicateNamesForCodingScheme(uri, version, null);
    }
    
    public List<Triple> getValidTriplesForAssociationNames(String association, String codingSchemeUri, String version){
       List<String> associationNames = new ArrayList<String>();
       associationNames.add(association);
    List<String> uids = LexEvsServiceLocator.getInstance()
            .getDatabaseServiceManager().getCodedNodeGraphService()
            .getAssociationPredicateUidsForNames(
                    codingSchemeUri, version, null, associationNames );
    String uid = uids.get(0);
    return LexEvsServiceLocator.getInstance()
    .getDatabaseServiceManager().getCodedNodeGraphService()
    .getValidTriplesOfAssociation(
            codingSchemeUri, version, uid);
    }

    
    public void processEdgeAndVertexToGraphDb(Triple row, String associationName, ArangoDatabase db){
        LexVertex A = new LexVertex(row.getSourceEntityCode(), row.getSourceEntityNamespace());
        LexVertex B = new LexVertex(row.getTargetEntityCode(), row.getTargetEntityNamespace());
        ArangoVertexCollection collection = db.graph(getAssociationEdgeNameForRow(
        		row.getAssociationPredicateId(), db))
        		.vertexCollection(getVertexCollectionName(
        				row.getAssociationPredicateId(), db));
        VertexEntity Aa = collection.getVertex(A.getCode(), VertexEntity.class);
        VertexEntity Bb = collection.getVertex(B.getCode(), VertexEntity.class) ;
        if(Aa == null){
            Aa = storeVertex(A, db, db.name(), getVertexCollectionName( row.getAssociationPredicateId(), db));
        }
        if(Bb == null){
            Bb = storeVertex(B, db, db.name(), getVertexCollectionName( row.getAssociationPredicateId(), db));
        }

        if(Aa == null || Bb == null){return;}
        storeEdge(new NodeEdge(Aa.getId(), Bb.getId(), false, true,
                row.getAssociationPredicateId()), 
        		db, 
        		db.name(), 
        		getAssociationEdgeNameForRow( row.getAssociationPredicateId(), db)
        		);
    }
    
    public GraphEntity createGraphFromDataBaseAndCollections(ArangoDatabase db, String associationName, String edgeCollectionName, String vertexCollectionName){
		final EdgeDefinition edgeDefinition = new EdgeDefinition().collection(edgeCollectionName)
				.from(vertexCollectionName).to(vertexCollectionName);
			ArangoGraph graph = db.graph(associationName);
			return graph.create(Arrays.asList(edgeDefinition), new GraphCreateOptions());
    }
    
    private String getVertexCollectionName(String associationName, ArangoDatabase db) {
       return null;
    }

    private String getAssociationEdgeNameForRow(String associationName,  ArangoDatabase db) {
        // TODO Auto-generated method stub
        return null;
    }

    private void storeEdge(NodeEdge edge, ArangoDatabase db, String graphName, String edgeCollectionName){
    	db.graph(graphName).edgeCollection(edgeCollectionName).insertEdge(edge);
    }
    
    private VertexEntity storeVertex(LexVertex vertex, ArangoDatabase db, String graphName, String vertexCollectionName){
		return db.graph(graphName).vertexCollection(vertexCollectionName).insertVertex(vertex);
    }
    
    public ArangoDatabase getDataBaseConnectionForScheme(String nameOrUri, String version){
        return graphSourceMgr.getDataSource(nameOrUri).getDbInstance();
    }


	/**
	 * @return the graphSourceMgr
	 */
	public ErrorReportingGraphDbDataSourceManager getGraphSourceMgr() {
		return graphSourceMgr;
	}


	/**
	 * @param graphSourceMgr the graphSourceMgr to set
	 */
	public void setGraphSourceMgr(ErrorReportingGraphDbDataSourceManager graphSourceMgr) {
		this.graphSourceMgr = graphSourceMgr;
	}




}
