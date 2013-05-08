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
package org.LexGrid.LexBIG.Impl.helpers;

import java.io.IOException;
import java.util.WeakHashMap;

import org.LexGrid.annotations.LgClientSideSafe;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

/**
 * Class to search a lucene index, but the result is a bit set that indicates
 * yes or no as to which documents satisfy the query. Results are cached, so
 * that searches after the first on the same index using this filter are much
 * faster.
 * 
 * It also keeps track of the Score of each item in the bitset.
 * 
 * This is code is heavily borrowed from the QueryFilter class in Lucene.
 * 
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class ScoredQueryFilter extends Filter {
    private static final long serialVersionUID = -104041362110167918L;
    private Query query;
    private transient WeakHashMap<IndexReader, ScoredBitSet> cache = null;

    /**
     * Constructs a filter which only matches documents matching
     * <code>query</code>.
     */
    public ScoredQueryFilter(Query query) {
        this.query = query;
    }

    @Override
    public ScoredBitSet bits(IndexReader reader) throws IOException {

        if (cache == null) {
            cache = new WeakHashMap<IndexReader, ScoredBitSet>();
        }

        synchronized (cache) { // check cache
            ScoredBitSet cached = (ScoredBitSet) cache.get(reader);
            if (cached != null) {
                return cached;
            }
        }

        final ScoredBitSet bits = new ScoredBitSet(reader.maxDoc());

        new IndexSearcher(reader).search(query, new HitCollector() {
            @Override
            public final void collect(int doc, float score) {
                bits.set(doc, score); // set bit for hit
            }
        });

        synchronized (cache) { // update cache
            cache.put(reader, bits);
        }

        return bits;
    }

    @Override
    @LgClientSideSafe
    public String toString() {
        return "QueryFilter(" + query + ")";
    }

    @Override
    @LgClientSideSafe
    public boolean equals(Object o) {
        if (!(o instanceof ScoredQueryFilter))
            return false;
        return this.query.equals(((ScoredQueryFilter) o).query);
    }

    @Override
    @LgClientSideSafe
    public int hashCode() {
        return query.hashCode() ^ 0x923F64B9;
    }
}