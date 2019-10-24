package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension;
import org.LexGrid.LexBIG.Extensions.Generic.SearchExtension.MatchAlgorithm;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.supplement.SupplementExtensionImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.naming.SupportedAssociation;

public class NodeGraphResolutionExtensionImpl extends AbstractExtendable implements NodeGraphResolutionExtension {

    /**
     * 
     */
    private static final long serialVersionUID = -2869847921528174582L;
    
    


    @Override
    public Iterator<ConceptReference> getConceptReferencesForEntityCodeAndAssociationTargetOf(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            MatchAlgorithm alg, ModelMatch model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Iterator<ConceptReference> getConceptReferencesForEntityCodeAndAssociationSourceOf(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            MatchAlgorithm alg, ModelMatch model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable#buildExtensionDescription()
     */
    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Node Graph Resolution Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(SupplementExtensionImpl.class.getName());
        ed.setName("NodeGraphResolution");
        ed.setVersion("1.0");
        
        return ed;
    }
    
    private CodedNodeSet getCodedNodeSetForScheme(AbsoluteCodingSchemeVersionReference ref) throws LBException{
        return LexBIGServiceImpl.defaultInstance()
                .getCodingSchemeConcepts(
                        ref.getCodingSchemeURN(), 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()));
    }
    
    private CodedNodeSet getCodedNodeSetForModelMatch(CodedNodeSet set, ModelMatch model, AlgorithmMatch alg, String text) throws LBInvocationException, LBParameterException{
        switch(model){
        case NAME: return set.restrictToMatchingDesignations(text, SearchDesignationOption.PREFERRED_ONLY, alg.getMatch(), null);
        case CODE: return set.restrictToCodes(Constructors.createConceptReferenceList(text));
        case PROPERTY: return set.restrictToMatchingProperties(null, null, text, alg.getMatch(), null);
        default: return set;
        }
    }
    
    
    private List<ConceptReference> getConceptReferenceListForValidatedAssociation(String associationName, CodedNodeSet set){
        return null;
    }
    
    private Boolean isValidAssociation(String associationName, AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
        return ServiceUtility.IsValidParameter(ref.getCodingSchemeURN(),ref.getCodingSchemeVersion(), associationName, SupportedAssociation.class);
    }
    
    private Boolean isValidNodeForTargetOfAssociation(String entityCode, String associationName){
        return ServiceUtility.isSourceOfAssociationTarget(entityCode, associationName);
    }
    
    private Boolean isValidNodeForSourceOfAssociation(String entityCode, String associationName){
        return ServiceUtility.isTargetOfAssociationSource(entityCode, associationName);
    }
    
    
    

}
