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
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.Serializable;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Query.Filter;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;

/**
 * The Interface CodeToReturnResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodeToReturnResolver extends Serializable {

    /**
     * Builds the resolved concept reference.
     * 
     * @param codeToReturn the code to return
     * @param restrictToProperties the restrict to properties
     * @param restrictToPropertyTypes the restrict to property types
     * @param filters the filters
     * @param resolve the resolve
     * 
     * @return the resolved concept reference
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    
    public ResolvedConceptReference buildResolvedConceptReference(CodeToReturn codeToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, Filter[] filters, boolean resolve)
            throws LBInvocationException;

    public ResolvedConceptReferenceList buildResolvedConceptReference(List<CodeToReturn> codesToReturn,
            LocalNameList restrictToProperties, PropertyType[] restrictToPropertyTypes, Filter[] filters, boolean resolve)
            throws LBInvocationException;
}