package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedAssociation;
import org.lexevs.dao.database.access.association.model.AssociationRow;
import org.lexevs.dao.database.access.association.model.LexVertex;
import org.lexevs.dao.database.access.association.model.NodeEdge;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.locator.LexEvsServiceLocator;

import com.arangodb.ArangoDatabase;
import com.arangodb.ArangoVertexCollection;
import com.arangodb.entity.EdgeEntity;
import com.arangodb.entity.VertexEntity;

public class LexEVSRelsToGraph {
    String codingSchemeUri;
    String version;
    ArangoDatabase db;
    
    public LexEVSRelsToGraph(String uri, String version){
        this.codingSchemeUri = uri;
        this.version = version;
        this.db = getDataBaseConnectionForScheme(uri,version);
    }
    
    public List<String> getSupportedAssociationNamesForScheme(){

        return LexEvsServiceLocator.getInstance()
                .getDatabaseServiceManager()
                .getCodedNodeGraphService()
                .getAssociationPredicateNamesForCodingScheme(codingSchemeUri, version, null);
    }
    
    public List<Triple> getValidTriplesForAssociationNames(String association){
       List<String> associationNames = new ArrayList<String>();
       associationNames.add(association);
    List<String> uids = LexEvsServiceLocator.getInstance()
            .getDatabaseServiceManager().getCodedNodeGraphService()
            .getAssociationPredicateUidsForNames(
                    this.codingSchemeUri, this.version, null, associationNames );
    String uid = uids.get(0);
    return LexEvsServiceLocator.getInstance()
    .getDatabaseServiceManager().getCodedNodeGraphService()
    .getValidTriplesOfAssociation(
            this.codingSchemeUri, this.version,uid);
    }

    
    public void procesEdgeAndVertexeToGraphDb(Triple row){
        LexVertex A = new LexVertex(row.getSourceEntityCode(), row.getSourceEntityNamespace());
        LexVertex B = new LexVertex(row.getTargetEntityCode(), row.getTargetEntityNamespace());
        ArangoVertexCollection collection = db.graph(getAssociationEdgeNameForRow(row.getAssociationPredicateId())).vertexCollection(getVertexCollectionName(row.getAssociationPredicateId()));
        VertexEntity Aa = collection.getVertex(A.getCode(), VertexEntity.class);
        VertexEntity Bb = collection.getVertex(B.getCode(), VertexEntity.class) ;
        if(Aa == null){
            Aa = storeVertex(A);
        }
        
        if(Bb == null){
            Bb = storeVertex(B);
        }

        if(Aa == null || Bb == null){return;}
        storeEdge(new NodeEdge(Aa.getId(), Bb.getId(), false, true,
                row.getAssociationPredicateId()));
    }
    
    private String getVertexCollectionName(String associationPredicateId) {
        // TODO Auto-generated method stub
        return null;
    }

    private String getAssociationEdgeNameForRow(String associationPredicateId) {
        // TODO Auto-generated method stub
        return null;
    }

    public EdgeEntity storeEdge(NodeEdge edge){
        return null;
    }
    
    public VertexEntity storeVertex(LexVertex vertex){
        return null;
    }
    
    public ArangoDatabase getDataBaseConnectionForScheme(String nameOrUri, String version){
        return null;
    }
}
