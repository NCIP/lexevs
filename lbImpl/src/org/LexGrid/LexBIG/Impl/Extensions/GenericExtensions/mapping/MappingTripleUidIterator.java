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

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.MappingTripleIterator.MappingAbsoluteCodingSchemeVersionReferences;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.paging.AbstractPageableIterator;

public class MappingTripleUidIterator extends AbstractPageableIterator<String> {

    private static final long serialVersionUID = 5709428653655124881L;

    private String uri;
    private String version;
    private String relationsContainerName;
    private List<MappingSortOption> sortOptionList;
    private MappingAbsoluteCodingSchemeVersionReferences refs;
    
    public MappingTripleUidIterator() {
        super();
    }
    
    public MappingTripleUidIterator(
            String uri, 
            String version,
            String relationsContainerName, 
            MappingAbsoluteCodingSchemeVersionReferences refs,
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        super(MappingExtensionImpl.PAGE_SIZE);
        
        this.uri = uri;
        this.version = version;
        this.relationsContainerName = relationsContainerName;
        this.refs = refs;
        this.sortOptionList = sortOptionList;
    }
    
    @Override
    protected List<? extends String> doPage(int currentPosition, int pageSize) {
      
        return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodedNodeGraphService().
            getTripleUidsForMappingRelationsContainer(
                    uri, 
                    version, 
                    refs.getSourceCodingScheme(),
                    refs.getTargetCodingScheme(),
                    relationsContainerName, 
                    DaoUtility.mapMappingSortOptionListToSort(sortOptionList).getSorts(),
                    currentPosition, 
                    pageSize);
    } 
}