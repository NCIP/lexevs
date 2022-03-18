
package edu.mayo.informatics.lexgrid.convert.validator.processor;

import java.util.ArrayList;
import java.util.List;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver;

/**
 * The Class DefaultResolverProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultResolverProcessor implements ResolverProcessor {

    /** The resolvers. */
    private List<Resolver> resolvers = new ArrayList<Resolver>();
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor#addResolver(edu.mayo.informatics.lexgrid.convert.validator.resolution.Resolver)
     */
    public void addResolver(Resolver resolver) {
       resolvers.add(resolver);
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.processor.ResolverProcessor#resolve(java.util.List)
     */
    public List<ResolvedLoadValidationError> resolve(List<? extends LoadValidationError> errors) {
        List<ResolvedLoadValidationError> returnList =
               new ArrayList<ResolvedLoadValidationError>();
        
        for(LoadValidationError error : errors) {
            String errorCode = error.getErrorCode();
            
            List<Resolver> resolvers = getResolversForCode(errorCode);
            
            if(resolvers.size() > 0) {
                for(Resolver resolver : getResolversForCode(errorCode)) {
                    returnList.add(resolver.resolveError(error));
                }
            } else {
                returnList.add(new WrappingLoadValidationError(error));
            }
        }
        return returnList;
    }
    
    /**
     * Gets the resolvers for code.
     * 
     * @param errorCode the error code
     * 
     * @return the resolvers for code
     */
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