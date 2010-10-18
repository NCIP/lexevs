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
package edu.mayo.informatics.indexer.lucene.hitcollector;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Class BestScoreOfEntityHitCollector.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BestScoreOfEntityHitCollector extends AbstractBestScoreOfEntityHitCollector<List<ScoreDoc>> {

    public BestScoreOfEntityHitCollector(
            DocIdSetIterator boundryDocIterator,
            int maxDoc) {
        super(boundryDocIterator, maxDoc);
    }

    @Override
    protected void addToReturnValue(List<ScoreDoc> results, int docId, float score) {
       results.add(new ScoreDoc(docId, score));
    }

    @Override
    protected List<ScoreDoc> initializeResults() {
        return new ArrayList<ScoreDoc>();
    }   
}