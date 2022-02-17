
package edu.mayo.informatics.lexgrid.convert.validator.resolution;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;

/**
 * The Interface Resolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface Resolver {

    /**
     * Resolve error.
     * 
     * @param error the error
     * 
     * @return the resolved load validation error
     */
    public ResolvedLoadValidationError resolveError(LoadValidationError error);
    
    /**
     * Checks if is resolver valid for error.
     * 
     * @param errorCode the error code
     * 
     * @return true, if is resolver valid for error
     */
    public boolean isResolverValidForError(String errorCode);
}