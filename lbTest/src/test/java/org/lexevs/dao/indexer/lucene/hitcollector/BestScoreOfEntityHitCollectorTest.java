
package org.lexevs.dao.indexer.lucene.hitcollector;

import junit.framework.TestCase;

/**
 * The Class BestScoreOfEntityHitCollectorTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class BestScoreOfEntityHitCollectorTest extends TestCase {

    /**
     * Test no hits.
     * 
     * @throws Exception the exception
     */
    public void testNoHits() throws Exception {
        
 //       OpenBitSet idSet = new OpenBitSet();
        
        //Set the bits for boundary docs
//        idSet.set(0);
//        idSet.set(2);
//        idSet.set(4);
//        
//        BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 4);
        
//        List<ScoreDoc> scoreDocs = hitCollector.getResult();
        
//        assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 0);   
    }
    
//    /**
//     * Test hit all no multiples.
//     * 
//     * @throws Exception the exception
//     */
//    public void testHitAllNoMultiples() throws Exception {
//        
//        OpenBitSet idSet = new OpenBitSet();
//        
//        //Set the bits for boundary docs
//        idSet.set(0);
//        idSet.set(2);
//        idSet.set(4);
//        
//        BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 4);
//        
//        //simulate hits on all non-boundry docs    
//        hitCollector.collect(1, 1f);
//        hitCollector.collect(3, 1f);
//        
//        List<ScoreDoc> scoreDocs = hitCollector.getResult();
//        
//        assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 2);  
//        
//        assertTrue(scoreDocsContain(1,1f,scoreDocs));
//        assertTrue(scoreDocsContain(3,1f,scoreDocs));   
//    }
//    
//    /**
//     * Test hit all multiples same score.
//     */
//    public void testHitAllMultiplesSameScore(){
//        
//        OpenBitSet idSet = new OpenBitSet();
//        
//        //Set the bits for boundary docs (1=boundary doc, 0=not)
//        idSet.set(0);
//        idSet.set(3);
//        idSet.set(6);
//            
//        BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 6);
//        
//        //simulate hits on all non-boundry docs    
//        hitCollector.collect(1, 1f);
//        hitCollector.collect(2, 1f);
//        hitCollector.collect(4, 1f);
//        hitCollector.collect(5, 1f);
//        
//        List<ScoreDoc> scoreDocs = hitCollector.getResult();
//        
//        assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 2);  
//        
//        assertTrue(scoreDocsContain(1,2,1f,scoreDocs));
//        assertTrue(scoreDocsContain(4,5,1f,scoreDocs));   
//    }
//    
//    /**
//     * Test hit all multiples different score.
//     */
//    public void testHitAllMultiplesDifferentScore(){
//        
//        OpenBitSet idSet = new OpenBitSet();
//        
//        //Set the bits for boundary docs (1=boundary doc, 0=not)
//        idSet.set(0);
//        idSet.set(3);
//        idSet.set(6);
//            
//        BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(),6);
//        
//        //simulate hits on all non-boundry docs    
//        hitCollector.collect(1, 2f);
//        hitCollector.collect(2, 1f);
//        hitCollector.collect(4, 1f);
//        hitCollector.collect(5, 2f);
//        
//        List<ScoreDoc> scoreDocs = hitCollector.getResult();
//        
//        assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 2);  
//        
//        assertTrue(scoreDocsContain(1,2f,scoreDocs));
//        assertTrue(scoreDocsContain(5,2f,scoreDocs));   
//    }
//    
//   /**
//    * Test multiple boundary docs.
//    */
//   public void testMultipleBoundaryDocs(){
//        
//        OpenBitSet idSet = new OpenBitSet();
//        
//        //Set the bits for boundary docs (1=boundary doc, 0=not)
//        idSet.set(0);
//        idSet.set(1);
//        idSet.set(4);
//        idSet.set(5);
//        idSet.set(8);
//        idSet.set(10);
//        idSet.set(11);
//        idSet.set(12);
//        idSet.set(13);
//        idSet.set(14);
//        idSet.set(17);
//            
//        BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 18);
//        
//        //simulate hits on all non-boundry docs    
//        hitCollector.collect(2, 2f);
//        hitCollector.collect(3, 1f);
//        hitCollector.collect(6, 1f);
//        hitCollector.collect(7, 2f);
//        hitCollector.collect(15, 2f);
//        hitCollector.collect(18, 2f);
//        
//        List<ScoreDoc> scoreDocs = hitCollector.getResult();
//                
//        assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 4);  
//        
//        assertTrue(scoreDocsContain(2,2f,scoreDocs));
//        assertTrue(scoreDocsContain(7,2f,scoreDocs));   
//        assertTrue(scoreDocsContain(15,2f,scoreDocs));  
//        assertTrue(scoreDocsContain(18,2f,scoreDocs));  
//   }
//   
//   /**
//    * Test no bookend boundary docs.
//    */
//   public void testNoBookendBoundaryDocs(){
//       
//       OpenBitSet idSet = new OpenBitSet();
//       
//       //Set the bits for boundary docs 
//       idSet.set(1);
//       idSet.set(4);
//       idSet.set(5);
//       idSet.set(8);
//           
//       BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 9);
//       
//       //simulate hits on all non-boundry docs    
//       hitCollector.collect(0, 2f);
//       hitCollector.collect(9, 2f);
//       
//       List<ScoreDoc> scoreDocs = hitCollector.getResult();
//       
//       assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 2);  
//       
//       assertTrue(scoreDocsContain(0,2f,scoreDocs));
//       assertTrue(scoreDocsContain(9,2f,scoreDocs));   
//   }
//   
//   /**
//    * Test different boundary docs sizes.
//    */
//   public void testDifferentBoundaryDocsSizes(){
//       
//       OpenBitSet idSet = new OpenBitSet();
//       
//       //Set the bits for boundary docs (1=boundary doc, 0=not)
//   
//       idSet.set(0);
//       idSet.set(1);
//       idSet.set(2);
//       idSet.set(5);  
//       idSet.set(8);
//       idSet.set(9);
//       idSet.set(10);
//           
//       BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);
//       
//       //simulate hits on all non-boundry docs    
//       hitCollector.collect(3, 2f);
//       hitCollector.collect(4, 1f);
//       hitCollector.collect(6, 1f);
//       hitCollector.collect(7, 2f);
//       
//       List<ScoreDoc> scoreDocs = hitCollector.getResult();
//       
//       assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 2);  
//       
//       assertTrue(scoreDocsContain(3,2f,scoreDocs));
//       assertTrue(scoreDocsContain(7,2f,scoreDocs));   
//   }
//   
//   /**
//    * Test multiple different scores.
//    */
//   public void testMultipleDifferentScores(){
//       
//       OpenBitSet idSet = new OpenBitSet();
//       
//       //Set the bits for boundary docs (1=boundary doc, 0=not)
//   
//       idSet.set(0);
//       idSet.set(10);
//           
//       BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);
//       
//       //simulate hits on all non-boundry docs    
//       hitCollector.collect(1, 1f);
//       hitCollector.collect(2, 2f);
//       hitCollector.collect(3, 3f);
//       hitCollector.collect(4, 4f);
//       hitCollector.collect(5, 10f);
//       hitCollector.collect(6, 4f);
//       hitCollector.collect(7, 3f);
//       hitCollector.collect(8, 2f);
//       hitCollector.collect(9, 1f);
//       
//       List<ScoreDoc> scoreDocs = hitCollector.getResult();
//       
//       assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 1);  
//       
//       assertTrue(scoreDocsContain(5,10f,scoreDocs));
//   }
//   
//   /**
//    * Test multiple hits at the end.
//    */
//   public void testMultipleHitsAtTheEnd(){
//       
//       OpenBitSet idSet = new OpenBitSet();
//       
//       //Set the bits for boundary docs (1=boundary doc, 0=not)
//   
//       idSet.set(0);
//       idSet.set(1);
//       idSet.set(4);
//       idSet.set(5); 
//       idSet.set(10);
//       idSet.set(11);
//           
//       BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 11);
//       
//       //simulate hits on all non-boundry docs    
//       hitCollector.collect(6, 0f);
//       hitCollector.collect(7, 3f);
//       hitCollector.collect(8, 2f);
//       hitCollector.collect(9, 1f);
//       
//       List<ScoreDoc> scoreDocs = hitCollector.getResult();
//       
//       assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 1);  
//       
//       assertTrue(scoreDocsContain(7,3f,scoreDocs));
//   }
//   
//   /**
//    * Test multiple hits at the end.
//    */
//   public void testNoBoundaryDocAtTheEnd(){
//       
//       OpenBitSet idSet = new OpenBitSet();
//       
//       //Set the bits for boundary docs (1=boundary doc, 0=not)
//   
//       idSet.set(0);
//       idSet.set(1);
//       idSet.set(4);
//       idSet.set(5); 
//           
//       BestScoreOfEntityHitCollector hitCollector = new BestScoreOfEntityHitCollector(idSet.iterator(), 9);
//       
//       //simulate hits on all non-boundry docs    
//       hitCollector.collect(6, 0f);
//       hitCollector.collect(7, 3f);
//       hitCollector.collect(8, 2f);
//       hitCollector.collect(9, 1f);
//       
//       List<ScoreDoc> scoreDocs = hitCollector.getResult();
//       
//       assertTrue("Actual Size: " + scoreDocs.size(),scoreDocs.size() == 1);  
//       
//       assertTrue(scoreDocsContain(7,3f,scoreDocs));
//   }
//   
//   /**
//    * Test score comparator.
//    */
//   public void testScoreComparator(){
//       ScoreComparator comparator = new BestScoreOfEntityHitCollector.ScoreComparator();
//       
//       ScoreDoc doc1 = new ScoreDoc(1,2f);
//       ScoreDoc doc2 = new ScoreDoc(1,1f);
//       ScoreDoc doc3 = new ScoreDoc(1,2f);
//       
//       assertTrue(comparator.compare(doc1, doc2) <= -1);
//       
//       assertTrue(comparator.compare(doc2, doc1) >= 1);
//       
//       assertTrue(comparator.compare(doc1, doc3) == 0);     
//   }  
//   
//    /**
//     * Score docs contain.
//     * 
//     * @param startDoc the start doc
//     * @param endDoc the end doc
//     * @param score the score
//     * @param scoreDocs the score docs
//     * 
//     * @return true, if successful
//     */
//    private boolean scoreDocsContain(int startDoc, int endDoc, float score, List<ScoreDoc> scoreDocs){   
//        for(ScoreDoc doc : scoreDocs){
//            if( (startDoc <= doc.doc && doc.doc <= endDoc) && doc.score == score){
//                return true;
//            }
//        }
//        return false;
//    }
//    
//    /**
//     * Score docs contain.
//     * 
//     * @param docId the doc id
//     * @param score the score
//     * @param scoreDocs the score docs
//     * 
//     * @return true, if successful
//     */
//    private boolean scoreDocsContain(int docId, float score, List<ScoreDoc> scoreDocs){    
//        for(ScoreDoc doc : scoreDocs){
//            if(doc.doc == docId && doc.score == score){
//                return true;
//            }
//        }
//        return false;
//    }  
}