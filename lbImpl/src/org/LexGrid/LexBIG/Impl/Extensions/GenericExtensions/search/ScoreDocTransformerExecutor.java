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
 *      http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.search;

import java.io.Serializable;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.apache.lucene.search.ScoreDoc;


/**
 * @author @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class ScoreDocTransformerExecutor implements Serializable {
    

    private static final long serialVersionUID = 7939532917237511648L;

    public ResolvedConceptReferenceList transform(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude,
            ScoreDocTransformer transformer, Iterable<ScoreDoc> items) {
        ResolvedConceptReferenceList list = new ResolvedConceptReferenceList();
        for(ScoreDoc item : items){
            ProxyProtectedScoreDocWrapper wrapper = 
                    new ProxyProtectedScoreDocWrapper();
            wrapper.setScoreDoc(item);
            list.addResolvedConceptReference(transformer.doTransform(codeSystemsToInclude, wrapper));
        }
        
        return list;
    }

}
