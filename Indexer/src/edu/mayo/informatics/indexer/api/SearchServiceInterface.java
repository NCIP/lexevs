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
package edu.mayo.informatics.indexer.api;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;

import edu.mayo.informatics.indexer.api.exceptions.IndexSearchException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;

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
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public void search(Query query, Filter filter, HitCollector hitCollector) throws InternalIndexerErrorException, IndexSearchException;

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
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public Document[] search(Query query, Filter filter, boolean skipLowScoring, int maxToReturn)
            throws InternalIndexerErrorException;

    /**
     * Gets the next search results.
     * 
     * @param howMany the how many
     * 
     * @return the next search results
     * 
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public Document[] getNextSearchResults(int howMany) throws InternalIndexerErrorException;

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
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public float[] getScores() throws InternalIndexerErrorException;

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
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public Explanation explain(Query query, int doc) throws InternalIndexerErrorException;

    /**
     * Sets the similarity.
     * 
     * @param similarity the new similarity
     * 
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public void setSimilarity(Similarity similarity) throws InternalIndexerErrorException;

    /**
     * Gets the similarity.
     * 
     * @return the similarity
     */
    public Similarity getSimilarity();

    /**
     * Close.
     * 
     * @throws InternalIndexerErrorException the internal indexer error exception
     */
    public void close() throws InternalIndexerErrorException;
}