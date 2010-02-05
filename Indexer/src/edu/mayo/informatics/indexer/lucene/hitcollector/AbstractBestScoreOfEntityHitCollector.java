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
package edu.mayo.informatics.indexer.lucene.hitcollector;

import java.io.IOException;
import java.util.Comparator;

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.HitCollector;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Class BestScoreOfEntityHitCollector.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBestScoreOfEntityHitCollector<T> extends HitCollector {
    
    /** The boundry doc iterator. */
    private DocIdSetIterator boundryDocIterator;
    
    /** The highest score of group score. */
    private float highestScoreOfGroupScore = -1;
    
    /** The highest score of group doc id. */
    private int highestScoreOfGroupDocId = -1;
    
    /** The finalized. */
    private boolean finalized = false;
    
    /** The initialized. */
    private boolean initialized = false;
    
    /** The score docs. */
    //private List<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>();
    
    private T results;
    
    private int startOfBoundary = -1;
    
    private boolean atEndOfDocs = false;
    
    private int maxDoc;
    
    /**
     * Instantiates a new best score of entity hit collector.
     * 
     * @param boundryDocIterator the boundry doc iterator
     */
    public AbstractBestScoreOfEntityHitCollector(
            DocIdSetIterator boundryDocIterator,
            int maxDoc){
        this.boundryDocIterator = boundryDocIterator;
        this.results = initializeResults();
        this.maxDoc = maxDoc;
    }
    
    protected abstract T initializeResults();
    
    /**
     * Sets the next boundary doc position.
     * 
     * @param currentHitDoc the new next boundary doc position
     */
    private void setNextBoundaryDocPosition(int currentHitDoc){
        try {
            if(boundryDocIterator.doc() == 0){
                boundryDocIterator.next();
            }
            while(boundryDocIterator.doc() <= currentHitDoc){
                startOfBoundary = boundryDocIterator.doc();
                if(!boundryDocIterator.next()){
                    atEndOfDocs = true;
                    break;
                }
            }
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
    }
    
    /**
     * In group.
     * 
     * @param docId the doc id
     * 
     * @return true, if successful
     */
    private boolean inGroup(int docId){
        return  docId < boundryDocIterator.doc() || atEndOfDocs;
    }
  
    /* (non-Javadoc)
     * @see org.apache.lucene.search.HitCollector#collect(int, float)
     */
    @Override
    public void collect(int docId, float score) {  
        if(!initialized){
            setNextBoundaryDocPosition(docId);
            initialized = true;
        }
        
        if(inGroup(docId)){
            if(score > highestScoreOfGroupScore){
                highestScoreOfGroupDocId = docId;
                highestScoreOfGroupScore = score;     
            }
        } else {
            addToReturnValue(results, highestScoreOfGroupDocId, highestScoreOfGroupScore);
            highestScoreOfGroupDocId = docId;
            highestScoreOfGroupScore = score; 
            setNextBoundaryDocPosition(docId);         
        } 
    }   
    
    /**
     * Gets the score docs.
     * 
     * @return the score docs
     */
    public T getResult(){  
        postProcess();
        return results;
    }
    
    protected int getStartOfBoundary(){
        return this.startOfBoundary;
    }
    
    protected int getEndOfBoundary(){
        if(this.atEndOfDocs){
            return this.maxDoc + 1;
        } else {
            return this.boundryDocIterator.doc();
        }
    }
    
    protected int getMaxDoc(){
        return this.maxDoc;
    }
    
    /**
     * Post process.
     */
    protected void postProcess(){
        if(!finalized && highestScoreOfGroupDocId > -1){
            addToReturnValue(results, highestScoreOfGroupDocId, highestScoreOfGroupScore);
            finalized = true;
        }   
    }
    
    protected abstract void addToReturnValue(T results, int docId, float score);
    
    
    /**
     * The Class ScoreComparator.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    protected static class ScoreComparator implements Comparator<ScoreDoc>{
        
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare (ScoreDoc i, ScoreDoc j) {
            if (i.score > j.score) return -1;
            if (i.score < j.score) return 1;
            return 0;
        }
    };
}
