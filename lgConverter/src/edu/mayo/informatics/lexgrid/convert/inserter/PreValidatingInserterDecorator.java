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

import java.util.List;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.service.exception.CodingSchemeAlreadyLoadedException;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor;
import edu.mayo.informatics.lexgrid.convert.validator.processor.ValidationProcessor;

/**
 * The Class PreValidatingInserterDecorator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PreValidatingInserterDecorator implements ValidatingCodingSchemeInserter {

    /** The validation processor. */
    private ValidationProcessor<CodingScheme> validationProcessor;
    
    /** The resolver processor. */
    private ResolverProcessor resolverProcessor;
    
    /** The delegate. */
    private CodingSchemeInserter delegate;
    
    /**
     * Instantiates a new pre validating inserter decorator.
     * 
     * @param delegate the delegate
     */
    public PreValidatingInserterDecorator(CodingSchemeInserter delegate){
        this.delegate = delegate;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.ValidatingCodingSchemeInserter#insertCodingSchemeWithValidation(org.LexGrid.codingSchemes.CodingScheme)
     */
    public List<ResolvedLoadValidationError> insertCodingSchemeWithValidation(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        List<LoadValidationError> errors = this.validationProcessor.validate(codingScheme);
        List<ResolvedLoadValidationError> resolvedErrors =  this.resolverProcessor.resolve(errors);
        this.insertCodingScheme(codingScheme);
        
        return resolvedErrors;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.inserter.CodingSchemeInserter#insertCodingScheme(org.LexGrid.codingSchemes.CodingScheme)
     */
    public void insertCodingScheme(CodingScheme codingScheme) throws CodingSchemeAlreadyLoadedException {
        delegate.insertCodingScheme(codingScheme);
    }

    /**
     * Gets the validation processor.
     * 
     * @return the validation processor
     */
    public ValidationProcessor<CodingScheme> getValidationProcessor() {
        return validationProcessor;
    }

    /**
     * Sets the validation processor.
     * 
     * @param validationProcessor the new validation processor
     */
    public void setValidationProcessor(ValidationProcessor<CodingScheme> validationProcessor) {
        this.validationProcessor = validationProcessor;
    }

    /**
     * Gets the resolver processor.
     * 
     * @return the resolver processor
     */
    public ResolverProcessor getResolverProcessor() {
        return resolverProcessor;
    }

    /**
     * Sets the resolver processor.
     * 
     * @param resolverProcessor the new resolver processor
     */
    public void setResolverProcessor(ResolverProcessor resolverProcessor) {
        this.resolverProcessor = resolverProcessor;
    }  
}
