
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