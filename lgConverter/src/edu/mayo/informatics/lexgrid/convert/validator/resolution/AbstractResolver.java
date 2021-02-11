
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
        
        ResolutionStatus status = doResolveError( (T) error.getErrorObject() );

        String details = this.getResolutionDetails();
      
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
    public abstract ResolutionStatus doResolveError(T errorObject);
    
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