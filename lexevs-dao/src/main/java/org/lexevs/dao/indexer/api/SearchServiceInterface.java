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
package org.lexevs.dao.indexer.api;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;

/**
 * This is the interface that you use to search an index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SearchServiceInterface {
    
    /**
     * Searchable fields.
     * 
     * @return the string[]
     */
    public String[] searchableFields();
    
    /**
     * Search.
     * 
     * @param query the query
     * @param filter the filter
     * @param hitCollector the hit collector
     * 
     * @throws RuntimeException
     */
    public void search(Query query, Filter filter, HitCollector hitCollector) throws RuntimeException;

    /**
     * Search.
     * 
     * @param query the query
     * @param filter the filter
     * @param skipLowScoring the skip low scoring
     * @param maxToReturn the max to return
     * 
     * @return the document[]
     * 
     * @throws RuntimeException
     */
//    public Document[] search(Query query, Filter filter, boolean skipLowScoring, int maxToReturn)
//            throws RuntimeException;

    /**
     * Gets the next search results.
     * 
     * @param howMany the how many
     * 
     * @return the next search results
     * 
     * @throws RuntimeException
     */
    public Document[] getNextSearchResults(int howMany) throws RuntimeException;

    /**
     * Checks for more hits.
     * 
     * @return true, if successful
     */
    public boolean hasMoreHits();

    /**
     * Gets the scores.
     * 
     * @return the scores
     * 
     * @throws RuntimeException
     */
    public float[] getScores() throws RuntimeException;

    /**
     * Gets the hit total.
     * 
     * @return the hit total
     */
    public int getHitTotal();

    /**
     * Explain.
     * 
     * @param query the query
     * @param doc the doc
     * 
     * @return the explanation
     * 
     * @throws RuntimeException
     */
    public Explanation explain(Query query, int doc) throws RuntimeException;

    /**
     * Sets the similarity.
     * 
     * @param similarity the new similarity
     * 
     * @throws RuntimeException
     */
    public void setSimilarity(Similarity similarity) throws RuntimeException;

    /**
     * Gets the similarity.
     * 
     * @return the similarity
     */
    public Similarity getSimilarity();

    /**
     * Close.
     * 
     * @throws RuntimeException
     */
    public void close() throws RuntimeException;
}