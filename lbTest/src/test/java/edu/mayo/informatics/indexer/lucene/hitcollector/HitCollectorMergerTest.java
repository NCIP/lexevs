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

import junit.framework.TestCase;

import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.OpenBitSet;

/**
 * The Class HitCollectorMergerTest.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HitCollectorMergerTest extends TestCase {

  
	public void testOneHit() throws Exception {
		OpenBitSet idSet = new OpenBitSet();

		//Set the bits for boundary docs
		idSet.set(0);
		idSet.set(4);
		idSet.set(10);

		BestScoreOfEntityHitCollector hc1 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc1.collect(1, 1.0f);
		hc1.collect(4, 1.0f);
		hc1.collect(3, 1.2f);

		BestScoreOfEntityHitCollector hc2 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc2.collect(1, 1.0f);
		hc2.collect(2, 1.0f);
		

		HitCollectorMerger merger = new HitCollectorMerger(idSet.iterator(), 10);

		List<BestScoreOfEntityHitCollector> collectors = new ArrayList<BestScoreOfEntityHitCollector>();
		collectors.add(hc2);
		collectors.add(hc1);

		merger.setHitCollectors(collectors);
		
		List<ScoreDoc> scoreDocs = merger.getMergedScoreDocs();
		
		assertTrue("Size: " + scoreDocs.size(), scoreDocs.size() == 1);
	}
	
	public void testMultipleHits() throws Exception {
		OpenBitSet idSet = new OpenBitSet();

		//Set the bits for boundary docs
		idSet.set(0);
		idSet.set(4);
		idSet.set(10);

		BestScoreOfEntityHitCollector hc1 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc1.collect(1, 1.0f);
		hc1.collect(5, 1.0f);

		BestScoreOfEntityHitCollector hc2 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc2.collect(1, 1.0f);
		hc2.collect(6, 1.0f);

		HitCollectorMerger merger = new HitCollectorMerger(idSet.iterator(), 10);

		List<BestScoreOfEntityHitCollector> collectors = new ArrayList<BestScoreOfEntityHitCollector>();
		collectors.add(hc1);
		collectors.add(hc2);

		merger.setHitCollectors(collectors);
		
		List<ScoreDoc> scoreDocs = merger.getMergedScoreDocs();
		
		assertTrue("Size: " + scoreDocs.size(), scoreDocs.size() == 2);
	}
	
	public void testAllHits() throws Exception {
		OpenBitSet idSet = new OpenBitSet();

		//Set the bits for boundary docs
		idSet.set(0);
		idSet.set(4);
		idSet.set(10);

		BestScoreOfEntityHitCollector hc1 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc1.collect(1, 1.0f);
		hc1.collect(2, 1.0f);
		hc1.collect(3, 1.0f);
		hc1.collect(5, 1.0f);
		hc1.collect(6, 1.0f);
		hc1.collect(7, 1.0f);
		hc1.collect(8, 1.0f);
		hc1.collect(9, 1.0f);

		BestScoreOfEntityHitCollector hc2 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc2.collect(1, 1.0f);
		hc2.collect(2, 1.0f);
		hc2.collect(3, 1.0f);
		hc2.collect(5, 1.0f);
		hc2.collect(6, 1.0f);
		hc2.collect(7, 1.0f);
		hc2.collect(8, 1.0f);
		hc2.collect(9, 1.0f);

		HitCollectorMerger merger = new HitCollectorMerger(idSet.iterator(), 10);

		List<BestScoreOfEntityHitCollector> collectors = new ArrayList<BestScoreOfEntityHitCollector>();
		collectors.add(hc1);
		collectors.add(hc2);

		merger.setHitCollectors(collectors);
		
		List<ScoreDoc> scoreDocs = merger.getMergedScoreDocs();
		
		assertTrue("Size: " + scoreDocs.size(), scoreDocs.size() == 2);
	}
	
	public void testOneDocSizeHit() throws Exception {
		OpenBitSet idSet = new OpenBitSet();

		//Set the bits for boundary docs
		idSet.set(0);
		idSet.set(4);
		idSet.set(6);
		idSet.set(10);

		BestScoreOfEntityHitCollector hc1 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc1.collect(5, 1.0f);

		BestScoreOfEntityHitCollector hc2 = new BestScoreOfEntityHitCollector(idSet.iterator(), 10);

		hc2.collect(5, 1.0f);

		HitCollectorMerger merger = new HitCollectorMerger(idSet.iterator(), 10);

		List<BestScoreOfEntityHitCollector> collectors = new ArrayList<BestScoreOfEntityHitCollector>();
		collectors.add(hc1);
		collectors.add(hc2);

		merger.setHitCollectors(collectors);
		
		List<ScoreDoc> scoreDocs = merger.getMergedScoreDocs();
		
		assertTrue("Size: " + scoreDocs.size(), scoreDocs.size() == 1);
	}
}