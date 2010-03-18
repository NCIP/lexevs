package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

public interface ResolverProcessor {

    public void addResolver(Resolver resolver);
    
    public void resolve(List<LoadValidationError> errors);
}
