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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Class LazyLoadableCodeToReturn.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CommonIndexLazyLoadableCodeToReturn extends AbstractNonProxyLazyCodeToReturn {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2847101693632620073L;
    
    private List<AbsoluteCodingSchemeVersionReference> references;
 
    /**
     * Instantiates a new lazy loadable code to return.
     */
    public CommonIndexLazyLoadableCodeToReturn(){
        super();
    }
    
    public CommonIndexLazyLoadableCodeToReturn(
            List<AbsoluteCodingSchemeVersionReference> references,
            ScoreDoc doc){
        this(references, doc.score, doc.doc);
    }
 
    /**
     * Instantiates a new lazy loadable code to return.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * @param score the score
     * @param documentId the document id
     */
    public CommonIndexLazyLoadableCodeToReturn(
            List<AbsoluteCodingSchemeVersionReference> references,
            float score,
            int documentId){
        super(score, documentId);
        this.references = references;
    }
    
    protected Document buildDocument() throws Exception {
        return getEntityIndexService().getDocumentFromCommonIndexById(this.references, this.getDocumentId());
    }
}