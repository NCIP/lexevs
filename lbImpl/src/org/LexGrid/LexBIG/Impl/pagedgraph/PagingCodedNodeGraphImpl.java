
package org.LexGrid.LexBIG.Impl.pagedgraph;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandler;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandlerFactory;
import org.LexGrid.LexBIG.Impl.pagedgraph.builder.AssociationListBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.model.LazyLoadableResolvedConceptReferenceList;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.NullFocusRootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.root.RootsResolver;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.PagedGraphUtils;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.ValidatedParameterResolvingCallback;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;
import org.springframework.util.CollectionUtils;

/**
 * The Class PagingCodedNodeGraphImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PagingCodedNodeGraphImpl extends AbstractQueryBuildingCodedNodeGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1153282485482789848L;

    private RootsResolver rootsResolver = new NullFocusRootsResolver();
    
    public enum ArtificialRootResolvePolicy {RESOLVE_AS_IS, RESOLVE_CHILDREN, SKIP}
    
    private AssociationListBuilder associationListBuilder = new AssociationListBuilder();
    
    public PagingCodedNodeGraphImpl() {
        super();
    }         
    
    /**
     * Instantiates a new paging coded node graph impl.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @throws LBParameterException 
     */
    public PagingCodedNodeGraphImpl(
            String codingSchemeUri, 
            String version,
            String relationsContainerName) throws LBParameterException{
        super(codingSchemeUri, version, StringUtils.isBlank(relationsContainerName) ? null : relationsContainerName);
    }
    
  
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#resolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int)
     */
    @Override
    public ResolvedConceptReferenceList doResolveAsValidatedParameterList(
    		ConceptReference graphFocus, 
    		boolean resolveForward,
            boolean resolveBackward, 
            int resolveCodedEntryDepth, 
            int resolveAssociationDepth,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes, 
            SortOptionList sortOptions,
            LocalNameList filterOptions, 
            int maxToReturn,
            boolean keepLastAssociationLevelUnresolved,
            ArtificialRootResolvePolicy artificialRootResolvePolicy,
            CycleDetectingCallback cycleDetectingCallback) throws LBInvocationException, LBParameterException {
        
        artificialRootResolvePolicy = adjustArtificialRootResolvePolicy(
                resolveForward,
                resolveBackward,
                artificialRootResolvePolicy, 
                this.getGraphQueryBuilder().getQuery());
              
        String codingSchemeUri = this.getCodingSchemeUri();
        String version = this.getVersion();
        String relationsContainerName = this.getRelationsContainerName();
        GraphQueryBuilder graphQueryBuilder = this.getGraphQueryBuilder(); 
        
        Filter[] filters = ServiceUtility.validateFilters(filterOptions);
        
        if (graphFocus == null && resolveForward && resolveBackward) {
            throw new LBParameterException(
                    "If you do not provide a focus node, you must choose resolve forward or resolve reverse, not both."
                    + "  Choose resolve forward to start at root nodes.  Choose resolve reverse to start at tail nodes.");
        }
        
        ResolvedConceptReference focus = null;
        
        boolean needToValidateFocusExistsInGraph = true;
        
        if(graphFocus != null) {

            if(StringUtils.isNotBlank(graphFocus.getCodeNamespace())
                    &&
                    StringUtils.isNotBlank(graphFocus.getCodingSchemeName())){
                String codingSchemeName =
                    this.getNamespaceHandler().getCodingSchemeNameForNamespace(codingSchemeUri, version, graphFocus.getCodeNamespace());

                if(! StringUtils.equals(graphFocus.getCodingSchemeName(), codingSchemeName)){

                    return ServiceUtility.throwExceptionOrReturnDefault(
                            new LBParameterException("Based on the namespace provided as a focus (" + graphFocus.getCodeNamespace() + ")" +
                                    " there is no match to the provided Coding Scheme Name (" + graphFocus.getCodingSchemeName() + ")." +
                                    " If " + graphFocus.getCodeNamespace() + " is meant to be equivalent to the CodingScheme " + graphFocus.getCodingSchemeName() + ", " +
                                    " this must be declared in the SupportedNamespaces."),
                            new ResolvedConceptReferenceList(),
                            this.isStrictFocusValidation());
                }
            }
            
            //try to do a resolve based on the code and namespace we were given
            focus = attemptToResolveFocus(
                        codingSchemeUri, 
                        version,
                        graphFocus, 
                        this.shouldResolveNextLevel(resolveCodedEntryDepth),
                        propertyNames, 
                        propertyTypes);
            
            //if we didn't find it above, adjust the coding scheme based on the namespace, if possible
            if(focus == null) {
                if(graphFocus.getCodeNamespace() != null) {
                    AbsoluteCodingSchemeVersionReference ref = null;
                    try {
                        ref = this.getNamespaceHandler().getCodingSchemeForNamespace(codingSchemeUri, version, graphFocus.getCodeNamespace());
                    } catch (LBParameterException e) {
                       LoggerFactory.getLogger().warn(e.getMessage());
                    }
                
                    if(ref != null) {
                        focus =
                            attemptToResolveFocus(
                                    ref.getCodingSchemeURN(), 
                                    ref.getCodingSchemeVersion(), 
                                    graphFocus,
                                    this.shouldResolveNextLevel(resolveCodedEntryDepth),
                                    propertyNames,
                                    propertyTypes);
                    }
                }  
            } 
            
            //if we haven't found it yet, all we can do is assign the defaults provided by the coding scheme
            if(focus == null) {
               focus = new ResolvedConceptReference();
               focus.setCode(graphFocus.getCode());
               
               String namespace = graphFocus.getCodeNamespace();
               String codingSchemeName = graphFocus.getCodingSchemeName();
             
               if(StringUtils.isBlank(namespace)){
                   if(StringUtils.isBlank(codingSchemeName)) {
                       namespace = LexEvsServiceLocator.getInstance().getSystemResourceService().getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, version);
                   } else {
                       List<String> namespaces = this.getNamespaceHandler().getNamespacesForCodingScheme(codingSchemeUri, version, codingSchemeName);
                       if(CollectionUtils.isEmpty(namespaces)) {
                           return ServiceUtility.throwExceptionOrReturnDefault(
                                   new LBParameterException("The provided focus did not contain a namespace, and information in the " +
                                   "SupportedNamespaces was unable to generate the correct one."),
                                   new ResolvedConceptReferenceList(),
                                   this.isStrictFocusValidation());
                       }
                       if(namespaces.size() > 1) {
                           return ServiceUtility.throwExceptionOrReturnDefault(
                                   new LBParameterException("The provided focus did not contain a namespace, and information in the " +
                                   "SupportedNamespaces did not provide a unique namespace. Please provide a namespace with the focus"),
                                   new ResolvedConceptReferenceList(),
                                   this.isStrictFocusValidation());
                       }
                       
                       namespace = namespaces.get(0);
                   }
               } 
               
               String expectedCodingSchemeName = this.getNamespaceHandler().getCodingSchemeNameForNamespace(codingSchemeUri, version, namespace);
               if(StringUtils.isBlank(codingSchemeName)){
                   codingSchemeName = expectedCodingSchemeName;
               } else {
                   
                   if(!codingSchemeName.equals(expectedCodingSchemeName)) {
                       return ServiceUtility.throwExceptionOrReturnDefault(
                               new LBParameterException("Based on the namespace provided as a focus (" + namespace + ")" +
                                       " there is no match to the provided Coding Scheme Name (" + codingSchemeName + ")." +
                                       " If " + namespace + " is meant to be equivalent to the CodingScheme " + codingSchemeName + ", " +
                                       " this must be declared in the SupportedNamespaces."
                               ), 
                               new ResolvedConceptReferenceList(),
                               this.isStrictFocusValidation()
                       );
                   }
               }

               if(StringUtils.isBlank(codingSchemeName)) {
                   return ServiceUtility.throwExceptionOrReturnDefault(
                           new LBParameterException("Could not determine a Coding Scheme for the requested Focus Code."),
                           new ResolvedConceptReferenceList(),
                           this.isStrictFocusValidation()
                           );
               }
               
               focus.setCodeNamespace(namespace);    
               
               focus.setCodingSchemeName(codingSchemeName);
            } else {
                needToValidateFocusExistsInGraph = false;
            }
         
            boolean isValidFocus = PagedGraphUtils.checkFocus(
                    this.getCodingSchemeUri(),
                    this.getVersion(),
                    this.getRelationsContainerName(),
                    focus, 
                    resolveForward, 
                    resolveBackward, 
                    filters, 
                    this.getGraphQueryBuilder().getQuery(),
                    needToValidateFocusExistsInGraph,
                    true);
            
            if(! isValidFocus) {
                return new ResolvedConceptReferenceList();
            }
            
        } else {

            return new LazyLoadableResolvedConceptReferenceList(
                    new ValidatedParameterResolvingCallback() {

                        private static final long serialVersionUID = 1401783416514871042L;

                        @Override
                        public ResolvedConceptReferenceList doResolveAsValidatedParameterList(
                                ConceptReference graphFocus, 
                                boolean resolveForward, 
                                boolean resolveBackward,
                                int resolveCodedEntryDepth, 
                                int resolveAssociationDepth, 
                                LocalNameList propertyNames,
                                PropertyType[] propertyTypes, 
                                SortOptionList sortOptions, 
                                LocalNameList filterOptions,
                                int maxToReturn, 
                                boolean keepLastAssociationLevelUnresolved,
                                ArtificialRootResolvePolicy artificialRootResolvePolicy,
                                CycleDetectingCallback cycleDetectingCallback) throws LBInvocationException, LBParameterException {
                            
                            return PagingCodedNodeGraphImpl.this.doResolveAsValidatedParameterList(
                                    graphFocus, 
                                    resolveForward, 
                                    resolveBackward, 
                                    resolveCodedEntryDepth, 
                                    resolveAssociationDepth, 
                                    propertyNames, 
                                    propertyTypes, 
                                    sortOptions, 
                                    filterOptions, 
                                    maxToReturn, 
                                    keepLastAssociationLevelUnresolved, 
                                    artificialRootResolvePolicy,
                                    cycleDetectingCallback);
                        }
                        
                    }, 
                    
                    this.getCodingSchemeUri(),
                    this.getVersion(),
                    this.getRelationsContainerName(),
                    resolveForward, 
                    resolveBackward, 
                    resolveAssociationDepth, 
                    resolveCodedEntryDepth, 
                    keepLastAssociationLevelUnresolved,
                    this.getGraphQueryBuilder().getQuery(),
                    propertyNames, 
                    propertyTypes,
                    sortOptions, 
                    filterOptions, 
                    cycleDetectingCallback,
                    artificialRootResolvePolicy,
                    maxToReturn);
        }
        
        int resolveForwardAssociationDepth = resolveAssociationDepth;
        int resolveBackwardAssociationDepth = resolveAssociationDepth;
        
        if(this.rootsResolver.isRootOrTail(focus) && 
                resolveAssociationDepth >= 0 && 
                artificialRootResolvePolicy.equals(ArtificialRootResolvePolicy.RESOLVE_CHILDREN)) {
        	if(resolveForward) {
        		resolveForwardAssociationDepth++;
        	}
        	if(resolveBackward) {
        		resolveBackwardAssociationDepth++;
            }
        }

        if(resolveForward && shouldResolveNextLevel(resolveForwardAssociationDepth)) {
 
            focus.setSourceOf(
            		associationListBuilder.buildSourceOfAssociationList(
            		this.getCodingSchemeUri(),
            		this.getVersion(),
                    focus.getCode(),
                    focus.getCodeNamespace(),
                    relationsContainerName,
                    resolveForward,
                    resolveBackward,
                    resolveForwardAssociationDepth - 1, 
                    resolveBackwardAssociationDepth,
                    resolveCodedEntryDepth,
                    graphQueryBuilder.getQuery(),
                    propertyNames,
                    propertyTypes,
                    sortOptions,
                    filterOptions,
                    cycleDetectingCallback));
        }
        
        if(resolveBackward && shouldResolveNextLevel(resolveBackwardAssociationDepth)) {
            focus.setTargetOf(
            		associationListBuilder.buildTargetOfAssociationList(
            		this.getCodingSchemeUri(),
                    this.getVersion(),
                    focus.getCode(),
                    focus.getCodeNamespace(),
                    relationsContainerName,
                    resolveForward,
                    resolveBackward,
                    resolveForwardAssociationDepth, 
                    resolveBackwardAssociationDepth - 1,
                    resolveCodedEntryDepth,
                    graphQueryBuilder.getQuery(),
                    propertyNames,
                    propertyTypes,
                    sortOptions,
                    filterOptions,
                    cycleDetectingCallback));
        }
        
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        if(! this.rootsResolver.isRootOrTail(focus)) {
            returnList.addResolvedConceptReference(focus);
        } else {
            switch (artificialRootResolvePolicy) {
                case RESOLVE_CHILDREN: {
                    returnList = flattenRootList(focus);
                    break;
                }
                case RESOLVE_AS_IS: {
                    returnList.addResolvedConceptReference(focus);
                    break;
                }
                case SKIP: {
                    break;
                }
            }
        }
    
        return returnList;
    }

    private ResolvedConceptReference attemptToResolveFocus(
            String codingSchemeUri, 
            String version, 
            ConceptReference graphFocus, 
            boolean resolve,
            LocalNameList propertyNames, 
            PropertyType[] propertyTypes) {
        ResolvedConceptReference
            focus =
                LexEvsServiceLocator.getInstance().getDatabaseServiceManager().
                getEntityService().
                getResolvedCodedNodeReference(
                        codingSchemeUri, 
                        version, 
                        graphFocus.getCode(), 
                        graphFocus.getCodeNamespace(),
                        resolve,
                        DaoUtility.localNameListToString(propertyNames),
                        DaoUtility.propertyTypeArrayToString(propertyTypes));
        
        try {
            if(focus == null && ServiceUtility.isSupplement(codingSchemeUri, version)) {
                AbsoluteCodingSchemeVersionReference parent = 
                    ServiceUtility.getParentOfSupplement(codingSchemeUri, version);
                
                return this.attemptToResolveFocus(
                        parent.getCodingSchemeURN(), 
                        parent.getCodingSchemeVersion(), 
                        graphFocus, 
                        resolve, 
                        propertyNames, 
                        propertyTypes);
            }
        } catch (LBParameterException e) {
           LoggerFactory.getLogger().warn("Error attempting to Resolve Focus from Supplement.");
        }
        return focus;
    }

    protected ArtificialRootResolvePolicy adjustArtificialRootResolvePolicy(
            boolean resolveForward,
            boolean resolveBackward, 
            ArtificialRootResolvePolicy artificialRootResolvePolicy, 
            GraphQuery query) {
        if(resolveForward && query.getRestrictToTargetCodes().size() > 0) {
            return ArtificialRootResolvePolicy.SKIP;
        }
        if(resolveBackward && query.getRestrictToSourceCodes().size() > 0) {
            return ArtificialRootResolvePolicy.SKIP;
        }
       
        return artificialRootResolvePolicy;
    }

    private ResolvedConceptReferenceList flattenRootList(ResolvedConceptReference root) {
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        if(root.getSourceOf() != null) {
            for(Association assoc : root.getSourceOf().getAssociation()) {
                for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                    returnList.addResolvedConceptReference(ac);
                }
            }
        }
        
        if(root.getTargetOf() != null) {
            for(Association assoc : root.getTargetOf().getAssociation()) {
                for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
                    returnList.addResolvedConceptReference(ac);
                }
            }
        }
        
        return returnList;
    }
      
    /**
     * Should resolve next level.
     * 
     * @param depth the depth
     * 
     * @return true, if successful
     */
    private boolean shouldResolveNextLevel(int depth) {
        return ! (depth == 0);
    }
    
    private NamespaceHandler getNamespaceHandler() {
        return NamespaceHandlerFactory.getNamespaceHandler();
    }
}