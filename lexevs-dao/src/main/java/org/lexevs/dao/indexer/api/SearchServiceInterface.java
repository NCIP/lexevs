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
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;

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
     * @param Collector the collector
     * 
     * @throws RuntimeException
     */
    public void search(Query query, Filter filter, Collector collector) throws RuntimeException;


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
     * Close.
     * 
     * @throws RuntimeException
     */
    public void close() throws RuntimeException;
}