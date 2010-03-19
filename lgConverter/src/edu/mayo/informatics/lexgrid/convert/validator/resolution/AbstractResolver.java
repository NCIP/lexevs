package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport.ResolutionStatus;

public abstract class AbstractResolver<T> implements Resolver {
    
    public static String UNRESOLVED_DETAILS = "No Action Taken.";

    public boolean isResolverValidForError(String errorCode) {
        return doGetValidErrorCodes().contains(errorCode);
    }
    
    protected abstract List<String> doGetValidErrorCodes();

    @SuppressWarnings("unchecked")
    public ErrorResolutionReport resolveError(LoadValidationError error) {
        
        boolean resolved = doResolveError( (T) error.getErrorObject() );
        
        ResolutionStatus status;
        String details;
        if(resolved) {
            status = ResolutionStatus.RESOLVED;
            details = this.getResolutionDetails();
        } else {
            status = ResolutionStatus.RESOLUTION_FAILED;
            details = this.getUnresolvedDetails();
        }
        return new ErrorResolutionReport(
                status,
                error,
                details);
    }
    
    public abstract boolean doResolveError(T errorObject);
    
    public abstract String getResolutionDetails();
    
    public String getUnresolvedDetails() {
        return UNRESOLVED_DETAILS;
    }
}
