package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.AbstractEndNode.Root;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;

public class NullFocusRootsResolver implements RootsResolver {

    @Override
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            ResolveDirection direction,
            GraphQuery query) {
        List<ConceptReference> returnList = new ArrayList<ConceptReference>();
        
        if(direction.equals(ResolveDirection.FORWARD)) {
            if(CollectionUtils.isNotEmpty(query.getRestrictToSourceCodes())) {
                return query.getRestrictToSourceCodes();
            }
            
            if(CollectionUtils.isNotEmpty(query.getRestrictToTargetCodes())) {
                return this.getRelatedSourceCodes(codingSchemeUri, codingSchemeVersion, query);
            }
            
            return DaoUtility.createList(ConceptReference.class, new Root(codingSchemeUri, codingSchemeVersion));
        }
        
        return returnList;
    }
    
    public boolean isRootOrTail(ConceptReference ref) {
        return (ref.getCode().equals(AbstractEndNode.ROOT) || ref.getCode().equals(AbstractEndNode.TAIL));
    }
    
    protected List<ConceptReference> getRelatedTargetCodes(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            GraphQuery query){
         
        List<String> uids = new ArrayList<String>();
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        for(ConceptReference ref : query.getRestrictToSourceCodes()) {
            uids.addAll( 
            service.
            getTripleUidsContainingSubject(
                    codingSchemeUri, 
                    codingSchemeVersion, 
                    null, 
                    null, 
                    ref.getCode(), 
                    ref.getCodeNamespace(), 
                    query, 
                    0, 
                    -1));
        }
        
        return service.getConceptReferencesFromUidTarget(codingSchemeUri, codingSchemeVersion, uids);
    }
    
    protected List<ConceptReference> getRelatedSourceCodes(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            GraphQuery query){
         
        List<String> uids = new ArrayList<String>();
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        for(ConceptReference ref : query.getRestrictToTargetCodes()) {
            uids.addAll( 
            service.
            getTripleUidsContainingObject(
                    codingSchemeUri, 
                    codingSchemeVersion, 
                    null, 
                    null, 
                    ref.getCode(), 
                    ref.getCodeNamespace(), 
                    query, 
                    0, 
                    -1));
        }
        
        return service.getConceptReferencesFromUidSource(codingSchemeUri, codingSchemeVersion, uids);
    }


}
