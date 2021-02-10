
package org.LexGrid.LexBIG.Impl.pagedgraph.builder;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.comparator.ResultComparator;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandler;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandlerFactory;
import org.LexGrid.LexBIG.Impl.pagedgraph.model.LazyLoadableAssociatedConceptList;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.PagedGraphUtils;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.springframework.util.Assert;

/**
 * The Class AssociationListBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationListBuilder implements Serializable{
    
    private static final long serialVersionUID = 2754244213732834688L;
    
    /** The associated concept page size. */
    private int associatedConceptPageSize = 100;

    /**
     * The Enum AssociationDirection.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public enum AssociationDirection {
        SOURCE_OF,
        TARGET_OF}
    
    /** The database service manager. */
    private DatabaseServiceManager getDatabaseServiceManager() {
        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
    }
    
    /**
     * Builds the source of association list.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * @param relationsContainerName the relations container name
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveForwardAssociationDepth the resolve forward association depth
     * @param resolveBackwardAssociationDepth the resolve backward association depth
     * @param resolveCodedEntryDepth the resolve coded entry depth
     * @param graphQuery the graph query
     * @param filterOptions 
     * @param cycleDetectingCallback the cycle detecting callback
     * 
     * @return the association list
     */
    public AssociationList buildSourceOfAssociationList(
            String codingSchemeUri,
            String version,
            String entityCode,
            String entityCodeNamespace,
            String relationsContainerName,
            boolean resolveForward,
            boolean resolveBackward,
            int resolveForwardAssociationDepth,
            int resolveBackwardAssociationDepth,
            int resolveCodedEntryDepth,
            GraphQuery graphQuery,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortAlgorithms,
            LocalNameList filterOptions, 
            CycleDetectingCallback cycleDetectingCallback) {
        return this.doBuildAssociationList(
                codingSchemeUri, 
                version, 
                entityCode, 
                entityCodeNamespace, 
                relationsContainerName,
                resolveForward,
                resolveBackward,
                resolveForwardAssociationDepth,
                resolveBackwardAssociationDepth,
                resolveCodedEntryDepth,
                graphQuery,
                propertyNames, 
                propertyTypes,
                sortAlgorithms,
                filterOptions,
                cycleDetectingCallback,
                AssociationDirection.SOURCE_OF);
    }
    
    /**
     * Builds the target of association list.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * @param relationsContainerName the relations container name
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveForwardAssociationDepth the resolve forward association depth
     * @param resolveBackwardAssociationDepth the resolve backward association depth
     * @param resolveCodedEntryDepth the resolve coded entry depth
     * @param graphQuery the graph query
     * @param cycleDetectingCallback the cycle detecting callback
     * 
     * @return the association list
     */
    public AssociationList buildTargetOfAssociationList(
                String codingSchemeUri,
                String version,
                String entityCode,
                String entityCodeNamespace,
                String relationsContainerName,
                boolean resolveForward,
                boolean resolveBackward,
                int resolveForwardAssociationDepth,
                int resolveBackwardAssociationDepth,
                int resolveCodedEntryDepth,
                GraphQuery graphQuery,
                LocalNameList propertyNames, 
                PropertyType[] propertyTypes, 
                SortOptionList sortAlgorithms,
                LocalNameList filterOptions, 
                CycleDetectingCallback cycleDetectingCallback) {
        return this.doBuildAssociationList(
                codingSchemeUri, 
                version, 
                entityCode, 
                entityCodeNamespace, 
                relationsContainerName,
                resolveForward,
                resolveBackward,
                resolveForwardAssociationDepth,
                resolveBackwardAssociationDepth,
                resolveCodedEntryDepth,
                graphQuery,
                propertyNames,
                propertyTypes,
                sortAlgorithms,
                filterOptions,
                cycleDetectingCallback,
                AssociationDirection.TARGET_OF); }
    
    
    /**
     * Do build association list.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param entityCode the entity code
     * @param entityCodeNamespace the entity code namespace
     * @param direction the direction
     * @param relationsContainerName the relations container name
     * @param resolveForward the resolve forward
     * @param resolveBackward the resolve backward
     * @param resolveForwardAssociationDepth the resolve forward association depth
     * @param resolveBackwardAssociationDepth the resolve backward association depth
     * @param resolveCodedEntryDepth the resolve coded entry depth
     * @param graphQuery the graph query
     * @param filterOptions 
     * @param cycleDetectingCallback the cycle detecting callback
     * 
     * @return the association list
     */
    protected AssociationList doBuildAssociationList(
            String codingSchemeUri,
            String version,
            String entityCode,
            String entityCodeNamespace,
            String relationsContainerName,
            boolean resolveForward,
            boolean resolveBackward,
            int resolveForwardAssociationDepth,
            int resolveBackwardAssociationDepth,
            int resolveCodedEntryDepth,
            GraphQuery graphQuery,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortAlgorithms,
            LocalNameList filterOptions, 
            CycleDetectingCallback cycleDetectingCallback,
            AssociationDirection direction) {
        Assert.notNull(graphQuery, "Must pass in a GraphQuery.");
        
        boolean isValidFocus = PagedGraphUtils.checkFocus(
                codingSchemeUri, 
                version, 
                relationsContainerName, 
                entityCode,
                entityCodeNamespace,
                resolveForward, 
                resolveBackward, 
                graphQuery);
        
        if(!isValidFocus) {
            return null;
        }

        CodedNodeGraphService codedNodeGraphService =
            getDatabaseServiceManager().getCodedNodeGraphService();

        AssociationList returnList = new AssociationList();

        for(String currentRelationContainer : this.getRelationContainers(
                codingSchemeUri,
                version,
                relationsContainerName)) {

            Map<String,Integer> tripleUidsAndCount;
            if(direction.equals(AssociationDirection.SOURCE_OF)) {
                tripleUidsAndCount = codedNodeGraphService.getTripleUidsContainingSubjectCount(
                        codingSchemeUri, 
                        version, 
                        currentRelationContainer, 
                        entityCode, 
                        entityCodeNamespace, 
                        graphQuery);
            } else {
                tripleUidsAndCount = codedNodeGraphService.getTripleUidsContainingObjectCount(
                        codingSchemeUri, 
                        version, 
                        currentRelationContainer, 
                        entityCode, 
                        entityCodeNamespace, 
                        graphQuery);
            }

            for(String associationPredicateName : tripleUidsAndCount.keySet()) {
                int tripleUidsCount = tripleUidsAndCount.get(associationPredicateName);
                if(tripleUidsCount > 0) {
                    Association association = new Association();

                    association.setAssociationName(associationPredicateName);
                    association.setRelationsContainerName(currentRelationContainer);

                    AssociatedConceptList associatedConceptList =
                        new LazyLoadableAssociatedConceptList(
                                tripleUidsCount,
                                codingSchemeUri,
                                version,
                                currentRelationContainer,
                                associationPredicateName, 
                                entityCode, 
                                entityCodeNamespace, 
                                resolveForward,
                                resolveBackward,
                                resolveForwardAssociationDepth,
                                resolveBackwardAssociationDepth,
                                resolveCodedEntryDepth,
                                graphQuery,
                                propertyNames, 
                                propertyTypes, 
                                sortAlgorithms,
                                filterOptions,
                                cycleDetectingCallback,
                                direction,
                                associatedConceptPageSize);

                    association.setAssociatedConcepts(associatedConceptList);

                    returnList.addAssociation(association);
                }
            }
        }
        
        if(returnList.getAssociationCount() == 0) {
            String adjustedCodingSchemeUri;
            String adjustedCodingSchemeVersion;

            try {
                AbsoluteCodingSchemeVersionReference ref = this.getNamespaceHandler().getCodingSchemeForNamespace(
                        codingSchemeUri, 
                        version, 
                        entityCodeNamespace);
                if(ref == null) {
                    return returnList;
                }
                
                adjustedCodingSchemeUri = ref.getCodingSchemeURN();
                adjustedCodingSchemeVersion = ref.getCodingSchemeVersion();
  
            } catch (LBParameterException e) {
                LoggerFactory.getLogger().info("Cannot map namespace: " +  entityCodeNamespace);

                return returnList;
            }

            if(! adjustedCodingSchemeUri.equals(codingSchemeUri) ||
                    ! adjustedCodingSchemeVersion.endsWith(version)) {
                return this.doBuildAssociationList(
                        adjustedCodingSchemeUri, 
                        adjustedCodingSchemeVersion, 
                        entityCode, 
                        entityCodeNamespace, 
                        null, 
                        resolveForward, 
                        resolveBackward, 
                        resolveForwardAssociationDepth, 
                        resolveBackwardAssociationDepth, 
                        resolveCodedEntryDepth,
                        graphQuery, 
                        propertyNames, 
                        propertyTypes, 
                        sortAlgorithms, 
                        filterOptions, 
                        cycleDetectingCallback, 
                        direction);
            }
        }

        if(ResultComparator.isSortOptionListValid(sortAlgorithms)) {
            ResultComparator<Association> comparator = 
                new ResultComparator<Association>(sortAlgorithms, Association.class);

            Association[] array = returnList.getAssociation();
            Arrays.sort(array, comparator);

            returnList.setAssociation(array);
        }

        if(returnList.getAssociationCount() == 0) {
            return null;
        } else {
            return returnList;
        }
    }
    
    protected List<String> getRelationContainers(
            String codingSchemeUri, 
            String codingSchemeVersion,
            String relationContainer){
        
        if(StringUtils.isNotBlank(relationContainer)) {
            return Arrays.asList(relationContainer);
        }
        return
            getDatabaseServiceManager().getCodedNodeGraphService().getRelationNamesForCodingScheme(codingSchemeUri, codingSchemeVersion);
    }

    /**
     * Gets the association predicate names.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param codingSchemeVersion the coding scheme version
     * 
     * @return the association predicate names
     */
    protected List<String> getAssociationPredicateNames(
            String codingSchemeUri, 
            String codingSchemeVersion,
            String relationsContainerName) {
        CodedNodeGraphService codedNodeGraphService =
            getDatabaseServiceManager().getCodedNodeGraphService();

        return codedNodeGraphService.getAssociationPredicateNamesForCodingScheme(
                codingSchemeUri,
                codingSchemeVersion,
                relationsContainerName);
    }
    
    private NamespaceHandler getNamespaceHandler() {
        return NamespaceHandlerFactory.getNamespaceHandler();
    }
}