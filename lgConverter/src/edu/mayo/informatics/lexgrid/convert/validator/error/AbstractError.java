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

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.lexevs.dao.database.service.error.DefaultDatabaseError;

/**
 * The Class AbstractError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractError extends DefaultDatabaseError implements LoadValidationError {
    
    /** The severity. */
    private Severity severity = Severity.UNKNOWN;
  
    protected AbstractError(Object errorObject) {
        this(null, errorObject, null);
    }
    
    protected AbstractError(String errorCode, Object errorObject, Exception errorException) {
        super(errorCode, errorObject, errorException);
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorMessage()
     */
    public String getErrorMessage() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n             *ERROR REPORT*");
        sb.append("\n");
        sb.append("\nError Code: " + this.getErrorCode());
        sb.append("\nUnique Error Id: " + this.getUniqueErrorId());
        sb.append("\nDescription: " + this.getErrorDescription() );
        sb.append("\n -- Caused By Object with Description: ");
        sb.append("\n --- " + this.getErrorObjectDescription());
        sb.append("\nException (if any)");
        if(super.getErrorException() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            super.getErrorException().printStackTrace(pw);
            pw.flush();
            sw.flush();
            sb.append(sw.getBuffer());
            pw.close();
            try {
                sw.close();
            } catch (IOException e) {
                //
            }
        }
        
        return sb.toString();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getErrorMessage();
    }
    
    /**
     * Gets the error object description.
     * 
     * @return the error object description
     */
    protected abstract String getErrorObjectDescription();

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getSeverity()
     */
    public Severity getSeverity() {
        return severity;
    }

    /**
     * Sets the severity.
     * 
     * @param severity the new severity
     */
    public void setSeverity(Severity severity) {
        this.severity = severity;
    }
}
