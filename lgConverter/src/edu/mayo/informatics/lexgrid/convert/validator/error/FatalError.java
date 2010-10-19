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
package edu.mayo.informatics.lexgrid.convert.validator.error;

/**
 * The Class FatalError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class FatalError extends AbstractError {

    /** The FATA l_ erro r_ code. */
    public static String FATAL_ERROR_CODE = "FATAL-ERROR";
    
    /** The FATA l_ erro r_ description. */
    public static String FATAL_ERROR_DESCRIPTION = "A Fatal Error has occured. This is not recoverable.";
    
    /**
     * Instantiates a new fatal error.
     * 
     * @param errorObject the error object
     * @param exception the exception
     */
    public FatalError(Object errorObject, Exception exception){
        super(FATAL_ERROR_CODE, errorObject, exception);
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        return this.getErrorException().toString();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
        return FATAL_ERROR_DESCRIPTION;
    }
}