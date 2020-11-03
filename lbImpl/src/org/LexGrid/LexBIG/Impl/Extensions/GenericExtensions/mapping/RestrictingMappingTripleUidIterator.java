/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.AbstractMappingTripleIterator.MappingAbsoluteCodingSchemeVersionReferences;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractRefereshingPageableIterator;

/**
 * The Class RestrictingMappingTripleUidIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RestrictingMappingTripleUidIterator extends AbstractRefereshingPageableIterator<Map<String,ResolvedConceptReferencesIterator>,String> {
    
    protected static ConceptReference INVALID_CONCEPT_REFERENCE = new ConceptReference();
    static {
        INVALID_CONCEPT_REFERENCE.setCode("__INVALID__CONCEPT__REFERENCE__");
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    /** The uri. */
    private String uri;
    
    /** The version. */
    private String version;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /** The resolved concept references iterator. */
    private ResolvedConceptReferencesIterator sourceCodesResolvedConceptReferencesIterator;
    
    private ResolvedConceptReferencesIterator targetCodesResolvedConceptReferencesIterator;
    
    private ResolvedConceptReferencesIterator sourceOrTargetCodesResolvedConceptReferencesIterator;
    
    /** The refs. */
    private MappingAbsoluteCodingSchemeVersionReferences refs;
    
    public static int PAGE_SIZE = 50;
    
    private List<ConceptReference> inOrderConceptReferences = new ArrayList<ConceptReference>();
    
    private List<MappingSortOption> sortOptionList;
    
    private List<ConceptReference> sourceResolvedIteratorConceptReferences;
    
    private List<ConceptReference> targetResolvedIteratorConceptReferences;
    
    private List<ConceptReference> sourceOrTargetResolvedIteratorConceptReferences;
    
    private static String SOURCE_ITERATOR = "source";
    private static String TARGET_ITERATOR = "target";
    private static String BOTH_ITERATOR = "both";
    
    private boolean nullsortOptionListPageComplete = false;
    
    public RestrictingMappingTripleUidIterator(){
        super();
    }
 
    /**
     * Instantiates a new restricting mapping triple uid iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param codedNodeSet the coded node set
     * 
     * @throws LBException the LB exception
     */
    public RestrictingMappingTripleUidIterator(
            String uri, 
            String version,
            String relationsContainerName, 
            MappingAbsoluteCodingSchemeVersionReferences refs,
            CodedNodeSet sourceCodesCodedNodeSet,
            CodedNodeSet targetCodesCodedNodeSet,
            CodedNodeSet sourceOrTargetCodesCodedNodeSet,
            List<MappingSortOption> sortOptionList) throws LBException {
        super(PAGE_SIZE);

        this.uri = uri;
        this.version = version;
        this.relationsContainerName = relationsContainerName;
        this.refs = refs;
        this.sortOptionList = sortOptionList;

        if(sourceCodesCodedNodeSet != null){
            this.sourceCodesResolvedConceptReferencesIterator = sourceCodesCodedNodeSet.resolve(null, null, null, null, false);
        }
        if(targetCodesCodedNodeSet != null){
            this.targetCodesResolvedConceptReferencesIterator = targetCodesCodedNodeSet.resolve(null, null, null, null, false);
        }
        if(sourceOrTargetCodesCodedNodeSet != null){
            this.sourceOrTargetCodesResolvedConceptReferencesIterator = sourceOrTargetCodesCodedNodeSet.resolve(null, null, null, null, false);
        }

            this.sourceResolvedIteratorConceptReferences = 
                this.buildResolvedConceptReferenceList(sourceCodesResolvedConceptReferencesIterator, PAGE_SIZE);

            this.targetResolvedIteratorConceptReferences = 
                this.buildResolvedConceptReferenceList(targetCodesResolvedConceptReferencesIterator, PAGE_SIZE);

            this.sourceOrTargetResolvedIteratorConceptReferences = 
                this.buildResolvedConceptReferenceList(sourceOrTargetCodesResolvedConceptReferencesIterator, PAGE_SIZE);

    } 

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
    @Override
    protected List<? extends String> doPage(int currentPosition, int pageSize) {
        try {
            if(!hasMoreToPage()){
                return null;
            }
        } catch (LBResourceUnavailableException e) {
            throw new RuntimeException(e);
        }
        
        List<ConceptReference> sourceConceptReferences;
        List<ConceptReference> targetConceptReferences;
        List<ConceptReference> sourceOrTargetConceptReferences;

        sourceConceptReferences = getConceptReferencesForPage(pageSize, SourceOrTarget.SOURCE);
        targetConceptReferences = getConceptReferencesForPage(pageSize, SourceOrTarget.TARGET);
        sourceOrTargetConceptReferences = getConceptReferencesForPage(pageSize, SourceOrTarget.SOURCE_OR_TARGET);


            return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService().
                getTripleUidsForMappingRelationsContainerForCodes(
                        uri, 
                        version, 
                        refs.getSourceCodingScheme(),
                        refs.getTargetCodingScheme(),
                        relationsContainerName, 
                        sourceConceptReferences,
                        targetConceptReferences,
                        sourceOrTargetConceptReferences,
                        DaoUtility.mapMappingSortOptionListToSort(sortOptionList).getSorts(),
                        currentPosition,
                        pageSize);

    }
    
    private boolean hasMoreToPage() throws LBResourceUnavailableException{
        if(isSortingEnabled()){
            if(!this.nullsortOptionListPageComplete){
                this.nullsortOptionListPageComplete = true;
                return true;
            } else {
                return false;
            }
        } else {
            return ((this.sourceResolvedIteratorConceptReferences != null && 
                    this.sourceResolvedIteratorConceptReferences.size() > 0)
                    ||
                   (this.targetResolvedIteratorConceptReferences != null && 
                    this.targetResolvedIteratorConceptReferences.size() > 0)
                    ||
                   (this.sourceOrTargetResolvedIteratorConceptReferences != null && 
                    this.sourceOrTargetResolvedIteratorConceptReferences.size() > 0));
        }
    }
    
    private enum SourceOrTarget {SOURCE,TARGET,SOURCE_OR_TARGET}
    
    /**
     * Gets the concept references for page.
     * 
     * @param pageSize the page size
     * 
     * @return the concept references for page
     */
    private List<ConceptReference> getConceptReferencesForPage(int pageSize, SourceOrTarget sourceOrTarget){

            switch (sourceOrTarget){
                case SOURCE: {
                    return this.sourceResolvedIteratorConceptReferences;
                }
                case TARGET: {
                    return this.targetResolvedIteratorConceptReferences;
                }
                case SOURCE_OR_TARGET: {
                    return this.sourceOrTargetResolvedIteratorConceptReferences;
                }
                default: {
                    throw new RuntimeException(sourceOrTarget + " not recognized.");
                }
            }

    }
    
    public List<ConceptReference> buildResolvedConceptReferenceList(ResolvedConceptReferencesIterator iterator, int pageSize) throws LBResourceUnavailableException, LBInvocationException{
        
        if(iterator == null){
            return null;
        } else {
            
            List<ConceptReference> list = new ArrayList<ConceptReference>();
            if(!isSortingEnabled()){
                if(iterator.hasNext()){
                    list = DaoUtility.createList(ConceptReference.class, iterator.next(pageSize).getResolvedConceptReference());
                }
            } else {
                while(iterator.hasNext()){ 
                    list.addAll(
                            DaoUtility.createList(ConceptReference.class, iterator.next(pageSize).getResolvedConceptReference()));
                }
            }
            
            //If the list is empty, that means the restriction restricted everything.
            //We must then pass in an invalid code to prevent matches, because by default
            //an empty list will match all.
            if(list.isEmpty()){
                list.add(INVALID_CONCEPT_REFERENCE);
            }

            if(!isSortingEnabled()){
                inOrderConceptReferences.addAll(list);
            }
            
            return list;
        }
    }

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractRefereshingPageableIterator#doGetRefresh()
     */
    @Override
    protected Map<String,ResolvedConceptReferencesIterator> doGetRefresh() {
        Map<String,ResolvedConceptReferencesIterator> map = new HashMap<String,ResolvedConceptReferencesIterator>();
        map.put(SOURCE_ITERATOR, sourceCodesResolvedConceptReferencesIterator);
        map.put(TARGET_ITERATOR, targetCodesResolvedConceptReferencesIterator);
        map.put(BOTH_ITERATOR, sourceOrTargetCodesResolvedConceptReferencesIterator);
        
        return map;
    }
    
    private boolean isSortingEnabled(){
        return CollectionUtils.isNotEmpty(this.sortOptionList);
    }

    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractRefereshingPageableIterator#doRefresh(java.lang.Object)
     */
    @Override
    protected void doRefresh(Map<String,ResolvedConceptReferencesIterator> refresh) {
        this.sourceCodesResolvedConceptReferencesIterator = refresh.get(SOURCE_ITERATOR);
        this.targetCodesResolvedConceptReferencesIterator = refresh.get(TARGET_ITERATOR);
        this.sourceOrTargetCodesResolvedConceptReferencesIterator = refresh.get(BOTH_ITERATOR);
    }

    protected List<ConceptReference> getInOrderConceptReferences() {
        return inOrderConceptReferences;
    }

    protected List<ConceptReference> getSourceResolvedIteratorConceptReferences() {
        return sourceResolvedIteratorConceptReferences;
    }

    protected List<ConceptReference> getTargetResolvedIteratorConceptReferences() {
        return targetResolvedIteratorConceptReferences;
    }

    protected ResolvedConceptReferencesIterator getSourceCodesResolvedConceptReferencesIterator() {
        return sourceCodesResolvedConceptReferencesIterator;
    }

    protected ResolvedConceptReferencesIterator getTargetCodesResolvedConceptReferencesIterator() {
        return targetCodesResolvedConceptReferencesIterator;
    }

    protected ResolvedConceptReferencesIterator getSourceOrTargetCodesResolvedConceptReferencesIterator() {
        return sourceOrTargetCodesResolvedConceptReferencesIterator;
    }

    protected List<ConceptReference> getSourceOrTargetResolvedIteratorConceptReferences() {
        return sourceOrTargetResolvedIteratorConceptReferences;
    }
}