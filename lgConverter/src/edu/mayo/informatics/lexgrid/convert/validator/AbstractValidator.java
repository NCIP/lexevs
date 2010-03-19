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
package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.ArrayList;
import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

/**
 * The Class AbstractValidator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractValidator<T> implements Validator {

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.Validator#validate(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    public List<LoadValidationError> validate(Object obj) {
        List<LoadValidationError> errors = new ArrayList<LoadValidationError>();
        
        doValidate( (T)obj, errors);
        
        return errors;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.Validator#isValidClassForValidator(java.lang.Class)
     */
    public boolean isValidClassForValidator(Class<?> clazz) {
        for(Class<?> validClazz : doGetValidClasses()) {
            if(validClazz.isAssignableFrom(clazz)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Do get valid classes.
     * 
     * @return the list< class<?>>
     */
    protected abstract List<Class<?>> doGetValidClasses();
    
    /**
     * Do validate.
     * 
     * @param object the object
     * @param errors the errors
     */
    public abstract void doValidate(T object, List<LoadValidationError> errors);

}
