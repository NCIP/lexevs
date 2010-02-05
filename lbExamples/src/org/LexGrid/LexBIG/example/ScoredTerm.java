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
package org.LexGrid.LexBIG.example;

import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.commonTypes.EntityDescription;

/**
 * Used to manage and sort search results based on a scoring algorithm.
 */
class ScoredTerm implements Comparable<ScoredTerm> {
    ResolvedConceptReference ref = null;
    float score = 0;

    /**
     * Construct a ScoredTerm based on the given concept reference and score.
     * 
     * @param ref
     * @param score
     */
    public ScoredTerm(ResolvedConceptReference ref, float score) {
        this.ref = ref;
        this.score = score;
    }

    /**
     * Compare this ScoredTerm to another. Comparison is by score, using
     * description text as tie-breaker ...
     */
    public int compareTo(ScoredTerm st) {
        float f = st.score - this.score;
        if (f != 0)
            return f > 0 ? 1 : 0;
        EntityDescription ed1 = ref.getEntityDescription();
        EntityDescription ed2 = st.ref.getEntityDescription();
        String term1 = ed1 != null ? ed1.getContent() : "";
        String term2 = ed2 != null ? ed2.getContent() : "";
        return term1.compareTo(term2);
    }
}