package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.locator.LexEvsServiceLocator;

public class NullFocusRootsResolver implements RootsResolver {

    @Override
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
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
            
           return this.getRoots(codingSchemeUri, codingSchemeVersion, relationsContainerName, query.getRestrictToAssociations());
        }
        
        if(direction.equals(ResolveDirection.BACKWARD)) {
            if(CollectionUtils.isNotEmpty(query.getRestrictToTargetCodes())) {
                return query.getRestrictToTargetCodes();
            }
            
            if(CollectionUtils.isNotEmpty(query.getRestrictToSourceCodes())) {
                return this.getRelatedTargetCodes(codingSchemeUri, codingSchemeVersion, query);
            }
            
            return this.getTails(codingSchemeUri, codingSchemeVersion, relationsContainerName, query.getRestrictToAssociations());
        }
        
        return returnList;
    }
    
    public boolean isRootOrTail(ConceptReference ref) {
        boolean isRootOrTail = (ref.getCode().equals(AbstractEndNode.ROOT) || ref.getCode().equals(AbstractEndNode.TAIL));
        
        return isRootOrTail;
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
    
    protected List<ConceptReference> getRoots(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName,
            List<String> associationNames){
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        if(CollectionUtils.isEmpty(associationNames)) {
            associationNames = 
            service.getAssociationPredicateNamesForCodingScheme(codingSchemeUri, codingSchemeVersion, relationsContainerName);
        }
        
        List<ConceptReference> roots = 
            service.getRootConceptReferences(codingSchemeUri, codingSchemeVersion, relationsContainerName, associationNames, TraverseAssociations.INDIVIDUALLY);
        
        return roots;
    }
    
    protected List<ConceptReference> getTails(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName,
            List<String> associationNames){
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        if(CollectionUtils.isEmpty(associationNames)) {
            associationNames = 
            service.getAssociationPredicateNamesForCodingScheme(codingSchemeUri, codingSchemeVersion, relationsContainerName);
        }
        
        return 
            service.getTailConceptReferences(codingSchemeUri, codingSchemeVersion, relationsContainerName, associationNames, TraverseAssociations.INDIVIDUALLY);
    }
    
}
