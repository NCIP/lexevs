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

import java.util.BitSet;
import java.util.List;

import junit.framework.TestCase;

import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.OpenBitSet;

import edu.mayo.informatics.indexer.lucene.hitcollector.AbstractBestScoreOfEntityHitCollector.ScoreComparator;

/**
 * The Class BestScoreOfEntityHitCollectorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BitSetBestScoreOfEntityHitCollectorTest extends TestCase {

    public void testNoHits() throws Exception {
        
        OpenBitSet idSet = new OpenBitSet();
        
        //Set the bits for boundary docs
        idSet.set(0);
        idSet.set(2);
        idSet.set(4);
        
        BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 4);
        
        BitSet bitSet = hitCollector.getResult();
        
       assertTrue(bitSet.isEmpty());
    }
    

    public void testHitAllNoMultiples() throws Exception {
        
        OpenBitSet idSet = new OpenBitSet();
        
        //Set the bits for boundary docs
        idSet.set(0);
        idSet.set(2);
        idSet.set(4);
        
        BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 4);
        //simulate hits on all non-boundry docs    
        hitCollector.collect(1, 1f);
        hitCollector.collect(3, 1f);
  
        BitSet bitSet = hitCollector.getResult();
        
        assertTrue("BitSet: " + bitSet, checkBitSet(bitSet, 1, 3));
    }
 
    public void testHitAllMultiplesSameScore(){
        
        OpenBitSet idSet = new OpenBitSet();
        
        //Set the bits for boundary docs (1=boundary doc, 0=not)
        idSet.set(0);
        idSet.set(3);
        idSet.set(6);
        
        BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(),6); 
        //simulate hits on all non-boundry docs    
        hitCollector.collect(1, 1f);
        hitCollector.collect(2, 1f);
        hitCollector.collect(4, 1f);
        hitCollector.collect(5, 1f);
        
        BitSet bitSet = hitCollector.getResult();
        
        assertTrue(checkBitSet(bitSet, 1,2,4,5));
    }
    
    public void testHitAllMultiplesDifferentScore(){
        
        OpenBitSet idSet = new OpenBitSet();
        
        //Set the bits for boundary docs (1=boundary doc, 0=not)
        idSet.set(0);
        idSet.set(3);
        idSet.set(6);
            
        BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 6); 
         
        //simulate hits on all non-boundry docs    
        hitCollector.collect(1, 2f);
        hitCollector.collect(2, 1f);
        hitCollector.collect(4, 1f);
        hitCollector.collect(5, 2f);
        
        BitSet bitSet = hitCollector.getResult();
        
        assertTrue(checkBitSet(bitSet, 1,2,4,5));
    }
    
   public void testMultipleBoundaryDocs(){
        
        OpenBitSet idSet = new OpenBitSet();
        
        //Set the bits for boundary docs (1=boundary doc, 0=not)
        idSet.set(0);
        idSet.set(1);
        idSet.set(4);
        idSet.set(5);
        idSet.set(8);
        idSet.set(10);
        idSet.set(11);
        idSet.set(12);
        idSet.set(13);
        idSet.set(14);
        idSet.set(17);
            
        BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 18); 
      
        //simulate hits on all non-boundry docs    
        hitCollector.collect(2, 2f);
        hitCollector.collect(3, 1f);
        hitCollector.collect(6, 1f);
        hitCollector.collect(7, 2f);
        hitCollector.collect(9, 2f);
        hitCollector.collect(15, 2f);
        hitCollector.collect(18, 2f);
        
        BitSet bitSet = hitCollector.getResult();
                
        assertTrue("BitSet : " + bitSet, checkBitSet(bitSet, 2,3,6,7,9,15,16,18));
   }

   public void testNoBookendBoundaryDocs(){
       
       OpenBitSet idSet = new OpenBitSet();
       
       //Set the bits for boundary docs 
       idSet.set(1);
       idSet.set(4);
       idSet.set(5);
       idSet.set(8);
           
       BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(),9);
       
       //simulate hits on all non-boundry docs    
       hitCollector.collect(0, 2f);
       hitCollector.collect(9, 2f);
       
       BitSet bitSet = hitCollector.getResult();
       
       assertTrue(
    		   "BitSet: " + bitSet, checkBitSet(bitSet,0,9));
   }
   
 public void testNoBookendBoundaryDocsTwoMatches(){
       
       OpenBitSet idSet = new OpenBitSet();
       
       //Set the bits for boundary docs 
       idSet.set(3);
       idSet.set(4);
       idSet.set(5);
       idSet.set(8);
           
       BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(),11);
       
       //simulate hits on all non-boundry docs    
       hitCollector.collect(0, 2f);
       hitCollector.collect(1, 2f);
       hitCollector.collect(2, 2f);
       hitCollector.collect(9, 2f);
       hitCollector.collect(10, 2f);
       hitCollector.collect(11, 2f);
       
       BitSet bitSet = hitCollector.getResult();
       
       assertTrue(
    		   "BitSet: " + bitSet, checkBitSet(bitSet,0,1,2,9,10,11));
   }
   
  
   public void testDifferentBoundaryDocsSizes(){
       
       OpenBitSet idSet = new OpenBitSet();
       
       //Set the bits for boundary docs (1=boundary doc, 0=not)
   
       idSet.set(0);
       idSet.set(1);
       idSet.set(2);
       idSet.set(5);  
       idSet.set(8);
       idSet.set(9);
       idSet.set(10);
           
       BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 10);
       
       //simulate hits on all non-boundry docs    
       hitCollector.collect(3, 2f);
       hitCollector.collect(4, 1f);
       hitCollector.collect(6, 1f);
       hitCollector.collect(7, 2f);
       
       BitSet bitSet = hitCollector.getResult();
      
       assertTrue(checkBitSet(bitSet, 3,4,6,7));
   }
   
   public void testMultipleDifferentScores(){
       
       OpenBitSet idSet = new OpenBitSet();
       
       //Set the bits for boundary docs (1=boundary doc, 0=not)
   
       idSet.set(0);
       idSet.set(10);
           
       BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 10);
        
       //simulate hits on all non-boundry docs    
       hitCollector.collect(1, 1f);
       hitCollector.collect(2, 2f);
       hitCollector.collect(3, 3f);
       hitCollector.collect(4, 4f);
       hitCollector.collect(5, 10f);
       hitCollector.collect(6, 4f);
       hitCollector.collect(7, 3f);
       hitCollector.collect(8, 2f);
       hitCollector.collect(9, 1f);
       
       BitSet bitSet = hitCollector.getResult();
       
       assertTrue(checkBitSet(bitSet,1,2,3,4,5,6,7,8,9));
   }
   
   public void testMultipleHitsAtTheEnd(){
       
       OpenBitSet idSet = new OpenBitSet();
       
       //Set the bits for boundary docs (1=boundary doc, 0=not)
   
       idSet.set(0);
       idSet.set(1);
       idSet.set(4);
       idSet.set(5); 
       idSet.set(10);
       idSet.set(11);
           
       BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 10);
         
       //simulate hits on all non-boundry docs    
       hitCollector.collect(6, 0f);
       hitCollector.collect(7, 3f);
       hitCollector.collect(8, 2f);
       hitCollector.collect(9, 1f);
       
       BitSet bitSet = hitCollector.getResult();
       
       assertTrue(checkBitSet(bitSet,6,7,8,9));  
   }
   
   public void testNoBoundaryDocAtTheEnd(){
       
       OpenBitSet idSet = new OpenBitSet();
       
       //Set the bits for boundary docs (1=boundary doc, 0=not)
   
       idSet.set(0);
       idSet.set(1);
       idSet.set(4);
       idSet.set(5);     
       
       BitSetBestScoreOfEntityHitCollector hitCollector = new BitSetBestScoreOfEntityHitCollector(idSet.iterator(), 9);
        
       //simulate hits on all non-boundry docs    
       hitCollector.collect(6, 0f);
       hitCollector.collect(7, 3f);
       hitCollector.collect(8, 2f);
       hitCollector.collect(9, 1f);
       
       BitSet bitSet = hitCollector.getResult();
       
       assertTrue(checkBitSet(bitSet,6,7,8,9));  
   }
 
   public void testScoreComparator(){
       ScoreComparator comparator = new BestScoreOfEntityHitCollector.ScoreComparator();
       
       ScoreDoc doc1 = new ScoreDoc(1,2f);
       ScoreDoc doc2 = new ScoreDoc(1,1f);
       ScoreDoc doc3 = new ScoreDoc(1,2f);
       
       assertTrue(comparator.compare(doc1, doc2) <= -1);
       
       assertTrue(comparator.compare(doc2, doc1) >= 1);
       
       assertTrue(comparator.compare(doc1, doc3) == 0);     
   }  
   
    /**
     * Score docs contain.
     * 
     * @param startDoc the start doc
     * @param endDoc the end doc
     * @param score the score
     * @param scoreDocs the score docs
     * 
     * @return true, if successful
     */
    private boolean scoreDocsContain(int startDoc, int endDoc, float score, List<ScoreDoc> scoreDocs){   
        for(ScoreDoc doc : scoreDocs){
            if( (startDoc <= doc.doc && doc.doc <= endDoc) && doc.score == score){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Score docs contain.
     * 
     * @param docId the doc id
     * @param score the score
     * @param scoreDocs the score docs
     * 
     * @return true, if successful
     */
    private boolean scoreDocsContain(int docId, float score, List<ScoreDoc> scoreDocs){    
        for(ScoreDoc doc : scoreDocs){
            if(doc.doc == docId && doc.score == score){
                return true;
            }
        }
        return false;
    }  
    
    protected boolean checkBitSet(BitSet bitSet, int... setBits){
    	BitSet expected = new BitSet();
    	for(int i : setBits){
    		expected.set(i);
    	}
    	return bitSet.equals(expected);
    }
}
