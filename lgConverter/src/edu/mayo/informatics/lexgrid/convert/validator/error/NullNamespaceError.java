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
package edu.mayo.informatics.lexgrid.convert.validator.error;

import org.LexGrid.concepts.Entity;
import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;

/**
 * The Class NullNamespaceError.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NullNamespaceError extends AbstractError {

    /** The NUL l_ namespac e_ code. */
    public static String NULL_NAMESPACE_CODE = "NULL-NAMESPACE";
    
    /** The null namespace object. */
    private Object nullNamespaceObject;
    
    /**
     * Instantiates a new null namespace error.
     * 
     * @param nullNamespaceObject the null namespace object
     */
    public NullNamespaceError(Object nullNamespaceObject) {
        this.nullNamespaceObject = nullNamespaceObject;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorCode()
     */
    public String getErrorCode() {
        return NULL_NAMESPACE_CODE;
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorObject()
     */
    public Object getErrorObject() {
        return nullNamespaceObject;
    }
    
    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError#getErrorDescription()
     */
    public String getErrorDescription() {
        return "A namespace is required.";
    }

    /* (non-Javadoc)
     * @see edu.mayo.informatics.lexgrid.convert.validator.error.AbstractError#getErrorObjectDescription()
     */
    @Override
    protected String getErrorObjectDescription() {
        if(nullNamespaceObject instanceof Entity) {
            return "Code: " + ((Entity)nullNamespaceObject).getEntityCode();
        } else if(nullNamespaceObject instanceof AssociationSource) {
            return "Code: " + ((AssociationSource)nullNamespaceObject).getSourceEntityCode();
        } else if(nullNamespaceObject instanceof AssociationTarget) {
            return "Code: " +  ((AssociationTarget)nullNamespaceObject).getTargetEntityCode();
        } else {
            return nullNamespaceObject.toString();
        }
    }
}
