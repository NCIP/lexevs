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
package edu.mayo.informatics.indexer.lucene;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Similarity;

import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.api.exceptions.IndexSearchException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;

/**
 * Used for searching 1 index.
 * 
 * This is the main class to be used for searching. It automatically searches
 * across multiple indexes, and automatically re-opens the indexes as necessary
 * if they have become out of date (due to another process adding documents to
 * the index)
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class LuceneIndexSearcher implements SearchServiceInterface {
    private final Logger logger = Logger.getLogger("Indexer.Index");

    private LuceneIndexReader index_;
    private IndexSearcher searcher_;
    private LuceneHits[] luceneHits_ = null;
    private Hits hits_ = null;
    private int readSoFar_ = 0;
    private int lastStartPoint_ = 0;

    public LuceneIndexSearcher(LuceneIndexReader index) {
        this.index_ = index;
        searcher_ = new IndexSearcher(index.getBaseIndexReader());
    }

    public void reloadSearcher() throws InternalIndexerErrorException {
        index_.reopen();
        searcher_ = new IndexSearcher(index_.getBaseIndexReader());
    }

//    public Document[] search(Query query, Filter filter, boolean skipLowScoringHits, int maxToReturn)
//            throws InternalIndexerErrorException {
//        int max = maxToReturn;
//        if (maxToReturn <= 0) {
//            max = Integer.MAX_VALUE;
//        }
//        if (skipLowScoringHits) {
//            return searchSkipLowScoreing(query, filter, max);
//        } else {
//            return searchSkipNone(query, filter, max);
//        }
//    }
    
    public void search(Query query, Filter filter, HitCollector hitCollector) throws InternalIndexerErrorException, IndexSearchException{
        try {
            searcher_.search(query, filter, hitCollector);
        } catch (BooleanQuery.TooManyClauses e) {
            throw new IndexSearchException("The specified query is too general and too many results have been returned -- please narrow your query.");
        } catch (IOException e) {
            throw new InternalIndexerErrorException(e.getMessage());
        }   
    }

    private Document[] searchSkipLowScoreing(Query query, Filter filter, int maxToReturn)
            throws InternalIndexerErrorException {
        this.luceneHits_ = null;
        this.hits_ = null;

        if (!index_.upToDate()) {
            reloadSearcher();
        }

        try {
            hits_ = searcher_.search(query, filter);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error searching the index " + e);
        }

        int stop = hits_.length() >= maxToReturn ? maxToReturn : hits_.length();
        Document[] temp = new Document[stop];
        readSoFar_ = stop;
        lastStartPoint_ = 0;
        try {
            for (int i = 0; i < stop; i++) {
                temp[i] = hits_.doc(i);
            }
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error collecting the results to return " + e);
        }

        return temp;

    }

//    private Document[] searchSkipNone(Query query, Filter filter, int maxToReturn) throws InternalIndexerErrorException {
//        this.luceneHits_ = null;
//        this.hits_ = null;
//
//        if (!index_.upToDate()) {
//            reloadSearcher();
//        }
//
//        final ArrayList tempHits = new ArrayList(index_.maxDoc() / 4);
//        readSoFar_ = 0;
//
//        try {
//            searcher_.search(query, filter, new HitCollector() {
//                public void collect(int doc, float score) {
//                    tempHits.add(new LuceneHits(doc, score));
//                }
//            });
//        } catch (IOException e) {
//            logger.error(e);
//            throw new InternalIndexerErrorException("There was an error searching the index " + e);
//        }
//
//        luceneHits_ = (LuceneHits[]) tempHits.toArray(new LuceneHits[tempHits.size()]);
//
//        Arrays.sort(luceneHits_, new HitComparator());
//
//        // normalize the scores
//        float scoreNorm = 1.0f;
//        if (luceneHits_.length > 0 && luceneHits_[0].score_ > 1.0f)
//            scoreNorm = 1.0f / luceneHits_[0].score_;
//
//        for (int i = 0; i < luceneHits_.length; i++)
//            luceneHits_[i].score_ = luceneHits_[i].score_ * scoreNorm;
//
//        // Collect the first set of results
//        int stop = luceneHits_.length >= maxToReturn ? maxToReturn : luceneHits_.length;
//        Document[] temp = new Document[stop];
//        readSoFar_ = stop;
//        lastStartPoint_ = 0;
//        try {
//            for (int i = 0; i < stop; i++) {
//                temp[i] = searcher_.doc(luceneHits_[i].doc_);
//            }
//        } catch (IOException e) {
//            logger.error(e);
//            throw new InternalIndexerErrorException("There was an error collecting the results to return " + e);
//        }
//
//        return temp;
//    }

    public Document[] getNextSearchResults(int howMany) throws InternalIndexerErrorException {
        int hitLength = 0;
        if (luceneHits_ != null) {
            hitLength = luceneHits_.length;
        } else {
            hitLength = hits_.length();
        }

        int stop = hitLength >= readSoFar_ + howMany ? readSoFar_ + howMany : hitLength;
        Document[] temp = new Document[stop - readSoFar_];
        lastStartPoint_ = readSoFar_;

        try {
            int j = 0;
            if (luceneHits_ != null) {
                for (int i = readSoFar_; i < stop; i++) {
                    temp[j++] = searcher_.doc(luceneHits_[i].doc_);
                }
            } else {
                for (int i = readSoFar_; i < stop; i++) {
                    temp[j++] = hits_.doc(i);
                }
            }

        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error collecting the results to return " + e);
        }

        readSoFar_ += stop - readSoFar_;
        return temp;
    }

    public boolean hasMoreHits() {
        if (this.luceneHits_ != null) {
            return readSoFar_ < luceneHits_.length;
        } else {
            return readSoFar_ < this.hits_.length();
        }
    }

    /**
     * Returned the scores for that last retrieved set of results.
     * 
     * @return An array of scores that match the hits.
     * @throws InternalIndexerErrorException
     */
    public float[] getScores() throws InternalIndexerErrorException {
        float[] temp = new float[readSoFar_ - lastStartPoint_];
        try {
            int j = 0;
            if (luceneHits_ != null) {
                for (int i = lastStartPoint_; i < readSoFar_; i++) {
                    temp[j++] = luceneHits_[i].score_;
                }
            } else {
                for (int i = lastStartPoint_; i < readSoFar_; i++) {
                    temp[j++] = hits_.score(i);
                }
            }

        } catch (Exception e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error collecting the results to return " + e);
        }
        return temp;
    }

    public int getHitTotal() {
        if (this.luceneHits_ != null) {
            return luceneHits_.length;
        } else {
            return hits_.length();
        }
    }

    public String[] searchableFields() {
        return (String[]) index_.searchableFields().toArray(new String[index_.searchableFields().size()]);
    }

    public Explanation explain(Query query, int doc) throws InternalIndexerErrorException {
        try {
            if (!index_.upToDate()) {
                reloadSearcher();
            }

            return searcher_.explain(query, doc);
        } catch (InternalIndexerErrorException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalIndexerErrorException("There was a problem generating the explanation" + e);
        }
    }

    public void setSimilarity(Similarity similarity) throws InternalIndexerErrorException {
        try {
            searcher_.setSimilarity(similarity);
        } catch (Exception e) {
            throw new InternalIndexerErrorException("There was a problem setting the similarity" + e);
        }
    }

    public Similarity getSimilarity() {
        return searcher_.getSimilarity();
    }

    public void close() throws InternalIndexerErrorException {
        try {
            searcher_.close();
        } catch (IOException e) {
            throw new InternalIndexerErrorException("There was a problem closing the searcher" + e);
        }
    }
}