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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import org.apache.lucene.search.ScoreDoc;

/**
 * The Class NonProxyLazyCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NonProxyLazyCodeToReturn extends LazyLoadableCodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8339301513416366127L;

    /**
     * Instantiates a new non proxy lazy code to return.
     * 
     * @param scoreDoc the score doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     */
    public NonProxyLazyCodeToReturn(ScoreDoc scoreDoc, String internalCodeSystemName, String internalVersionString) {
        super(scoreDoc, internalCodeSystemName, internalVersionString);
    }

    /**
     * Instantiates a new non proxy lazy code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public NonProxyLazyCodeToReturn(String internalCodeSystemName, String internalVersionString, float score,
            int documentId) {
        super(internalCodeSystemName, internalVersionString, score, documentId);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getCode()
     */
    @Override
    public String getCode() {
        hydrateIfNeeded();
        return super.getCode();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getEntityDescription()
     */
    @Override
    public String getEntityDescription() {
        hydrateIfNeeded();
        return super.getEntityDescription();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getEntityTypes()
     */
    @Override
    public String[] getEntityTypes() {
        hydrateIfNeeded();
        return super.getEntityTypes();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getNamespace()
     */
    @Override
    public String getNamespace() {
        hydrateIfNeeded();
        return super.getNamespace();
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#getUri()
     */
    @Override
    public String getUri() {
        hydrateIfNeeded();
        return super.getUri();
    }

    /**
     * Hydrate if needed.
     */
    protected void hydrateIfNeeded(){
        if(!super.isHydrated()){
            try {
                super.hydrate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
