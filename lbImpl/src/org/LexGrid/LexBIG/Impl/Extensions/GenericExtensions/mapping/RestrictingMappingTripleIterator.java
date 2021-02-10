
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.CodedNodeSetBackedMapping.RelationshipRestriction;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.apache.commons.collections.CollectionUtils;

/**
 * The Class RestrictingMappingTripleIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RestrictingMappingTripleIterator extends AbstractMappingTripleIterator<RestrictingMappingTripleUidIterator> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    private CodedNodeSet sourceCodesCodedNodeSet;
    
    private CodedNodeSet targetCodesCodedNodeSet;
    
    private CodedNodeSet sourceOrTargetCodesCodedNodeSet;
            
    private List<MappingSortOption> sortOptionList;
   
    /**
     * Instantiates a new mapping triple iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param codedNodeSet the coded node set
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public RestrictingMappingTripleIterator(
            String uri, 
            String version,
            String relationsContainerName,
            CodedNodeSet sourceCodesCodedNodeSet,
            CodedNodeSet targetCodesCodedNodeSet,
            CodedNodeSet sourceOrTargetCodesCodedNodeSet,
            List<RelationshipRestriction> relationshipRestrictions,
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        super(uri,version, relationsContainerName);
        this.sourceCodesCodedNodeSet = sourceCodesCodedNodeSet;
        this.targetCodesCodedNodeSet = targetCodesCodedNodeSet;
        this.sourceOrTargetCodesCodedNodeSet = sourceOrTargetCodesCodedNodeSet;
        this.sortOptionList = sortOptionList;
        this.initializetMappingTripleIterator();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.AbstractMappingTripleIterator#doPage(int, int)
     */
    @Override
    protected List<? extends ResolvedConceptReference> doPage(int currentPosition, int pageSize) {
        List<? extends ResolvedConceptReference> returnList = super.doPage(currentPosition, pageSize);
        
        try {
            if(returnList != null && CollectionUtils.isEmpty(this.sortOptionList)){
                Collections.sort(returnList, new MapMatchComparator(this.getTripleUidIterator().getInOrderConceptReferences()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return returnList;
    }
    
    /**
     * Gets the ranks.
     * 
     * @param list the list
     * 
     * @return the ranks
     */
    private Map<String,Integer> getRanks(List<ConceptReference> list){
        try {
         
            Map<String,Integer> returnMap = new HashMap<String,Integer>();
            
            for(int i=0;i<list.size();i++){
                returnMap.put(list.get(i).getCode(), i);
            }    
            
            return returnMap;
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.AbstractMappingTripleIterator#createTripleIterator()
     */
    @Override
    protected RestrictingMappingTripleUidIterator createTripleIterator() throws Exception {

        return new RestrictingMappingTripleUidIterator(
                getUri(), 
                getVersion(),
                getRelationsContainerName(),
                getRefs(),
                sourceCodesCodedNodeSet,
                targetCodesCodedNodeSet,
                sourceOrTargetCodesCodedNodeSet,
                this.sortOptionList);
    }

    /**
     * The Class MapMatchComparator.
     */
    private class MapMatchComparator implements Comparator<ResolvedConceptReference>{

        /** The rank list. */
        private Map<String,Integer> rankList;
        
        /**
         * Instantiates a new map match comparator.
         * 
         * @param list the list
         */
        private MapMatchComparator(List<ConceptReference> list){
            this.rankList = getRanks(list);
        }
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        @Override
        public int compare(ResolvedConceptReference ref1, ResolvedConceptReference ref2) {
            return getRank(ref1, rankList) - getRank(ref2, rankList);
        }
    }
    
    /**
     * Gets the rank.
     * 
     * @param ref the ref
     * @param rankList the rank list
     * 
     * @return the rank
     */
    private int getRank(ResolvedConceptReference ref, Map<String,Integer> rankList){
        int rank = 0;
        rank += nullSafeInt(rankList.get(ref.getCode()));

        if(ref.getSourceOf() != null){
            for(Association assoc : ref.getSourceOf().getAssociation()){
                for(AssociatedConcept child : assoc.getAssociatedConcepts().getAssociatedConcept()){
                    rank += nullSafeInt(rankList.get(child.getCode()));
                }
            }
        }

        return rank;
    }
    
    /**
     * Null safe int.
     * 
     * @param integer the integer
     * 
     * @return the int
     */
    private static int nullSafeInt(Integer integer){
        if(integer == null){
            return 0;
        } else {
            return integer;
        }
    }
}