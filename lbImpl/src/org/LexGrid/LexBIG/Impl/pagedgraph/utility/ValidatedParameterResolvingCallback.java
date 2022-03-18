
package org.LexGrid.LexBIG.Impl.pagedgraph.utility;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.pagedgraph.PagingCodedNodeGraphImpl.ArtificialRootResolvePolicy;
import org.LexGrid.LexBIG.Impl.pagedgraph.paging.callback.CycleDetectingCallback;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

public interface ValidatedParameterResolvingCallback extends Serializable {

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
            CycleDetectingCallback cycleDetectingCallback)
            throws LBInvocationException, LBParameterException;
}