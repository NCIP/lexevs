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
package org.LexGrid.LexBIG.Impl.helpers.lazyloading;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.helpers.AdditiveCodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeHolder;
import org.LexGrid.LexBIG.Impl.helpers.CodeToReturn;
import org.LexGrid.LexBIG.Impl.helpers.DefaultCodeHolder;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.exceptions.MissingResourceException;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.ResourceManager;

import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.api.exceptions.IndexSearchException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;
import edu.mayo.informatics.indexer.lucene.hitcollector.BestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetFilteringBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.HitCollectorMerger;

/**
 * A factory for creating LazyCodeHolder objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLazyCodeHolderFactory implements CodeHolderFactory {
    
    private static final long serialVersionUID = 4365560788599358013L;

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Impl.helpers.lazyloading.CodeHolderFactory#buildCodeHolder(java.lang.String, java.lang.String, org.apache.lucene.search.Query)
     */
    public CodeHolder buildCodeHolder(String internalCodeSystemName,
            String internalVersionString, 
            List<BooleanQuery> combinedQuery, 
            List<Query> bitSetQueries) throws LBInvocationException, LBParameterException {
        try {
            //Match all docs (excluding code boundry docs) if no queries are provided
            if(combinedQuery == null || combinedQuery.size() == 0){
                combinedQuery = new ArrayList<BooleanQuery>();
                BooleanQuery booleanQuery = new BooleanQuery();
                booleanQuery.add(
                        new MatchAllDocsQuery(), Occur.MUST);
                booleanQuery.add(
                        new TermQuery(new Term("codeBoundry", "T")), Occur.MUST_NOT);
                combinedQuery.add(booleanQuery);
            }          
            
            AdditiveCodeHolder codeHolder = new DefaultCodeHolder();

            IndexInterface ii = ResourceManager.instance().getIndexInterface(internalCodeSystemName,
                    internalVersionString);

            SearchServiceInterface searcher = ii.getSearcher(internalCodeSystemName, internalVersionString);

            int maxDoc = ii.
                getIndexReader(internalCodeSystemName, internalVersionString).maxDoc();

            List<ScoreDoc> scoreDocs = null;

            List<BitSet> bitSets = new ArrayList<BitSet>();
            
            if(bitSetQueries != null){
                for(Query query : bitSetQueries){
                    BitSetBestScoreOfEntityHitCollector bitSetCollector =
                        new BitSetBestScoreOfEntityHitCollector(
                                ii.getBoundaryDocumentIterator(
                                        internalCodeSystemName, 
                                        internalVersionString), 
                                        maxDoc);
                    searcher.search(query, null, bitSetCollector);
                    bitSets.add(bitSetCollector.getResult());
                }
            }
            
            if(combinedQuery.size() == 1){
                BitSetFilteringBestScoreOfEntityHitCollector collector =
                    new BitSetFilteringBestScoreOfEntityHitCollector(
                            this.andBitSets(bitSets),
                            ii.getBoundaryDocumentIterator(
                                    internalCodeSystemName, 
                                    internalVersionString), 
                                    maxDoc);
                
                searcher.search(combinedQuery.get(0), null, collector);
                scoreDocs = collector.getResult();
            } else {
                HitCollectorMerger merger = new HitCollectorMerger(
                        ii.getBoundaryDocumentIterator(
                                internalCodeSystemName, 
                                internalVersionString), maxDoc);
                for(Query query : combinedQuery){
                    BestScoreOfEntityHitCollector collector =
                        new BitSetFilteringBestScoreOfEntityHitCollector(
                                this.andBitSets(bitSets),
                                ii.getBoundaryDocumentIterator(
                                        internalCodeSystemName, 
                                        internalVersionString), 
                                        maxDoc); 

                    searcher.search(query, null, collector);
                    merger.addHitCollector(collector);
                }
                scoreDocs = merger.getMergedScoreDocs();
            }
  
            for(ScoreDoc doc : scoreDocs){
                codeHolder.add(buildCodeToReturn(doc, internalCodeSystemName, internalVersionString));
            }

            return codeHolder;
        } catch (MissingResourceException e) {
           String logId = LoggerFactory.getLogger().error("Problem building the CodeHolder: ", e);
           throw new LBInvocationException(e.getLocalizedMessage(), logId);
        } catch (InternalIndexerErrorException e) {
            String logId = LoggerFactory.getLogger().error("Problem building the CodeHolder: ", e);
            throw new LBInvocationException(e.getLocalizedMessage(), logId);
        } catch (IndexSearchException e) {
            throw new LBParameterException(e.getLocalizedMessage());
        }
    }
    
    private BitSet andBitSets(List<BitSet> bitSets){
       BitSet totalBitSet = null;
        for(BitSet bitSet : bitSets){
            if(totalBitSet == null){
                totalBitSet = bitSet;
            } else {
                totalBitSet.and(bitSet);
            }
        }
        return totalBitSet;
    }
    
    /**
     * Builds the code to return.
     * 
     * @param doc the doc
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @return the code to return
     */
    protected abstract CodeToReturn buildCodeToReturn(ScoreDoc doc, String internalCodeSystemName, String internalVersionString);
}
