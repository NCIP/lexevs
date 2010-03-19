package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.ArrayList;
import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

public class DefaultResolverProcessor implements ResolverProcessor {

    private List<Resolver> resolvers = new ArrayList<Resolver>();
    
    public void addResolver(Resolver resolver) {
       resolvers.add(resolver);
    }

    public List<ErrorResolutionReport> resolve(List<LoadValidationError> errors) {
        List<ErrorResolutionReport> returnList =
               new ArrayList<ErrorResolutionReport>();
        
        for(LoadValidationError error : errors) {
            String errorCode = error.getErrorCode();
            for(Resolver resolver : getResolversForCode(errorCode)) {
                returnList.add(resolver.resolveError(error));
            }
        }
        return returnList;
    }
    
    protected List<Resolver> getResolversForCode(String errorCode) {
        List<Resolver> returnList = new ArrayList<Resolver>();
        
        for(Resolver resolver : resolvers) {
            if(resolver.isResolverValidForError(errorCode)) {
                returnList.add(resolver);
            }
        }
        return returnList;
    }

}
