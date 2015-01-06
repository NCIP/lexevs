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

public class CglibWrappingIteratorBackedRCReferenceIterator extends IteratorBackedResolvedConceptReferencesIterator {
    

    /**
     * 
     */
    private static final long serialVersionUID = -3461720920130327671L;
    private String mappingUri;
    private String mappingVersion;
    private String relationsContainerName;
    private List<MappingSortOption> sortOptionList;
    private CodedNodeSet targetCodesCodedNodeSet;
    private CodedNodeSet sourceCodesCodedNodeSet;
    private CodedNodeSet sourceOrTargetCodesCodedNodeSet;
    private List<RelationshipRestriction> relationshipRestrictions;
    
    public CglibWrappingIteratorBackedRCReferenceIterator(){
        super();
    }
    
    public CglibWrappingIteratorBackedRCReferenceIterator(Iterator<ResolvedConceptReference> iterator, 
            String mappingUri, 
            String mappingVersion,
            String relationsContainerName, 
            List<MappingSortOption> sortOptionList, 
            CodedNodeSet targetCodesCodedNodeSet, 
            CodedNodeSet sourceCodesCodedNodeSet,
            CodedNodeSet sourceOrTargetCodesCodedNodeSet, 
            List <RelationshipRestriction> relationshipRestrictions, 
            List<MappingSortOption> sortOptionLis) {
        super(iterator);
                this.mappingUri = mappingUri;
                this.mappingVersion = mappingVersion;
                this.relationsContainerName = relationsContainerName; 
                this.sortOptionList = sortOptionList;
                this.targetCodesCodedNodeSet = targetCodesCodedNodeSet;
                this.sourceCodesCodedNodeSet = sourceCodesCodedNodeSet;
                this.sourceOrTargetCodesCodedNodeSet = sourceOrTargetCodesCodedNodeSet;
    }

    public CglibWrappingIteratorBackedRCReferenceIterator(Iterator<ResolvedConceptReference> iterator, 
            int count, 
            String mappingUri, 
            String mappingVersion,
            String relationsContainerName, 
            CodedNodeSet targetCodesCodedNodeSet, 
            CodedNodeSet sourceCodesCodedNodeSet,
            CodedNodeSet sourceOrTargetCodesCodedNodeSet, 
            List <RelationshipRestriction> relationshipRestrictions, 
            List<MappingSortOption> sortOptionList) {
        super(iterator, count);
        this.mappingUri = mappingUri;
        this.mappingVersion = mappingVersion;
        this.relationsContainerName = relationsContainerName; 
        this.sortOptionList = sortOptionList;
        this.targetCodesCodedNodeSet = targetCodesCodedNodeSet;
        this.sourceCodesCodedNodeSet = sourceCodesCodedNodeSet;
        this.sourceOrTargetCodesCodedNodeSet = sourceOrTargetCodesCodedNodeSet;
    }
    
    
    @Override
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {
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
                        sourceCodesCodedNodeSet,
                        targetCodesCodedNodeSet,
                        sourceOrTargetCodesCodedNodeSet,
                        relationshipRestrictions,
                        sortOptionList);
        }
        ResolvedConceptReferenceList returnList = new ResolvedConceptReferenceList();
        
        int pos = 0;
        while(iterator.hasNext() && pos < end){
            ResolvedConceptReference ref = iterator.next();
            if(pos >= start){
                returnList.addResolvedConceptReference(ref);
            }
            pos++;
        }
        
        return returnList;
    }


    private boolean areAllCodedNodeSetsNull(){
        return this.sourceCodesCodedNodeSet == null &&
            this.targetCodesCodedNodeSet == null &&
            this.sourceOrTargetCodesCodedNodeSet == null;
    }

}
