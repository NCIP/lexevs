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
package org.LexGrid.LexBIG.Impl.codednodeset;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.LexGrid.LexBIG.Utility.Constructors;

public class LuceneOnlyToNodeListCodedNodeSet extends SingleLuceneIndexCodedNodeSet {
    
    private static final long serialVersionUID = -5959522938971242708L;
    
    public LuceneOnlyToNodeListCodedNodeSet() {
        super();
    }
 
    public LuceneOnlyToNodeListCodedNodeSet(String uri, String version, ConceptReferenceList codeList) throws LBInvocationException, LBParameterException, LBResourceUnavailableException {
        super(uri, Constructors.createCodingSchemeVersionOrTagFromVersion(version), null, null);
        
        if(codeList == null || codeList.getConceptReferenceCount() == 0) {
            codeList = Constructors.createConceptReferenceList("NO-MATCH-CODE", "NO-MATCH-NAMESPACE", "NO-MATCH-CODINGSCHEME");
        }
        this.restrictToCodes(codeList);
        
        AdditiveCodeHolder holder = new DefaultCodeHolder();
        for(ConceptReference ref : codeList.getConceptReference()) {
            holder.add(new CodeToReturn(uri, version, ref));
        }
        
        this.setToNodeListCodes(holder);
    }
}