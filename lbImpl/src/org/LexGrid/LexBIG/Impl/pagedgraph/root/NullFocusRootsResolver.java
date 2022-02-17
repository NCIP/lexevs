
package org.LexGrid.LexBIG.Impl.pagedgraph.root;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations.TraverseAssociations;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.database.utility.DaoUtility.SortContainer;
import org.lexevs.locator.LexEvsServiceLocator;

public class NullFocusRootsResolver implements RootsResolver {

    private static final long serialVersionUID = 9219617549685434658L;


    @Override
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String version, 
            String containerName,
            ResolveDirection direction, 
            GraphQuery graphQuery,
            SortOptionList sortOptionList) {
        return this.resolveRoots(codingSchemeUri, version, containerName, direction, graphQuery, sortOptionList, 0, -1);
    }
    
    
    @Override
    public List<ConceptReference> resolveRoots(
            String codingSchemeUri, 
            String codingSchemeVersion, 
            String relationsContainerName,
            ResolveDirection direction,
            GraphQuery query,
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize) {
        List<ConceptReference> returnList = new ArrayList<ConceptReference>();
        
        if(direction.equals(ResolveDirection.FORWARD)) {
            if(CollectionUtils.isNotEmpty(query.getRestrictToSourceCodes())) {
                return getSubList(query.getRestrictToSourceCodes(), currentPosition, pageSize);
            }
            
            if(CollectionUtils.isNotEmpty(query.getRestrictToTargetCodes())) {
                return this.getRelatedSourceCodes(
                        codingSchemeUri, 
                        codingSchemeVersion, 
                        relationsContainerName,
                        query, 
                        sortOptionList,
                        currentPosition,
                        pageSize);
            }
            
           return this.getRoots(
                   codingSchemeUri, 
                   codingSchemeVersion, 
                   relationsContainerName, 
                   query.getRestrictToAssociations(),
                   query.getRestrictToAssociationsQualifiers(),
                   query.getRestrictToSourceCodeSystem(),
                   query.getRestrictToTargetCodeSystem(),
                   sortOptionList,
                   currentPosition,
                   pageSize);
        }
        
        if(direction.equals(ResolveDirection.BACKWARD)) {
            if(CollectionUtils.isNotEmpty(query.getRestrictToTargetCodes())) {
                return getSubList(query.getRestrictToTargetCodes(), currentPosition, pageSize);
            }
            
            if(CollectionUtils.isNotEmpty(query.getRestrictToSourceCodes())) {
                return this.getRelatedTargetCodes(
                        codingSchemeUri, 
                        codingSchemeVersion, 
                        relationsContainerName,
                        query,
                        sortOptionList,
                        currentPosition,
                        pageSize);
            }
            
            return this.getTails(
                    codingSchemeUri, 
                    codingSchemeVersion, 
                    relationsContainerName, 
                    query.getRestrictToAssociations(),
                    query.getRestrictToAssociationsQualifiers(),
                    query.getRestrictToSourceCodeSystem(),
                    query.getRestrictToTargetCodeSystem(),
                    sortOptionList,
                    currentPosition,
                    pageSize);
        }
        
        return returnList;
    }
    
    private List<ConceptReference> getSubList(List<ConceptReference> codes, int currentPosition, int pageSize) {
        if(codes == null || codes.size() == 0 || codes.size() < currentPosition){
            return codes;
        }
        
        List<ConceptReference> returnList = new ArrayList<ConceptReference>();
        for(int i=currentPosition;i<currentPosition + pageSize && i<codes.size();i++){
            returnList.add(codes.get(i));
        }
        
        return returnList;
    }


    @Override
    public boolean isRootOrTail(ConceptReference ref) {
       return isRefRootOrTail(ref);
    }
    
    public static boolean isRefRootOrTail(ConceptReference ref) {
        boolean isRootOrTail = (ref.getCode().equals(AbstractEndNode.ROOT) || ref.getCode().equals(AbstractEndNode.TAIL));
        
        return isRootOrTail;
    }
    
    protected List<ConceptReference> getRelatedTargetCodes(
            final String  codingSchemeUri, 
            final String codingSchemeVersion,
            final String containerName,
            final GraphQuery query,
            SortOptionList sortOptionList,
            final int currentPosition,
            final int pageSize){
         
        final SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        return LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
                getDaoCallbackService().
                    executeInDaoLayer(new DaoCallback<List<ConceptReference>>() {
            
                        @Override
                        public List<ConceptReference> execute(DaoManager daoManager) {
                            String codingSchemeUid = 
                                daoManager.getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
                                    getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
                            
                            return daoManager.getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
                                getConceptReferencesContainingSubject(
                                        codingSchemeUid, 
                                        containerName, 
                                        query.getRestrictToSourceCodes(),
                                        query.getRestrictToAssociations(),
                                        query.getRestrictToAssociationsQualifiers(), 
                                        DaoUtility.toCodeNamespacePair(query.getRestrictToTargetCodes()),
                                        query.getRestrictToTargetCodeSystem(), 
                                        query.getRestrictToEntityTypes(), 
                                        query.isRestrictToAnonymous(),
                                        sortContainer.getSorts(),
                                        currentPosition, 
                                        pageSize);
                        }
        });
    }
    
    protected List<ConceptReference> getRelatedSourceCodes(
            final String  codingSchemeUri, 
            final String codingSchemeVersion,
            final String containerName,
            final GraphQuery query, 
            SortOptionList sortOptionList,
            final int currentPosition,
            final int pageSize){
        
        final SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
       
        return LexEvsServiceLocator.getInstance().
            getDatabaseServiceManager().
                getDaoCallbackService().
                    executeInDaoLayer(new DaoCallback<List<ConceptReference>>() {
            
                        @Override
                        public List<ConceptReference> execute(DaoManager daoManager) {
                            String codingSchemeUid = 
                                daoManager.getCodingSchemeDao(codingSchemeUri, codingSchemeVersion).
                                    getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
                            
                            return daoManager.getCodedNodeGraphDao(codingSchemeUri, codingSchemeVersion).
                                getConceptReferencesContainingObject(
                                        codingSchemeUid, 
                                        containerName, 
                                        query.getRestrictToTargetCodes(),
                                        query.getRestrictToAssociations(),
                                        query.getRestrictToAssociationsQualifiers(), 
                                        DaoUtility.toCodeNamespacePair(query.getRestrictToSourceCodes()),
                                        query.getRestrictToSourceCodeSystem(), 
                                        query.getRestrictToEntityTypes(), 
                                        query.isRestrictToAnonymous(),
                                        sortContainer.getSorts(),
                                        currentPosition, 
                                        pageSize);
                        }
        });
    }
    
    protected List<ConceptReference> getRoots(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName,
            List<String> associationNames,
            List<QualifierNameValuePair> qualifiers,
            List<String> subjectEntityCodeNamespaces,
            List<String> objectEntityCodeNamespaces,
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize){
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        TraverseAssociations traverseAssociations;
        
        if(CollectionUtils.isEmpty(associationNames)) {
            associationNames = 
            service.getAssociationPredicateNamesForCodingScheme(codingSchemeUri, codingSchemeVersion, relationsContainerName);
            traverseAssociations = TraverseAssociations.TOGETHER;
        } else {
           traverseAssociations = TraverseAssociations.TOGETHER;
        }
        
        SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        List<ConceptReference> roots = 
            service.getRootConceptReferences(
            		codingSchemeUri, 
            		codingSchemeVersion, 
            		relationsContainerName, 
            		associationNames, 
            		qualifiers,
            		subjectEntityCodeNamespaces,
            		objectEntityCodeNamespaces,
            		traverseAssociations, 
            		sortContainer.getSorts(), 
            		currentPosition, 
                    pageSize);
        
        return roots;
    }
    
    protected List<ConceptReference> getTails(
            String  codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName,
            List<String> associationNames,
            List<QualifierNameValuePair> qualifiers,
            List<String> subjectEntityCodeNamespaces,
            List<String> objectEntityCodeNamespaces,
            SortOptionList sortOptionList,
            int currentPosition, 
            int pageSize){
        
        CodedNodeGraphService service =
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        TraverseAssociations traverseAssociations;
        
        if(CollectionUtils.isEmpty(associationNames)) {
            associationNames = 
            service.getAssociationPredicateNamesForCodingScheme(codingSchemeUri, codingSchemeVersion, relationsContainerName);
            traverseAssociations = TraverseAssociations.TOGETHER;
        } else {
        	 traverseAssociations = TraverseAssociations.TOGETHER;
        }
        
        SortContainer sortContainer = DaoUtility.mapSortOptionListToSort(sortOptionList);
        
        return 
            service.getTailConceptReferences(
            		codingSchemeUri, 
            		codingSchemeVersion, 
            		relationsContainerName, 
            		associationNames, 
            		qualifiers,
            		subjectEntityCodeNamespaces,
            		objectEntityCodeNamespaces,
            		traverseAssociations,
            		sortContainer.getSorts(), 
                    currentPosition, 
                    pageSize);
    }
}