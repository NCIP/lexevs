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

import java.io.ObjectStreamException;

import org.apache.lucene.search.ScoreDoc;

/**
 * The Class AopLazyCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AopLazyCodeToReturn extends LazyLoadableCodeToReturn {
    
    /**
     * Instantiates a new aop lazy code to return.
     */
    public AopLazyCodeToReturn() {
        super();
    }

    /**
     * Instantiates a new aop lazy code to return.
     * 
     * @param scoreDoc the score doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     */
    public AopLazyCodeToReturn(ScoreDoc scoreDoc, String internalCodeSystemName, String internalVersionString) {
        super(scoreDoc, internalCodeSystemName, internalVersionString);
    }

    /**
     * Instantiates a new aop lazy code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public AopLazyCodeToReturn(String internalCodeSystemName, String internalVersionString, float score, int documentId) {
        super(internalCodeSystemName, internalVersionString, score, documentId);
    }

    /**
     * Write replace.
     * 
     * @return the object
     * 
     * @throws ObjectStreamException the object stream exception
     */
    public Object writeReplace() throws ObjectStreamException{
        return new LazyLoadableCodeToReturn(
                super.getInternalCodeSystemName(), 
                super.getInternalVersionString(), 
                this.getScore(), 
                super.getDocumentId());
    }
    
    /**
     * Read resolve.
     * 
     * @return the object
     * 
     * @throws ObjectStreamException the object stream exception
     */
    public Object readResolve() throws ObjectStreamException{
        return AopCodeHolderFactory.makeProxy(
                new LazyLoadableCodeToReturn(
                super.getInternalCodeSystemName(), 
                super.getInternalVersionString(), 
                this.getScore(),
                super.getDocumentId()));
    }


    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.CodeToReturn#equals(java.lang.Object)
     */
    public final boolean equals(Object obj){
       return super.equals(obj);
    }
}
