package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public abstract class AbstractResolver<T> implements Resolver {

    public boolean isResolverValidForError(String errorCode) {
        return doGetValidErrorCodes().contains(errorCode);
    }
    
    protected abstract List<String> doGetValidErrorCodes();

    @SuppressWarnings("unchecked")
    public void resolveError(LoadValidationError error) {
        
        doResolveError( (T) error.getErrorObject() );
    }
    
    public abstract void doResolveError(T errorObject);
}
