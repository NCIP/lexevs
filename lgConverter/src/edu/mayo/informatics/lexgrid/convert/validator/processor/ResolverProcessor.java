
package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

/**
 * The Interface ResolverProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ResolverProcessor {

    /**
     * Adds the resolver.
     * 
     * @param resolver the resolver
     */
    public void addResolver(Resolver resolver);
    
    /**
     * Resolve.
     * 
     * @param errors the errors
     * 
     * @return the list< resolved load validation error>
     */
    public List<ResolvedLoadValidationError> resolve(List<? extends LoadValidationError> errors);
}