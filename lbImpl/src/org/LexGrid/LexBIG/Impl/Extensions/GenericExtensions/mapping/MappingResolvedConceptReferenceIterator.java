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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
import org.LexGrid.annotations.LgClientSideSafe;
@LgClientSideSafe
public class MappingResolvedConceptReferenceIterator extends IteratorBackedResolvedConceptReferencesIterator{

    /**
     * 
     */
    private static final long serialVersionUID = -698270712034240196L;
    private boolean areAllCodedNodeSetsNull;
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



    
    public ResolvedConceptReferenceList get(int start, int end) throws LBResourceUnavailableException,
            LBInvocationException, LBParameterException {

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
