package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.LexGrid.LexBIG.Extensions.Generic.NodeGraphResolutionExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.AbstractExtendable;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.naming.SupportedAssociation;
import org.lexevs.dao.database.graph.rest.client.LexEVSSpringRestClientImpl;
import org.lexevs.dao.database.utility.GraphingDatabaseUtil;
import org.lexevs.locator.LexEvsServiceLocator;

public class NodeGraphResolutionExtensionImpl extends AbstractExtendable implements NodeGraphResolutionExtension {

    /**
     * 
     */
    private static final long serialVersionUID = -2869847921528174582L;

    private static final String TARGET_OF = null;
    
    LexEVSSpringRestClientImpl lexClientService;


    @Override
    public Iterator<ConceptReference> getConceptReferencesForEntityCodeAndAssociationTargetOf(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model){
        CodedNodeSet set = null; 

        try {
            if(!this.isValidAssociation(associationName, reference))
            {throw new RuntimeException("Not a valid association name: " 
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());}
            set = this.getCodedNodeSetForScheme(reference);
            set = this.getCodedNodeSetForModelMatch(set, model, alg, textMatch);
            return getConceptReferenceListForValidatedAssociation(reference, associationName, TARGET_OF, set).iterator();
           
        } catch (LBException e) {
            throw new RuntimeException("Not able to resolve a graph for this coding scheme and graph "
                    + associationName 
                    + " CodingScheme " 
                    + reference.getCodingSchemeURN() 
                    + " version " 
                    + reference.getCodingSchemeVersion());
     
        }
        

    }

    @Override
    public Iterator<ConceptReference> getConceptReferencesForEntityCodeAndAssociationSourceOf(
            AbsoluteCodingSchemeVersionReference reference, String associationName, String textMatch,
            AlgorithmMatch alg, ModelMatch model) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void doRegister(ExtensionRegistry registry, ExtensionDescription description) throws LBParameterException {
        registry.registerGenericExtension(description);
    }


    @Override
    protected ExtensionDescription buildExtensionDescription() {
        ExtensionDescription ed = new ExtensionDescription();
        ed.setDescription("Node Graph Resolution Extension for LexEVS.");
        ed.setExtensionBaseClass(GenericExtension.class.getName());
        ed.setExtensionClass(NodeGraphResolutionExtensionImpl.class.getName());
        ed.setName("NodeGraphResolution");
        ed.setVersion("1.0");
        
        return ed;
    }
    
    protected CodedNodeSet getCodedNodeSetForScheme(AbsoluteCodingSchemeVersionReference ref) throws LBException{
        return LexBIGServiceImpl.defaultInstance()
                .getCodingSchemeConcepts(
                        ref.getCodingSchemeURN(), 
                        Constructors.createCodingSchemeVersionOrTagFromVersion(ref.getCodingSchemeVersion()));
    }
    
    protected CodedNodeSet getCodedNodeSetForModelMatch(CodedNodeSet set, ModelMatch model, AlgorithmMatch alg, String text) throws LBInvocationException, LBParameterException{
        switch(model){
        case NAME: return set.restrictToMatchingDesignations(text, SearchDesignationOption.PREFERRED_ONLY, alg.getMatch(), null);
        case CODE: return set.restrictToCodes(Constructors.createConceptReferenceList(text));
        case PROPERTY: return set.restrictToMatchingProperties(null, null, text, alg.getMatch(), null);
        default: return set;
        }
    }
    
    
    protected List<ConceptReference> getConceptReferenceListForValidatedAssociation(
            AbsoluteCodingSchemeVersionReference ref, 
            String associationName, 
            String direction,  
            CodedNodeSet set){
        List<ConceptReference> crefs = new ArrayList<ConceptReference>();
        try {
            ResolvedConceptReferenceList list = set.resolveToList(null, null, null, 10);
            if(isGetTargetOF(direction)){
            Stream.of(list.getResolvedConceptReference())
            .map(x -> lexClientService
                    .getOutBoundForGraphNode(
                            lexClientService.getBaseUrl(), 
                            getNormalizedDbNameForTermServiceIdentifiers(ref),
                            associationName, 
                            x.getCode()));
            }
        } catch (LBInvocationException | LBParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
    
    private boolean isGetTargetOF(String direction) {
        // TODO Auto-generated method stub
        return false;
    }

    protected Boolean isValidAssociation(String associationName, AbsoluteCodingSchemeVersionReference ref) throws LBParameterException {
        return ServiceUtility.IsValidParameter(ref.getCodingSchemeURN(),ref.getCodingSchemeVersion(), associationName, SupportedAssociation.class);
    }
    
    boolean isValidNodeForTargetOfAssociation( AbsoluteCodingSchemeVersionReference ref, String entityCode, String associationName){
        return ServiceUtility.isValidNodeForAssociation(ref, entityCode, associationName);
    }
    

    private String getNormalizedDbNameForTermServiceIdentifiers(AbsoluteCodingSchemeVersionReference ref){
        try {
            return GraphingDatabaseUtil.normalizeGraphandGraphDatabaseName(LexEvsServiceLocator
                    .getInstance()
                    .getSystemResourceService()
                    .getInternalCodingSchemeNameForUserCodingSchemeName(
                            ref.getCodingSchemeURN(), ref.getCodingSchemeVersion()));
        } catch (LBParameterException e) {
            throw new RuntimeException("Unable to retrieve and normalize database name for uri :" 
                    + ref.getCodingSchemeURN()
                    + " version: " + ref.getCodingSchemeVersion());
        }
    }
    
    public static void main(String ...args){
        AbsoluteCodingSchemeVersionReference ref = Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "18.05b");
        new NodeGraphResolutionExtensionImpl().getConceptReferencesForEntityCodeAndAssociationTargetOf(ref, "subClassOf", "Blood", AlgorithmMatch.EXACT_MATCH, ModelMatch.NAME);
    }

}
