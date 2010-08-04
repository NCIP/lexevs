package org.LexGrid.LexBIG.Impl.pagedgraph.utility;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.AbstractCodedNodeGraph;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

public class ValidatedParameterResolvingCallback {
    
    private AbstractCodedNodeGraph abstractCodedNodeGraph;
    
    public ValidatedParameterResolvingCallback(AbstractCodedNodeGraph abstractCodedNodeGraph){
        this.abstractCodedNodeGraph = abstractCodedNodeGraph;
    }

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
            CycleDetectingCallback cycleDetectingCallback)
            throws LBInvocationException, LBParameterException{
        return this.abstractCodedNodeGraph.doResolveAsList(
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
                keepLastAssociationLevelUnresolved);
    }
}
