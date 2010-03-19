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
package edu.mayo.informatics.lexgrid.convert.validator;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.lang.StringUtils;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.error.NullNamespaceError;

/**
 * The Class NullNamespaceValidator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullNamespaceValidator extends AbstractValidator<Object> {
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.AbstractValidator#doGetValidClasses()
     */
    @Override
    protected List<Class<?>> doGetValidClasses() {
        List<Class<?>> clazzes = new ArrayList<Class<?>>();
        clazzes.add(Entity.class);
        clazzes.add(AssociationSource.class);
        clazzes.add(AssociationTarget.class);
        
        return clazzes;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.AbstractValidator#doValidate(java.lang.Object, java.util.List)
     */
    @Override
    public void doValidate(Object object, List<LoadValidationError> errors) {
        String namespace = null;

        if(object instanceof Entity) {
            namespace =((Entity)object).getEntityCodeNamespace();
        } else if(object instanceof AssociationSource) {
            namespace = ((AssociationSource)object).getSourceEntityCodeNamespace();
        } else if(object instanceof AssociationTarget) {
            namespace = ((AssociationTarget)object).getTargetEntityCodeNamespace();
        } 
        
        if(StringUtils.isBlank(namespace)) {
            errors.add(new NullNamespaceError(object));
        }
    }
}
