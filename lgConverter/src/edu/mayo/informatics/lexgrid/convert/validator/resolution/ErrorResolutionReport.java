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
package edu.mayo.informatics.lexgrid.convert.validator.resolution;

/**
 * The Class ErrorResolutionReport.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ErrorResolutionReport {

    /**
     * The Enum ResolutionStatus.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public enum ResolutionStatus
    { 
        /** The RESOLVED. */
        RESOLVED, 

        PARTIALLY_RESOLVED, 
        
        /** The RESOLUTIO n_ failed. */
        RESOLUTION_FAILED, 
        
        /** The NO t_ addressed. */
        NOT_ADDRESSED }
    
    /** The resolution status. */
    private ResolutionStatus resolutionStatus;
       
    /** The resolution details. */
    private String resolutionDetails;

    /**
     * Instantiates a new error resolution report.
     * 
     * @param resolutionStatus the resolution status
     * @param resolutionDetails the resolution details
     */
    public ErrorResolutionReport(ResolutionStatus resolutionStatus,
            String resolutionDetails) {
        super();
        this.resolutionStatus = resolutionStatus;
        this.resolutionDetails = resolutionDetails;
    }

    /**
     * Gets the resolution status.
     * 
     * @return the resolution status
     */
    public ResolutionStatus getResolutionStatus() {
        return resolutionStatus;
    }

    /**
     * Sets the resolution status.
     * 
     * @param resolutionStatus the new resolution status
     */
    public void setResolutionStatus(ResolutionStatus resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }

    /**
     * Gets the resolution details.
     * 
     * @return the resolution details
     */
    public String getResolutionDetails() {
        return resolutionDetails;
    }

    /**
     * Sets the resolution details.
     * 
     * @param resolutionDetails the new resolution details
     */
    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        
        StringBuffer sb = new StringBuffer();
        
        sb.append("\n        *ERROR RESOLUTION REPORT*");
        sb.append("\n            Resolution Details:");
        sb.append("\n");
        sb.append("\nResolution Status: " + this.getResolutionStatus());
        sb.append("\nResolution Details: " + this.getResolutionDetails());
   
        return sb.toString();
    }
}