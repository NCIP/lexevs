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
package org.LexGrid.LexBIG.Impl.pagedgraph.query;

import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.NameAndValue;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.apache.commons.collections.CollectionUtils;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery;
import org.lexevs.dao.database.service.codednodegraph.model.GraphQuery.QualifierNameValuePair;
import org.springframework.util.StringUtils;

/**
 * The Class DefaultGraphQueryBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultGraphQueryBuilder implements GraphQueryBuilder {
    
    /** The graph query. */
    private GraphQuery graphQuery = new GraphQuery();

    public DefaultGraphQueryBuilder(){
        super();
    }
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#getQuery()
     */
    @Override
    public GraphQuery getQuery() {
        return this.graphQuery;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToAssociations(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public void restrictToAssociations(NameAndValueList association, NameAndValueList associationQualifiers)
        throws LBInvocationException, LBParameterException {

        if(association != null) {
            for(NameAndValue nameAndValue : association.getNameAndValue()) {
                //TODO: resolve URIs
                if(StringUtils.hasText(nameAndValue.getContent())){
                    throw new UnsupportedOperationException();
                }
                graphQuery.getRestrictToAssociations().add(nameAndValue.getName());

            }
        }
        if(associationQualifiers != null) {
            for(NameAndValue nameAndValue : associationQualifiers.getNameAndValue()) {
                String qualName = nameAndValue.getName();
                String qualValue = nameAndValue.getContent();

                if(StringUtils.hasText(qualValue) && !StringUtils.hasText(qualName)) {
                    throw new LBParameterException("When applying a Qualifier Restriction onto an Association," +
                    " you must not specify a Qualifier VALUE without a Qualifier NAME");
                }

                graphQuery.getRestrictToAssociationsQualifiers().add(new QualifierNameValuePair(qualName, qualValue));

            }
        }
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToCodeSystem(java.lang.String)
     */
    @Override
    public void restrictToCodeSystem(String codingScheme) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public void restrictToCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToDirectionalNames(org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList, org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList)
     */
    @Override
    public void restrictToDirectionalNames(NameAndValueList directionalNames,
            NameAndValueList associationQualifiers) throws LBInvocationException, LBParameterException {
        
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToSourceCodeSystem(java.lang.String)
     */
    @Override
    public void restrictToSourceCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToSourceCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public void restrictToSourceCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToTargetCodeSystem(java.lang.String)
     */
    @Override
    public void restrictToTargetCodeSystem(String codingScheme) throws LBInvocationException,
            LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.pagedgraph.query.GraphQueryBuilder#restrictToTargetCodes(org.LexGrid.LexBIG.LexBIGService.CodedNodeSet)
     */
    @Override
    public void restrictToTargetCodes(CodedNodeSet codes) throws LBInvocationException, LBParameterException {
        // TODO Auto-generated method stub (IMPLEMENT!)
        throw new UnsupportedOperationException();
    }

}
