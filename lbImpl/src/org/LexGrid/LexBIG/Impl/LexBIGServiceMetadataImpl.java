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
package org.LexGrid.LexBIG.Impl;

import java.util.ArrayList;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.dataAccess.MetaDataQuery;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.lucene.index.Term;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.logging.LoggerFactory;

/**
 * Lucene implementation of the LexBIGServiceMetadata interface.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LexBIGServiceMetadataImpl implements LexBIGServiceMetadata {
    private static final long serialVersionUID = 3382129429728528566L;
    protected ArrayList<Query> queryClauses = new ArrayList<Query>();
    protected ArrayList<Term> termClauses = new ArrayList<Term>();

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#restrictToCodingScheme
     * (org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference)
     */
    public LexBIGServiceMetadata restrictToCodingScheme(AbsoluteCodingSchemeVersionReference acsvr)
            throws LBParameterException {
        getLogger().logMethod(new Object[] { acsvr });
        queryClauses.add(MetaDataQuery.makeCodingSchemeRestriction(acsvr));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#restrictToProperties
     * (java.lang.String[])
     */
    public LexBIGServiceMetadata restrictToProperties(String[] properties) throws LBParameterException {
        getLogger().logMethod(new Object[] { properties });
        queryClauses.add(MetaDataQuery.makePropertyRestriction(properties));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @seeorg.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#
     * restrictToPropertyParents(java.lang.String[])
     */
    public LexBIGServiceMetadata restrictToPropertyParents(String[] propertyParents) throws LBParameterException {
        getLogger().logMethod(new Object[] { propertyParents });
        queryClauses.add(MetaDataQuery.makePropertyParentRestriction(propertyParents));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#restrictToValue
     * (java.lang.String, java.lang.String)
     */
    public LexBIGServiceMetadata restrictToValue(String matchText, String matchAlgorithm) throws LBParameterException {
        getLogger().logMethod(new Object[] { matchText, matchAlgorithm });
        queryClauses.add(MetaDataQuery.makeValueRestriction(matchText, matchAlgorithm));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.LexGrid.LexBIG.LexBIGService.LexBIGServiceMetadata#resolve()
     */
    public MetadataPropertyList resolve() throws LBParameterException, LBInvocationException {
        getLogger().logMethod(new Object[] {});
        try {
            if (queryClauses.size() + termClauses.size() < 1) {
                throw new LBParameterException("At leat one restriction must be applied before resolving");
            }

            BooleanQuery masterQuery = new BooleanQuery();
            for (int i = 0; i < queryClauses.size(); i++) {
                masterQuery.add(queryClauses.get(i), Occur.MUST);
            }
            for (int i = 0; i < termClauses.size(); i++) {
                masterQuery.add(new RegexQuery(termClauses.get(i)), Occur.MUST);
            }

            return LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getMetadataIndexService().search(masterQuery);
        } catch (LBParameterException e) {
            throw e;
        } catch (Exception e) {
            String id = getLogger().error("An unexpected error occurred resolving the MetaData search.", e);
            throw new LBInvocationException(
                    "An unexpected error occurred resolving the metadata search.  See the log for more details", id);
        }
    }

    public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes() throws LBInvocationException {
        getLogger().logMethod(new Object[] {});
        try {
            return LexEvsServiceLocator.getInstance().
            getIndexServiceManager().
            getMetadataIndexService().listCodingSchemes();
        } catch (Exception e) {
            String id = getLogger().error("An unexpected error occurred while listing metadata coding schemes.", e);
            throw new LBInvocationException(
                    "An unexpected error occurred while listing metadata coding schemes.  See the log for more details",
                    id);
        }
    }
}