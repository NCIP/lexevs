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
package org.lexevs.dao.indexer.lucene;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Collector;
//import org.apache.lucene.search.Hits;
//import org.apache.lucene.search.IndexReader;
//import org.apache.lucene.search.ParallelMultiReader;
import org.apache.lucene.search.Query;
//import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.join.ToParentBlockJoinIndexSearcher;
import org.lexevs.dao.indexer.api.SearchServiceInterface;

/**
 * Used for searching 1 or more indexes.
 * 
 * This is the main class to be used for searching. It automatically searches
 * across multiple indexes, and automatically re-opens the indexes as necessary
 * if they have become out of date (due to another process adding documents to
 * the index)
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class LuceneMultiIndexSearcher implements SearchServiceInterface {
    private final Logger logger = Logger.getLogger("Indexer.Index");

    private LuceneIndexReader[] indexes_;
    private MultiReader multiReader;
    private  ToParentBlockJoinIndexSearcher multiSearcher;
    private IndexReader[] searchers;
    private LuceneHits[] luceneHits = null;
    private TopDocs hits = null;
    private int readSoFar = 0;
    private int lastStartPoint = 0;

//    public LuceneMultiIndexSearcher(LuceneIndexReader[] indexes, boolean parallel) throws RuntimeException {
//        this.indexes_ = indexes;
//        searchers = new IndexReader[indexes.length];
//
//        for (int i = 0; i < indexes.length; i++) {
//            searchers[i] = indexes[i].getBaseIndexReader();
//        }
//
//        try {
//                multiReader = new MultiReader(searchers);
//        } catch (IOException e) {
//            logger.error(e);
//            throw new RuntimeException("There was an error opening the multi-index searcher " + e);
//        }
//
//    }

//    public void reloadSearchers() throws RuntimeException {
//        searchers = new IndexReader[indexes_.length];
//        try {
//            for (int i = 0; i < indexes_.length; i++) {
//                indexes_[i].reopen();
//                searchers[i] = indexes_[i].getBaseIndexReader();
//            }
//
//            multiSearcher = new MultiReader(searchers);
//        } catch (IOException e) {
//            logger.error(e);
//            throw new RuntimeException("There was an error opening the multi-index searcher " + e);
//        }
//    }


//    public boolean hasMoreHits() {
//        if (this.luceneHits != null) {
//            return readSoFar < luceneHits.length;
//        } else {
//            return readSoFar < this.hits.length();
//        }
//    return false;
//    }

//    public int getHitTotal() {
//        if (this.luceneHits != null) {
//            return luceneHits.length;
//        } else {
//            return hits.length();
//        }
//    	return -1;
//
//    }

//    public String[] searchableFields() {
//        HashSet temp = new HashSet();
//
//        for (int i = 0; i < indexes_.length; i++) {
//            temp.addAll(indexes_[i].searchableFields());
//        }
//
//        return (String[]) temp.toArray(new String[temp.size()]);
//    }

//    /**
//     * Returned the scores for that last retrieved set of results.
//     * 
//     * @return An array of scores that match the hits.
//     * @throws RuntimeException
//     */
//    public float[] getScores() throws RuntimeException {
//        float[] temp = new float[readSoFar - lastStartPoint];
//        try {
//            int j = 0;
//            if (luceneHits != null) {
//                for (int i = lastStartPoint; i < readSoFar; i++) {
//                    temp[j++] = luceneHits[i].score_;
//                }
//            } else {
//                for (int i = lastStartPoint; i < readSoFar; i++) {
//                    temp[j++] = hits.scoreDocs[i].score;
//                }
//            }
//
//        } catch (Exception e) {
//            logger.error(e);
//            throw new RuntimeException("There was an error collecting the results to return " + e);
//        }
//        return temp;
//    }



    public void close() throws RuntimeException {
//        try {
//            multiSearcher.close();
//        } catch (IOException e) {
//            throw new RuntimeException("There was a problem closing the searcher" + e);
//        }
    }
}