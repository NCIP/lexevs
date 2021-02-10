
package org.LexGrid.LexBIG.Impl.pagedgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.KeyedGraph;
import org.LexGrid.LexBIG.Impl.pagedgraph.utility.MultiGraphUtility;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

/**
 * The Class IntersectGraph.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class IntersectGraph extends AbstractMultiGraph {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6431881502847939549L;
    
    public IntersectGraph() {
        super();
    }

    /**
     * Instantiates a new intersect graph.
     * 
     * @param graph1 the graph1
     * @param graph2 the graph2
     */
    public IntersectGraph(CodedNodeGraph graph1, CodedNodeGraph graph2) {
        super(graph1, graph2);
    }
    
    @Override
    public List<String> listCodeRelationships(ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
       List<String> assocs1 = this.getGraph1().listCodeRelationships(sourceCode, targetCode, directOnly);
       List<String> assocs2 = this.getGraph2().listCodeRelationships(sourceCode, targetCode, directOnly);
       
       Set<String> returnSet = new HashSet<String>(assocs1);

       returnSet.retainAll(assocs2);
       
       return new ArrayList<String>(returnSet);
    }

    @Override
    public Boolean areCodesRelated(NameAndValue association, ConceptReference sourceCode, ConceptReference targetCode,
            boolean directOnly) throws LBInvocationException, LBParameterException {
        boolean relatedGraph1 = this.getGraph1().areCodesRelated(association, sourceCode, targetCode, directOnly);
        boolean relatedGraph2 = this.getGraph1().areCodesRelated(association, sourceCode, targetCode, directOnly);
    
        return relatedGraph1 && relatedGraph2;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.AbstractCodedNodeGraph#doResolveAsList(org.LexGrid.LexBIG.DataModel.Core.ConceptReference, boolean, boolean, int, int, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType[], org.LexGrid.LexBIG.DataModel.Collections.SortOptionList, org.LexGrid.LexBIG.DataModel.Collections.LocalNameList, int, boolean)
     */
    @Override
    public ResolvedConceptReferenceList doResolveAsList(ConceptReference graphFocus, boolean resolveForward,
            boolean resolveBackward, int resolveCodedEntryDepth, int resolveAssociationDepth,
            LocalNameList propertyNames, PropertyType[] propertyTypes, SortOptionList sortOptions,
            LocalNameList filterOptions, int maxToReturn, boolean keepLastAssociationLevelUnresolved)
            throws LBInvocationException, LBParameterException {
        ResolvedConceptReferenceList list1;
        try {
            list1 = this.getGraph1().resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        } catch (LBParameterException e) {
            list1 = null;
        }
        
        ResolvedConceptReferenceList list2;
        try {
            list2 = this.getGraph2().resolveAsList(
                graphFocus, resolveForward, resolveBackward, 
                resolveCodedEntryDepth, resolveAssociationDepth, propertyNames, 
                propertyTypes, sortOptions, maxToReturn);
        } catch (LBParameterException e) {
            list2 = null;
        }

        return intersectReferenceList(graphFocus == null, list1, list2);
    }

    @Override
    public CodedNodeSet toNodeList(ConceptReference graphFocus, boolean resolveForward, boolean resolveBackward,
            int resolveAssociationDepth, int maxToReturn) throws LBInvocationException, LBParameterException {
        CodedNodeSet cns1 = getGraph1().toNodeList(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn);
        CodedNodeSet cns2 = getGraph2().toNodeList(graphFocus, resolveForward, resolveBackward, resolveAssociationDepth, maxToReturn);
        
        return cns1.intersect(cns2);    
    }
    
    protected ResolvedConceptReferenceList intersectReferenceList(
            boolean nullFocus,
            ResolvedConceptReferenceList list1, 
            ResolvedConceptReferenceList list2) {
        
        if(nullFocus) {
            KeyedGraph graph1 = new KeyedGraph(list1);
            KeyedGraph intersectedGraph = graph1.intersect(new KeyedGraph(list2));
            
            return intersectedGraph.toResolvedConceptReferenceList();
        } else {
            return MultiGraphUtility.intersectReferenceList(list1, list2);
        }
    }
}