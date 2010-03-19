package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public class ErrorResolutionReport {

    public enum ResolutionStatus
    { RESOLVED, 
        RESOLUTION_FAILED, 
        NOT_ADDRESSED }
    
    private ResolutionStatus resolutionStatus;
    
    private LoadValidationError 
        loadValidationError;
       
    private String resolutionDetails;

    public ErrorResolutionReport(ResolutionStatus resolutionStatus, LoadValidationError loadValidationError,
            String resolutionDetails) {
        super();
        this.resolutionStatus = resolutionStatus;
        this.loadValidationError = loadValidationError;
        this.resolutionDetails = resolutionDetails;
    }

    public ResolutionStatus getResolutionStatus() {
        return resolutionStatus;
    }

    public void setResolutionStatus(ResolutionStatus resolutionStatus) {
        this.resolutionStatus = resolutionStatus;
    }


    public LoadValidationError getLoadValidationError() {
        return loadValidationError;
    }


    public void setLoadValidationError(LoadValidationError loadValidationError) {
        this.loadValidationError = loadValidationError;
    }


    public String getResolutionDetails() {
        return resolutionDetails;
    }


    public void setResolutionDetails(String resolutionDetails) {
        this.resolutionDetails = resolutionDetails;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n############################################");
        sb.append("\n        *ERROR RESOLUTION REPORT*");
        sb.append("\n               Root Cause:");
        sb.append(this.getLoadValidationError().getErrorMessage());
        sb.append("\n            Resolution Details:");
        sb.append("\n============================================");
        sb.append("\nResolution Status: " + this.getResolutionStatus());
        sb.append("\nResolution Details: " + this.getResolutionDetails());
        sb.append("\n############################################");
   
        return sb.toString();
    }
}
