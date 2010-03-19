package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

public interface ResolverProcessor {

    public void addResolver(Resolver resolver);
    
    public List<ErrorResolutionReport> resolve(List<LoadValidationError> errors);
}
