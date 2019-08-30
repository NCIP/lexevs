package edu.mayo.informatics.lexgrid.convert.directConversions.graphdb;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.naming.SupportedAssociation;
import org.lexevs.dao.database.access.association.model.AssociationRow;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.locator.LexEvsServiceLocator;

import com.arangodb.entity.EdgeEntity;

public class LexEVSRelsToGraph {
    String codingSchemeUri;
    String version;
    
    public LexEVSRelsToGraph(String uri, String version){
        this.codingSchemeUri = uri;
        this.version = version;
    }
    
    public List<String> getSupportedAssociationNamesForScheme(){

        return LexEvsServiceLocator.getInstance()
                .getDatabaseServiceManager()
                .getCodedNodeGraphService()
                .getAssociationPredicateNamesForCodingScheme(codingSchemeUri, version, null);
    }
    
    public List<Triple> getRootsForAssociationNames(String association){
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

    public List<AssociationRow> getRowsforRoots(List<String> roots, String assocName){
        return null;
    }
    
    public void storeEdgesAndVertexes(List<AssociationRow> rows){
       
    }
    
    public EdgeEntity storeEdge(AssociationRow row){
        return null;
    }
}
