
package org.LexGrid.LexBIG.Impl.pagedgraph;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * The Class AbstractMultiGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMultiGraph extends AbstractCodedNodeGraph {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8545723123536546421L;

    /** The graph1. */
    private CodedNodeGraph graph1;
    
    /** The graph2. */
    private CodedNodeGraph graph2;
    
    public AbstractMultiGraph() {
        super();
    }
    
    /**
     * Instantiates a new union graph.
     * 
     * @param graph1 the graph1
     * @param graph2 the graph2
     */
    public AbstractMultiGraph(CodedNodeGraph graph1, CodedNodeGraph graph2) {
        this.graph1 = graph1;
        this.graph2 = graph2;
    }


    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToAssociations(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public CodedNodeGraph restrictToAssociations(NameAndValueList association, NameAndValueList associationQualifiers)
            throws LBInvocationException, LBParameterException {
       this.graph1.restrictToAssociations(association, associationQualifiers);
       this.graph2.restrictToAssociations(association, associationQualifiers);  
       
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToCodeSystem(String codingScheme) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToCodeSystem(codingScheme);
        this.graph2.restrictToCodeSystem(codingScheme);  
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToCodes(codes);
        this.graph2.restrictToCodes(codes);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToDirectionalNames(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public CodedNodeGraph restrictToDirectionalNames(NameAndValueList directionalNames,
            NameAndValueList associationQualifiers) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToDirectionalNames(directionalNames, associationQualifiers);
        this.graph2.restrictToDirectionalNames(directionalNames, associationQualifiers);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToSourceCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
       this.graph1.restrictToSourceCodeSystem(codingScheme);
       this.graph2.restrictToSourceCodeSystem(codingScheme);
       
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToSourceCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToSourceCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToSourceCodes(codes);
        this.graph2.restrictToSourceCodes(codes);
        
        return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodeSystem(java.lang.String)
     */
    @Override
    public CodedNodeGraph restrictToTargetCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
       this.graph1.restrictToTargetCodeSystem(codingScheme);
       this.graph2.restrictToTargetCodeSystem(codingScheme);
       
       return this;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph#restrictToTargetCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public CodedNodeGraph restrictToTargetCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        this.graph1.restrictToTargetCodes(codes);
        this.graph2.restrictToTargetCodes(codes);
        
        return this;
    }
    
    @Override
    public CodedNodeGraph restrictToAnonymous(Boolean restrictToAnonymous) throws LBInvocationException,
            LBParameterException {
        this.graph1.restrictToAnonymous(restrictToAnonymous);
        this.graph2.restrictToAnonymous(restrictToAnonymous);
        
        return this;
    }

    @Override
    public CodedNodeGraph restrictToEntityTypes(LocalNameList localNameList) throws LBInvocationException,
            LBParameterException {
        this.graph1.restrictToEntityTypes(localNameList);
        this.graph2.restrictToEntityTypes(localNameList);
        
        return this;
    }

    /**
     * Gets the graph1.
     * 
     * @return the graph1
     */
    public CodedNodeGraph getGraph1() {
        return graph1;
    }

    /**
     * Sets the graph1.
     * 
     * @param graph1 the new graph1
     */
    public void setGraph1(CodedNodeGraph graph1) {
        this.graph1 = graph1;
    }

    /**
     * Gets the graph2.
     * 
     * @return the graph2
     */
    public CodedNodeGraph getGraph2() {
        return graph2;
    }

    /**
     * Sets the graph2.
     * 
     * @param graph2 the new graph2
     */
    public void setGraph2(CodedNodeGraph graph2) {
        this.graph2 = graph2;
    }
}