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

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.annotations.LgProxyClass;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.lexevs.locator.LexEvsServiceLocator;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
@LgProxyClass
public class ScoreDocTransformer implements Serializable {

    private static final long serialVersionUID = 7176335324999288237L;
    
    
    public ResolvedConceptReference doTransform(Set<AbsoluteCodingSchemeVersionReference> codeSystemsToInclude, ProxyProtectedScoreDocWrapper item) {
        Document doc = 
            LexEvsServiceLocator.getInstance().
                getIndexServiceManager().
                getSearchIndexService().
                getById(codeSystemsToInclude,item.getScoreDoc().doc);
        
        String code = doc.get("entityCode");
        String namespace = doc.get("entityCodeNamespace");
        String[] types = doc.getValues("type");
        String description = doc.get("entityDescription");
        String codingSchemeUri = doc.get("codingSchemeUri");
        String codingSchemeName = doc.get("codingSchemeName");
        String codingSchemeVersion = doc.get("codingSchemeVersion");
        
        ResolvedConceptReference ref = new ResolvedConceptReference();
        ref.setCode(code);
        ref.setCodeNamespace(namespace);
        ref.setEntityType(types);
        ref.setCodingSchemeName(codingSchemeName);
        ref.setCodingSchemeURI(codingSchemeUri);
        ref.setCodingSchemeVersion(codingSchemeVersion);
        if(StringUtils.isNotBlank(description)){
            ref.setEntityDescription(Constructors.createEntityDescription(description));
        }
        
        return ref;
    }
}