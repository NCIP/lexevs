package org.LexGrid.LexBIG.Impl.pagedgraph;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

public class UnionGraph implements CodedNodeGraph {

    private static final long serialVersionUID = 8545723123536546421L;

    private CodedNodeGraph graph1;
    private CodedNodeGraph graph2;
    
    public UnionGraph(CodedNodeGraph graph1, CodedNodeGraph graph2) {
        this.graph1 = graph1;
        this.graph2 = graph2;
    }
    
    @Override
    public Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public CodedNodeGraph intersect(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public Boolean isCodeInGraph(ConceptReference code) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public ConceptReferenceList listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public ConceptReferenceList listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            int distance) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions, int maxToReturn)
            throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public ResolvedConceptReferenceList resolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public CodedNodeGraph restrictToAssociations(NameAndValueList association, NameAndValueList associationQualifiers)
            throws LBInvocationException, LBParameterException {
       this.graph1.restrictToAssociations(association, associationQualifiers);
       this.graph2.restrictToAssociations(association, associationQualifiers);  
       
       return this;
    }

    @Override
    public CodedNodeGraph restrictToCodeSystem(String codingScheme) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToCodeSystem(codingScheme);
        this.graph2.restrictToCodeSystem(codingScheme);  
        
        return this;
    }

    @Override
    public CodedNodeGraph restrictToCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToCodes(codes);
        this.graph2.restrictToCodes(codes);
        
        return this;
    }

    @Override
    public CodedNodeGraph restrictToDirectionalNames(NameAndValueList directionalNames,
            NameAndValueList associationQualifiers) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToDirectionalNames(directionalNames, associationQualifiers);
        this.graph2.restrictToDirectionalNames(directionalNames, associationQualifiers);
        
        return this;
    }

    @Override
    public CodedNodeGraph restrictToSourceCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
       this.graph1.restrictToSourceCodeSystem(codingScheme);
       this.graph2.restrictToSourceCodeSystem(codingScheme);
       
       return this;
    }

    @Override
    public CodedNodeGraph restrictToSourceCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToSourceCodes(codes);
        this.graph2.restrictToSourceCodes(codes);
        
        return this;
    }

    @Override
    public CodedNodeGraph restrictToTargetCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
       this.graph1.restrictToTargetCodeSystem(codingScheme);
       this.graph2.restrictToTargetCodeSystem(codingScheme);
       
       return this;
    }

    @Override
    public CodedNodeGraph restrictToTargetCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToTargetCodes(codes);
        this.graph2.restrictToTargetCodes(codes);
        
        return this;
    }

    @Override
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    @Override
    public CodedNodeGraph union(CodedNodeGraph graph) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

}
