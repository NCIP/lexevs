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
package org.lexevs.dao.indexer.lucene.hitcollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.ScoreDoc;

/**
 * The Class HitCollectorMerger.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HitCollectorMerger {

   private List<BestScoreOfEntityHitCollector> hitCollectors = new ArrayList<BestScoreOfEntityHitCollector>();
   
   private List<Map<Integer,ScoreDoc>> docMaps = new ArrayList<Map<Integer,ScoreDoc>>();

   private DocIdSetIterator boundryDocIterator;
   
   private boolean atEnd = false;
   
   private int maxDocs;
   
   public HitCollectorMerger(DocIdSetIterator boundryDocIterator, int maxDocs){
       this.boundryDocIterator = boundryDocIterator;
       this.maxDocs = maxDocs;
   }
   
   public List<ScoreDoc> getMergedScoreDocs(){ 
       sortHitCollectors();
       buildMaps();
    
       try {
           List<ScoreDoc> returnDocs = new ArrayList<ScoreDoc>();
           Iterator<ScoreDoc> scoreDocsItr = hitCollectors.get(0).getResult().iterator();
           
           int start = 0;
           int end = 0;

           while(scoreDocsItr.hasNext()){
               int firstMatchingDoc = scoreDocsItr.next().doc;
               while(boundryDocIterator.docID() < firstMatchingDoc){
                   start = boundryDocIterator.docID();
                   atEnd = boundryDocIterator.nextDoc() == DocIdSetIterator.NO_MORE_DOCS;    
               }
               
               if(atEnd){
                   end = maxDocs;
               } else {
                   end = boundryDocIterator.docID();
               }
               
               ScoreDoc combinedDoc = getCombinedScoreDoc(start, end);
               if(combinedDoc != null){
                   returnDocs.add(combinedDoc);
               }
           }  
   
           return returnDocs;
       } catch (IOException e) {
           throw new RuntimeException(e);
       }    
   }
   
 
   
   private void sortHitCollectors(){
       Collections.sort(hitCollectors, new Comparator<BestScoreOfEntityHitCollector>(){

           public int compare(BestScoreOfEntityHitCollector o1, BestScoreOfEntityHitCollector o2) {
               return o1.getResult().size() - o2.getResult().size();
           }
       } );
   }

   private ScoreDoc getCombinedScoreDoc(int start, int end) {
       List<ScoreDoc> scoreDocs = new ArrayList<ScoreDoc>();

       for(Map<Integer,ScoreDoc> map : docMaps){
           ScoreDoc doc = getHitCollectorContainDocInRange(map, start, end);
           if(doc == null){
               return null;
           } else {
               scoreDocs.add(doc);
           }
       }
       float totalScore = 0;
       for(ScoreDoc doc : scoreDocs){
           totalScore += doc.score;
       }
       return new ScoreDoc(scoreDocs.get(0).doc, totalScore);
   }
   
   private ScoreDoc getHitCollectorContainDocInRange(Map<Integer,ScoreDoc> map, int startOfGroup, int endOfGroup) {
       if(endOfGroup == -1){
           endOfGroup = getLastIndexOfMatch(map) + 1;
           if(endOfGroup == -1){
               return null;
           }
       }
       for(int i=startOfGroup;i<endOfGroup;i++){
           ScoreDoc doc = map.get(i);
           if(doc != null){
               return doc;
           }
       }  
       return null;
   }
   
   protected void buildMaps(){
       for(BestScoreOfEntityHitCollector hitCollector : hitCollectors){
           docMaps.add(
                   scoreDocListToMap(hitCollector.getResult()));
       }
   }
   
   protected Map<Integer,ScoreDoc> scoreDocListToMap(List<ScoreDoc> scoreDocs){
       Map<Integer,ScoreDoc> map = new HashMap<Integer,ScoreDoc>();
       for(ScoreDoc doc : scoreDocs){
          map.put(doc.doc, doc);
       }
       return map;
   }
   
   public int getLastIndexOfMatch(Map<Integer,ScoreDoc> map){
       int max = -1;
       
       Set<Integer> keySet = map.keySet();
       for(int i : keySet){
           if(i > max){
               max = i;
           }
       }
       return max;
   }

   public List<BestScoreOfEntityHitCollector> getHitCollectors() {
       return hitCollectors;
   }

   public void setHitCollectors(List<BestScoreOfEntityHitCollector> hitCollectors) {
       this.hitCollectors = hitCollectors;
   }
   
   public void addHitCollector(BestScoreOfEntityHitCollector hitCollector) {
       this.hitCollectors.add(hitCollector);
   }
}