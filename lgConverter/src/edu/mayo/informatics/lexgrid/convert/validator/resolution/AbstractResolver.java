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
package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

/**
 * The Class AbstractResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractResolver<T> implements Resolver {
    
    /** The UNRESOLVE d_ details. */
    public static String UNRESOLVED_DETAILS = "No Action Taken.";

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver#isResolverValidForError(java.lang.String)
     */
    public boolean isResolverValidForError(String errorCode) {
        return doGetValidErrorCodes().contains(errorCode);
    }
    
    /**
     * Do get valid error codes.
     * 
     * @return the list< string>
     */
    protected abstract List<String> doGetValidErrorCodes();

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver#resolveError(edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError)
     */
    @SuppressWarnings("unchecked")
    public ResolvedLoadValidationError resolveError(LoadValidationError error) {
        
        boolean resolved = doResolveError( (T) error.getErrorObject() );
        
        ResolutionStatus status;
        String details;
        if(resolved) {
            status = ResolutionStatus.RESOLVED;
            details = this.getResolutionDetails();
        } else {
            status = ResolutionStatus.RESOLUTION_FAILED;
            details = this.getUnresolvedDetails();
        }
        ErrorResolutionReport report = new ErrorResolutionReport(
                status,
                details);
        
        return new WrappingLoadValidationError(error, report);
    }
    
    /**
     * Do resolve error.
     * 
     * @param errorObject the error object
     * 
     * @return true, if successful
     */
    public abstract boolean doResolveError(T errorObject);
    
    /**
     * Gets the resolution details.
     * 
     * @return the resolution details
     */
    public abstract String getResolutionDetails();
    
    /**
     * Gets the unresolved details.
     * 
     * @return the unresolved details
     */
    public String getUnresolvedDetails() {
        return UNRESOLVED_DETAILS;
    }
}
