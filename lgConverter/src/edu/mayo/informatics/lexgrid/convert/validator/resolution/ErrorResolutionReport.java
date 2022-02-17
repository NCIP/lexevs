
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