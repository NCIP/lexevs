
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