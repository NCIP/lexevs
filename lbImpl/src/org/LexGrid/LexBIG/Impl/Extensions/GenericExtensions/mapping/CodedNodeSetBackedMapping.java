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
    
    private CodedNodeSet sourceAndTargetCodesCodedNodeSet;
    
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
        
        if(areAllCodedNodeSetsNull()){
           iterator = new MappingTripleIterator(
                    mappingUri,
                    mappingVersion,
                    relationsContainerName,
                    null);
        } else {
            iterator = 
                new RestrictingMappingTripleIterator(
                        mappingUri,
                        mappingVersion,
                        relationsContainerName, 
                        this.sourceCodesCodedNodeSet,
                        this.targetCodesCodedNodeSet,
                        this.sourceAndTargetCodesCodedNodeSet,
                        null);
        }
        
        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator);
    }
    
    @Override
    public ResolvedConceptReferencesIterator resolveMapping(List<MappingSortOption> sortOptionList) throws LBException {
        Iterator<ResolvedConceptReference> iterator;
        
        if(areAllCodedNodeSetsNull()){
           iterator = new MappingTripleIterator(
                    mappingUri,
                    mappingVersion,
                    relationsContainerName,
                    sortOptionList);
        } else {
            iterator = 
                new RestrictingMappingTripleIterator(
                        mappingUri,
                        mappingVersion,
                        relationsContainerName, 
                        this.sourceCodesCodedNodeSet,
                        this.targetCodesCodedNodeSet,
                        this.sourceAndTargetCodesCodedNodeSet,
                        sortOptionList);
        }
        
        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator);
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
            this.sourceAndTargetCodesCodedNodeSet == null;
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
            
            case BOTH : {
                if(this.sourceAndTargetCodesCodedNodeSet == null){
                    this.sourceAndTargetCodesCodedNodeSet = this.createCodedNodeSet();
                }
                return this.sourceAndTargetCodesCodedNodeSet;
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

        CodedNodeSet cns = this.getCodedNodeSet(searchContext);

        doRestrict.restrict(cns);

        switch(searchContext){

            case SOURCE_CODES : {
                this.sourceCodesCodedNodeSet = cns;
                break;
            }
    
            case TARGET_CODES : {
                this.targetCodesCodedNodeSet = cns;
                break;
            }
    
            case BOTH : {
                this.sourceAndTargetCodesCodedNodeSet = cns;
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
