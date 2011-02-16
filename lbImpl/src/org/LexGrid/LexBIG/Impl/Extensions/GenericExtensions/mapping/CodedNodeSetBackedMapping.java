package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.service.codednodegraph.CodedNodeGraphService;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * The Class CodedNodeSetBackedMapping.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodedNodeSetBackedMapping implements Mapping {
 
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1523037493728989141L;
    
    /** The coded node set. */
    private CodedNodeSet sourceCodesCodedNodeSet;
    
    private CodedNodeSet targetCodesCodedNodeSet;
    
    private CodedNodeSet sourceOrTargetCodesCodedNodeSet;
    
    /** The mapping uri. */
    private String mappingUri;
    
    /** The mapping version. */
    private String mappingVersion;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /**
     * Instantiates a new coded node set backed mapping.
     */
    public CodedNodeSetBackedMapping(){
        super();
    }
    
    /**
     * Instantiates a new coded node set backed mapping.
     * 
     * @param codingScheme the coding scheme
     * @param codingSchemeVersionOrTag the coding scheme version or tag
     * @param relationsContainerName the relations container name
     * 
     * @throws LBException the LB exception
     */
    public CodedNodeSetBackedMapping(
            String codingScheme,
            CodingSchemeVersionOrTag codingSchemeVersionOrTag, 
            String relationsContainerName) throws LBException{
        AbsoluteCodingSchemeVersionReference ref = 
            ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingScheme, codingSchemeVersionOrTag, true);
        this.mappingUri = ref.getCodingSchemeURN();
        this.mappingVersion = ref.getCodingSchemeVersion();
        this.relationsContainerName = relationsContainerName;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping#resolveMapping()
     */
    @Override
    public ResolvedConceptReferencesIterator resolveMapping() throws LBException {
        Iterator<ResolvedConceptReference> iterator;
        
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        int count;
        
        if(areAllCodedNodeSetsNull()){
            iterator = new MappingTripleIterator(
                    mappingUri,
                    mappingVersion,
                    relationsContainerName,
                    null);
            
            count = service.getMappingTriplesCount(mappingUri, mappingVersion, relationsContainerName);
            
        } else {
            RestrictingMappingTripleIterator restrictingIterator = 
                new RestrictingMappingTripleIterator(
                        mappingUri,
                        mappingVersion,
                        relationsContainerName, 
                        this.sourceCodesCodedNodeSet,
                        this.targetCodesCodedNodeSet,
                        this.sourceOrTargetCodesCodedNodeSet,
                        null);
            
            iterator = restrictingIterator;
            
            count = this.estimateMappingNumber(
                    restrictingIterator.getTripleUidIterator().getSourceCodesResolvedConceptReferencesIterator(),
                    restrictingIterator.getTripleUidIterator().getTargetCodesResolvedConceptReferencesIterator(),
                    restrictingIterator.getTripleUidIterator().getSourceOrTargetCodesResolvedConceptReferencesIterator());      
        }
        
        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator, count);
    }
    
    protected int estimateMappingNumber(
            ResolvedConceptReferencesIterator sourceResolvedConceptReferencesIterator, 
            ResolvedConceptReferencesIterator targetResolvedConceptReferencesIterator, 
            ResolvedConceptReferencesIterator sourceAndTargetresolvedConceptReferencesIterator) throws LBResourceUnavailableException{
        if(sourceResolvedConceptReferencesIterator == null && 
                targetResolvedConceptReferencesIterator == null &&
                sourceAndTargetresolvedConceptReferencesIterator == null){
            return IteratorBackedResolvedConceptReferencesIterator.UNKNOWN_NUMBER;
        }
        
        //if the sourceAndTarget iterator is there -- the other two must be null
        if(sourceAndTargetresolvedConceptReferencesIterator != null){   
            int number = sourceAndTargetresolvedConceptReferencesIterator.numberRemaining();
            return this.getEstimateNumber(number);
        }

        if(sourceResolvedConceptReferencesIterator == null){
            return targetResolvedConceptReferencesIterator.numberRemaining();
        }

        if(targetResolvedConceptReferencesIterator == null){
            return sourceResolvedConceptReferencesIterator.numberRemaining();
        }

        int number = sourceResolvedConceptReferencesIterator.numberRemaining()
                    +
                    sourceResolvedConceptReferencesIterator.numberRemaining();
        return this.getEstimateNumber(number);     
    }
    
    private int getEstimateNumber(int number){
        if(number == 0 || number == 1){
            return number;
        } else {
            return number / 2;
        }
    }

    @Override
    public ResolvedConceptReferencesIterator resolveMapping(List<MappingSortOption> sortOptionList) throws LBException {
        if(CollectionUtils.isEmpty(sortOptionList)){
            return this.resolveMapping();
        }
        
        Iterator<ResolvedConceptReference> iterator;
        
        CodedNodeGraphService service = 
            LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService();
        
        int count;
        
        if(areAllCodedNodeSetsNull()){
           iterator = new MappingTripleIterator(
                    mappingUri,
                    mappingVersion,
                    relationsContainerName,
                    sortOptionList);
           
           count = service.getMappingTriplesCount(mappingUri, mappingVersion, relationsContainerName);
           
        } else {
            RestrictingMappingTripleIterator restrictingIterator = 
                new RestrictingMappingTripleIterator(
                        mappingUri,
                        mappingVersion,
                        relationsContainerName, 
                        this.sourceCodesCodedNodeSet,
                        this.targetCodesCodedNodeSet,
                        this.sourceOrTargetCodesCodedNodeSet,
                        sortOptionList);
            
            count = service.getMappingTriplesCountForCodes(
                    mappingUri, 
                    mappingVersion, 
                    relationsContainerName, 
                    restrictingIterator.getTripleUidIterator().getSourceResolvedIteratorConceptReferences(), 
                    restrictingIterator.getTripleUidIterator().getTargetResolvedIteratorConceptReferences(),
                    restrictingIterator.getTripleUidIterator().getSourceOrTargetResolvedIteratorConceptReferences());
            
            iterator = restrictingIterator;
        }
        
        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator, count);
    }
    
    @Override
    public Mapping restrictToMatchingDesignations(
            final String matchText, 
            final SearchDesignationOption option,
            final String matchAlgorithm, 
            final String language, 
            final SearchContext searchContext) throws LBInvocationException,
            LBParameterException {
        
        this.doRestrict(new DoRestrict(){

            @Override
            public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException {
                return codedNodeSet.
                    restrictToMatchingDesignations(
                            matchText, 
                            option, 
                            matchAlgorithm, 
                            language);
            }
            
        }, searchContext);
        
        return this;
    }
    
    private boolean areAllCodedNodeSetsNull(){
        return this.sourceCodesCodedNodeSet == null &&
            this.targetCodesCodedNodeSet == null &&
            this.sourceOrTargetCodesCodedNodeSet == null;
    }

    @Override
    public Mapping restrictToMatchingProperties(
            final LocalNameList propertyNames, 
            final PropertyType[] propertyTypes,
            final LocalNameList sourceList, 
            final LocalNameList contextList, 
            final NameAndValueList qualifierList, 
            final String matchText,
            final String matchAlgorithm, 
            final String language, 
            SearchContext searchContext) throws LBInvocationException,
            LBParameterException {
        
        this.doRestrict(new DoRestrict(){

            @Override
            public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException {
                return codedNodeSet.restrictToMatchingProperties(
                        propertyNames, 
                        propertyTypes, 
                        sourceList, 
                        contextList, 
                        qualifierList, 
                        matchText, 
                        matchAlgorithm, 
                        language);
            }
            
        }, searchContext);
        
        return this;
    }

    @Override
    public Mapping restrictToCodes(
            final ConceptReferenceList codeList, 
            SearchContext searchContext)
            throws LBInvocationException, LBParameterException {
        
        this.doRestrict(new DoRestrict(){

            @Override
            public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException {
                return codedNodeSet.restrictToCodes(codeList);
            }
            
        }, searchContext);
        
        return this;
    }

    @Override
    public Mapping restrictToRelationship(
            String matchText, 
            SearchDesignationOption option, 
            String matchAlgorithm,
            String language, 
            LocalNameList relationshipList) throws LBInvocationException, LBParameterException {
          throw new RuntimeException();
    }
    
    private CodedNodeSet getCodedNodeSet(SearchContext searchContext) throws LBParameterException{
        switch(searchContext){
        
            case SOURCE_CODES : {
                if(this.sourceCodesCodedNodeSet == null){
                    this.sourceCodesCodedNodeSet = this.createCodedNodeSet();
                }
                return this.sourceCodesCodedNodeSet;
            }
            
            case TARGET_CODES : {
                if(this.targetCodesCodedNodeSet == null){
                    this.targetCodesCodedNodeSet = this.createCodedNodeSet();
                }
                return this.targetCodesCodedNodeSet;
            }
            
            case SOURCE_OR_TARGET_CODES : {
                if(this.sourceOrTargetCodesCodedNodeSet == null){
                    this.sourceOrTargetCodesCodedNodeSet = this.createCodedNodeSet();
                }
                return this.sourceOrTargetCodesCodedNodeSet;
            }
        }
        
        throw new RuntimeException("Invalid SearchContext.");
    }
    
    public CodedNodeSet createCodedNodeSet() throws LBParameterException{
        try {
            return LexBIGServiceImpl.defaultInstance().
                getNodeSet(
                        this.mappingUri, 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(mappingVersion), 
                        null);
        } catch (LBException e) {
            throw new LBParameterException(e.getMessage());
        }
    }
    
    private interface DoRestrict {
        
        public CodedNodeSet restrict(CodedNodeSet codedNodeSet) throws LBParameterException, LBInvocationException;
    }
    
    protected void doRestrict(DoRestrict doRestrict, SearchContext searchContext) throws LBParameterException, LBInvocationException{
        
        if(searchContext == null){
            throw new LBParameterException("SearchContext cannot be null", "searchContext");
        }

        CodedNodeSet cns = this.getCodedNodeSet(searchContext);

        cns = doRestrict.restrict(cns);

        switch(searchContext){

            case SOURCE_CODES : {
                this.sourceCodesCodedNodeSet = cns;
                break;
            }
    
            case TARGET_CODES : {
                this.targetCodesCodedNodeSet = cns;
                break;
            }
    
            case SOURCE_OR_TARGET_CODES : {
                this.sourceOrTargetCodesCodedNodeSet = cns;
                break;
            }
        }
    }

    /**
     * Gets the mapping uri.
     * 
     * @return the mapping uri
     */
    public String getMappingUri(){
        return this.mappingUri;
    }
    
    /**
     * Gets the mapping version.
     * 
     * @return the mapping version
     */
    public String getMappingVersion(){
        return this.mappingVersion;
    }
    
    /**
     * Gets the relations container name.
     * 
     * @return the relations container name
     */
    public String getRelationsContainerName(){
        return this.relationsContainerName;
    }
}
