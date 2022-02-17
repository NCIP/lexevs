
package edu.mayo.informatics.lexgrid.convert.validator.error;

import org.lexevs.dao.database.service.error.DatabaseError;

/**
 * The Interface LoadValidationError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LoadValidationError extends DatabaseError {
    
    /**
     * The Enum Severity.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public enum Severity {
            /** The FATAL. */
            FATAL, 
            /** The NO n_ fatal. */
            NON_FATAL, 
            /** The UNKNOWN. */
            UNKNOWN}
    
    /**
     * Gets the severity.
     * 
     * @return the severity
     */
    public Severity getSeverity();
}