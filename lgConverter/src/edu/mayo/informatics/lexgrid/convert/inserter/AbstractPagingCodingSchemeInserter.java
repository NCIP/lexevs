/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entities;
import org.LexGrid.relations.Relations;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;

/**
 * The Class AbstractPagingCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractPagingCodingSchemeInserter extends AbstractCodingSchemeInserter implements PagingCodingSchemeInserter{

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractCodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    public List<ResolvedLoadValidationError> insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
 
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        errors.addAll(loadNonPagedItems(codingScheme));
        
        String uri = codingScheme.getCodingSchemeURI();
        String version = codingScheme.getRepresentsVersion();
        
        errors.addAll(pageEntities(uri, version, codingScheme.getEntities()));
        
        for(Relations relations : codingScheme.getRelations()) {
            errors.addAll(pageRelations(uri, version, relations));
        }
        
        return doResolveErrors(errors);
    }
    
    protected abstract List<ResolvedLoadValidationError> doResolveErrors(List<LoadValidationError> errors);
    
    /**
     * Load non paged items.
     * 
     * @param codingScheme the coding scheme
     */
    protected abstract List<LoadValidationError> loadNonPagedItems(CodingScheme codingScheme);
    
    /**
     * Page entities.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param codingSchemeVersion the coding scheme version
     * @param entities the entities
     */
    protected abstract List<LoadValidationError> pageEntities(String codingSchemeUri, String codingSchemeVersion, Entities entities);
    
    /**
     * Page relations.
     * 
     * @param codingSchemeUri the coding scheme uri
     * @param codingSchemeVersion the coding scheme version
     * @param relations the relations
     */
    protected abstract List<LoadValidationError> pageRelations(String codingSchemeUri, String codingSchemeVersion, Relations relations);
}
