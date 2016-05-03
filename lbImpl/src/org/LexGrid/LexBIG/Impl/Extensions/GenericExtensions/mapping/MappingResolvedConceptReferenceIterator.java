package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.CodedNodeSetBackedMapping.RelationshipRestriction;
import org.LexGrid.LexBIG.Impl.helpers.IteratorBackedResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

public class MappingResolvedConceptReferenceIterator extends IteratorBackedResolvedConceptReferencesIterator{

    /**
     * 
     */
    private static final long serialVersionUID = -698270712034240196L;
    private boolean areAllCodedNodeSetsNull;
    Iterator<ResolvedConceptReference> iterator;
    String mappingUri;
    String mappingVersion;
    String relationsContainerName;
    List<MappingSortOption> sortOptionList;
    CodedNodeSet sourceCodesCodedNodeSet;
    CodedNodeSet targetCodesCodedNodeSet;
    CodedNodeSet sourceOrTargetCodesCodedNodeSet;
    List<RelationshipRestriction> relationshipRestrictions;
    
   
    public MappingResolvedConceptReferenceIterator(){};
    public MappingResolvedConceptReferenceIterator(Iterator<ResolvedConceptReference> iterator, int count,boolean areAllCodedNodeSetsNull, String mappingUri,
            String mappingVersion, String relationsContainerName, List<MappingSortOption> sortOptionList,
            CodedNodeSet sourceCodesCodedNodeSet, CodedNodeSet targetCodesCodedNodeSet,
            CodedNodeSet sourceOrTargetCodesCodedNodeSet, List<RelationshipRestriction> relationshipRestrictions) {
        super(iterator, count);        
        this.iterator = iterator;
        this.areAllCodedNodeSetsNull = areAllCodedNodeSetsNull;
        this.mappingUri = mappingUri;
        this.mappingVersion = mappingVersion;
        this.relationsContainerName = relationsContainerName;
        this.sortOptionList = sortOptionList;
        this.sourceCodesCodedNodeSet = sourceCodesCodedNodeSet;
        this.targetCodesCodedNodeSet = targetCodesCodedNodeSet;
        this.sourceOrTargetCodesCodedNodeSet = sourceOrTargetCodesCodedNodeSet;
        this.relationshipRestrictions = relationshipRestrictions;
    }



    
    public ResolvedConceptReferenceList get(int start, int end)
            throws LBResourceUnavailableException, LBInvocationException, LBParameterException {
        Iterator<ResolvedConceptReference> iterator;

 
//        if (areAllCodedNodeSetsNull) {
//            iterator = new MappingTripleIterator(mappingUri, mappingVersion, relationsContainerName, sortOptionList);
//        } else {
//
//
//            iterator = new RestrictingMappingTripleIterator(mappingUri, mappingVersion, relationsContainerName,
//                    sourceCodesCodedNodeSet, targetCodesCodedNodeSet, sourceOrTargetCodesCodedNodeSet,
//                    relationshipRestrictions, sortOptionList);
//        }
//        
  
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();

        int pos = 0;
        while (this.iterator.hasNext() && pos < end) {
            ResolvedConceptReference ref = this.iterator.next();
            if (pos >= start) {
                returnList.addResolvedConceptReference(ref);
            }
            pos++;
        }

        return returnList;
    }

}
