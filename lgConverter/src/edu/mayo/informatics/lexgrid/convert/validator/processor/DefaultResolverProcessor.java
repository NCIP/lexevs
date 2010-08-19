/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
