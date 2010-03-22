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

import java.util.Date;

import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

/**
 * The Class WrappingdLoadValidationError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class WrappingLoadValidationError implements ResolvedLoadValidationError {
    
    /** The UNRESOLVE d_ details. */
    private static String UNRESOLVED_DETAILS = "NONE";
    
    /** The error. */
    private LoadValidationError error;
    
    /** The report. */
    private ErrorResolutionReport report;
    
    /**
     * Instantiates a new wrappingd load validation error.
     * 
     * @param error the error
     * @param report the report
     */
    public WrappingLoadValidationError(LoadValidationError error, ErrorResolutionReport report){
        this.error = error;
        this.report = report;
    }
    
    /**
     * Instantiates a new wrappingd load validation error.
     * 
     * @param error the error
     */
    public WrappingLoadValidationError(LoadValidationError error){
        this.error = error;
        this.report = buildDefaultUnresolvedReport();
    }
    
    /**
     * Builds the default unresolved report.
     * 
     * @return the error resolution report
     */
    protected ErrorResolutionReport buildDefaultUnresolvedReport() {
       return new ErrorResolutionReport(ResolutionStatus.NOT_ADDRESSED, UNRESOLVED_DETAILS);
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError#getErrorResolutionReport()
     */
    public ErrorResolutionReport getErrorResolutionReport() {
       return report;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorCode()
     */
    public String getErrorCode() {
        return error.getErrorCode();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
      return error.getErrorDescription();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorMessage()
     */
    public String getErrorMessage() {
       return error.getErrorMessage();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorObject()
     */
    public Object getErrorObject() {
       return error.getErrorObject();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getSeverity()
     */
    public Severity getSeverity() {
        return error.getSeverity();
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getUniqueErrorId()
     */
    public String getUniqueErrorId() {
       return error.getUniqueErrorId();
    }
    
    public Exception getErrorException() {
        return error.getErrorException();
    }
    
    public Date getErrorTime() {
        return error.getErrorTime();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append("\n####################################################");
        sb.append(error.toString());
        sb.append("\n");
        sb.append(report.toString());
        sb.append("\n####################################################");
        
        return sb.toString();
    }
}
