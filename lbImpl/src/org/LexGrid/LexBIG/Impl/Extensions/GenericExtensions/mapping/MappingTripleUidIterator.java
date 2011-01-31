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

/**
 * The Class MappingTripleUidIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingTripleUidIterator extends AbstractPageableIterator<String> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    /** The uri. */
    private String uri;
    
    /** The version. */
    private String version;
    
    /** The relations container name. */
    private String relationsContainerName;
    
    /** The sort option list. */
    private List<MappingSortOption> sortOptionList;
    
    /** The refs. */
    private MappingAbsoluteCodingSchemeVersionReferences refs;
    
    /**
     * Instantiates a new mapping triple uid iterator.
     */
    public MappingTripleUidIterator() {
        super();
    }
    
    /**
     * Instantiates a new mapping triple uid iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param refs the refs
     * @param sortOptionList the sort option list
     * 
     * @throws LBParameterException the LB parameter exception
     */
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
    
    /* (non-Javadoc)
     * @see org.lexevs.paging.AbstractPageableIterator#doPage(int, int)
     */
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