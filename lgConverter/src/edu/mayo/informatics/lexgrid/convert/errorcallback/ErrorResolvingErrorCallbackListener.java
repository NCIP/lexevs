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
package edu.mayo.informatics.lexgrid.convert.errorcallback;

import java.util.Arrays;
import java.util.List;

import org.lexevs.dao.database.service.error.DatabaseError;
import org.lexevs.dao.database.service.error.ErrorCallbackListener;
import org.lexevs.logging.LoggerFactory;

import edu.mayo.informatics.lexgrid.convert.validator.error.ResolvedLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.WrappingLoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.processor.DefaultResolverProcessor;

public class ErrorResolvingErrorCallbackListener extends DefaultResolverProcessor implements ErrorCallbackListener {

    @Override
    public void onDatabaseError(DatabaseError databaseError) {
        List<ResolvedLoadValidationError> errors = this.resolve(Arrays.asList(new WrappingLoadValidationError(databaseError)));
        
        for(ResolvedLoadValidationError resolvedError : errors) {
               LoggerFactory.getLogger().loadLogError(resolvedError.toString(), null);
        }   
    }
}