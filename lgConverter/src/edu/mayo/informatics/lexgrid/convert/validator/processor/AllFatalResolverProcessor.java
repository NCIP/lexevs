/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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