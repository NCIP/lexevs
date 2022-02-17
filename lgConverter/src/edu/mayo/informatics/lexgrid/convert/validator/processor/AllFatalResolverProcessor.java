
package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.ArrayList;
import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

public class AllFatalResolverProcessor implements ResolverProcessor {

    public void addResolver(Resolver resolver) {
        throw new UnsupportedOperationException("No adding Resolvers allowed -- this is a special Processor to make all errors Fatal.");
    }

    public List<ResolvedLoadValidationError> resolve(List<? extends LoadValidationError> errors) {
        List<ResolvedLoadValidationError> returnList = new ArrayList<ResolvedLoadValidationError>();
        
        for(LoadValidationError error : errors) {
            returnList.add(new WrappingLoadValidationError(error));
        }
        
        return returnList;
    }
}