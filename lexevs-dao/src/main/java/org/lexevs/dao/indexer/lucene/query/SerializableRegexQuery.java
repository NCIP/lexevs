
package org.lexevs.dao.indexer.lucene.query;

import java.io.Serializable;

import org.apache.lucene.index.Term;
import org.apache.lucene.sandbox.queries.regex.RegexQuery;;  //org.apache.lucene.search.RegexpQuery; replace with this one?

/**
 * The Class SerializableRegexQuery.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SerializableRegexQuery extends RegexQuery implements Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -2239755266727442903L;

    /**
     * Instantiates a new serializable regex query.
     * 
     * @param term the term
     */
    public SerializableRegexQuery(Term term) {
        super(term);
        this.setRegexImplementation(new SerializableRegexCapabilities());
      }
}