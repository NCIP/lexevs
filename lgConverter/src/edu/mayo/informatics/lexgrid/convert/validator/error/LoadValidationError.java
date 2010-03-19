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
package edu.mayo.informatics.lexgrid.convert.validator.error;

/**
 * The Interface LoadValidationError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LoadValidationError {
    
    /**
     * The Enum Severity.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public enum Severity {/** The FATAL. */
FATAL, /** The NO n_ fatal. */
 NON_FATAL, /** The UNKNOWN. */
 UNKNOWN}
    
    /**
     * Gets the unique error id.
     * 
     * @return the unique error id
     */
    public String getUniqueErrorId();
    
    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public String getErrorMessage();
    
    /**
     * Gets the error code.
     * 
     * @return the error code
     */
    public String getErrorCode();
    
    /**
     * Gets the error description.
     * 
     * @return the error description
     */
    public String getErrorDescription();
    
    /**
     * Gets the error object.
     * 
     * @return the error object
     */
    public Object getErrorObject();
    
    /**
     * Gets the severity.
     * 
     * @return the severity
     */
    public Severity getSeverity();
}
