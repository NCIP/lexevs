
package edu.mayo.informatics.lexgrid.convert.validator.error;

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
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getErrorMessage();
    }

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