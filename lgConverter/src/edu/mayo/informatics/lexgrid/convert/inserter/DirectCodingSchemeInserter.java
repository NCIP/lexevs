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
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;
import org.lexevs.dao.database.utility.DaoUtility;

import edu.mayo.informatics.lexgrid.convert.validator.error.FatalError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;

/**
 * The Class DirectCodingSchemeInserter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DirectCodingSchemeInserter extends AbstractCodingSchemeInserter {

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.AbstractCodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    public List<ResolvedLoadValidationError> insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        try {
            super.getDatabaseServiceManager().
            getCodingSchemeService().
            insertCodingScheme(codingScheme);
            
            return new ArrayList<ResolvedLoadValidationError>();
            
        } catch (Exception e) {
            return DaoUtility.createList(ResolvedLoadValidationError.class,
                      new WrappingLoadValidationError(
                              new FatalError(codingScheme, e)));
        }
    } 
}
