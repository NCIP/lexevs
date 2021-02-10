
package edu.mayo.informatics.lexgrid.convert.validator.error;

import java.util.Date;

import org.lexevs.dao.database.service.error.DatabaseError;

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
    
    public WrappingLoadValidationError(DatabaseError error){
        this(new LoadValidationErrorWrapper(error));
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
        sb.append("\n" + this.getErrorMessage());
        sb.append("\n" + this.getErrorResolutionReport());
        sb.append("\n####################################################");
        
        return sb.toString();
    }
    
    private static class LoadValidationErrorWrapper implements LoadValidationError {
        
        private DatabaseError error;
        
        private LoadValidationErrorWrapper(DatabaseError error) {
            this.error = error;
        }

        @Override
        public String getErrorDescription() {
            return error.getErrorDescription();
        }

        @Override
        public String getErrorMessage() {
            return error.getErrorMessage();
        }

        @Override
        public Severity getSeverity() {
            return Severity.UNKNOWN;
        }

        @Override
        public String getErrorCode() {
            return error.getErrorCode();
        }

        @Override
        public Exception getErrorException() {
           return error.getErrorException();
        }

        @Override
        public Object getErrorObject() {
            return error.getErrorObject();
        }

        @Override
        public Date getErrorTime() {
            return error.getErrorTime();
        }

        @Override
        public String getUniqueErrorId() {
            return error.getUniqueErrorId();
        }
    }
}