package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;

public interface Resolver {

    public void resolveError(LoadValidationError error);
    
    public boolean isResolverValidForError(String errorCode);
}
