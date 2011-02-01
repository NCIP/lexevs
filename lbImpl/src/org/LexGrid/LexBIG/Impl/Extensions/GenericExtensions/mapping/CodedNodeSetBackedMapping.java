package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Iterator;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
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
    private CodedNodeSet codedNodeSet;
    
    /** The mapping uri. */
    private String mappingUri;
    
    /** The mapping version. */
    private String mappingVersion;
    
    /** The relations container name. */
    private String relationsContainerName;
    
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
        this.codedNodeSet = LexBIGServiceImpl.defaultInstance().getNodeSet(codingScheme, codingSchemeVersionOrTag, null);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping#restrictToMatchingDesignations(java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
     */
    @Override
    public Mapping restrictToMatchingDesignations(
            String matchText, 
            SearchDesignationOption option,
            String matchAlgorithm, 
            String language) throws LBInvocationException, LBParameterException {
        this.codedNodeSet.restrictToMatchingDesignations(matchText, option, matchAlgorithm, language);
        
        return this;
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping#resolveMapping()
     */
    @Override
    public ResolvedConceptReferencesIterator resolveMapping() throws LBException {
        
        Iterator<ResolvedConceptReference> iterator = 
            new RestrictingMappingTripleIterator(
                    mappingUri,
                    mappingVersion,
                    relationsContainerName, 
                    codedNodeSet);

        return 
            new IteratorBackedResolvedConceptReferencesIterator(iterator);
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
