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

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;

/**
 * The Class MappingTripleIterator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MappingTripleIterator extends AbstractMappingTripleIterator<Iterator<String>> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 5709428653655124881L;

    private List<MappingSortOption> sortOptionList;
            
    /**
     * Instantiates a new mapping triple iterator.
     * 
     * @param uri the uri
     * @param version the version
     * @param relationsContainerName the relations container name
     * @param sortOptionList the sort option list
     * 
     * @throws LBParameterException the LB parameter exception
     */
    public MappingTripleIterator(
            String uri, 
            String version,
            String relationsContainerName,
            List<MappingSortOption> sortOptionList) throws LBParameterException {
        super(uri,version, relationsContainerName);
        this.sortOptionList = sortOptionList;
        this.initializetMappingTripleIterator();
    }

    @Override
    protected Iterator<String> createTripleIterator() throws Exception {
        return new MappingTripleUidIterator(
                getUri(), 
                getVersion(), 
                getRelationsContainerName(),
                getRefs(), 
                sortOptionList);
    }
    
    

}