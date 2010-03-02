package org.lexevs.dao.index.lucene.entity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.entity.EntityDao;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.lucene.hitcollector.BestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.BitSetFilteringBestScoreOfEntityHitCollector;
import edu.mayo.informatics.indexer.lucene.hitcollector.HitCollectorMerger;

public class LuceneEntityDao extends AbstractBaseIndexDao implements EntityDao {
	
	public static LexEvsIndexFormatVersion supportedIndexVersion2009 = LexEvsIndexFormatVersion.parseStringToVersion("2009");
	public static LexEvsIndexFormatVersion supportedIndexVersion2010 = LexEvsIndexFormatVersion.parseStringToVersion("2010");
	
	private IndexInterface indexInterface;
	
	private SystemResourceService systemResourceService;

	public List<ScoreDoc> query(AbsoluteCodingSchemeVersionReference reference, List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) {
		try {
			String internalCodeSystemName = systemResourceService.
				getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), 
						reference.getCodingSchemeVersion());
			
			return this.buildScoreDocs(internalCodeSystemName, reference.getCodingSchemeVersion(), combinedQuery, bitSetQueries);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected List<ScoreDoc> buildScoreDocs(String internalCodeSystemName, String internalVersionString, List<? extends Query> combinedQuery, 
            List<? extends Query> bitSetQueries) throws Exception {

        SearchServiceInterface searcher = indexInterface.getSearcher(internalCodeSystemName, internalVersionString);

        int maxDoc = indexInterface.
            getIndexReader(internalCodeSystemName, internalVersionString).maxDoc();

        List<ScoreDoc> scoreDocs = null;

        List<BitSet> bitSets = new ArrayList<BitSet>();
        
        if(bitSetQueries != null){
            for(Query query : bitSetQueries){
                BitSetBestScoreOfEntityHitCollector bitSetCollector =
                    new BitSetBestScoreOfEntityHitCollector(
                    		indexInterface.getBoundaryDocumentIterator(
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
                        indexInterface.getBoundaryDocumentIterator(
                                internalCodeSystemName, 
                                internalVersionString), 
                                maxDoc);
            
            searcher.search(combinedQuery.get(0), null, collector);
            scoreDocs = collector.getResult();
        } else {
            HitCollectorMerger merger = new HitCollectorMerger(
            		indexInterface.getBoundaryDocumentIterator(
                            internalCodeSystemName, 
                            internalVersionString), maxDoc);
            for(Query query : combinedQuery){
                BestScoreOfEntityHitCollector collector =
                    new BitSetFilteringBestScoreOfEntityHitCollector(
                            this.andBitSets(bitSets),
                            indexInterface.getBoundaryDocumentIterator(
                                    internalCodeSystemName, 
                                    internalVersionString), 
                                    maxDoc); 

                searcher.search(query, null, collector);
                merger.addHitCollector(collector);
            }
            scoreDocs = merger.getMergedScoreDocs();
        }
        
        return scoreDocs;
	}
	
	public Document getDocumentById(
			AbsoluteCodingSchemeVersionReference reference, int documentId) {
		try {
		String internalCodeSystemName = systemResourceService.
			getInternalCodingSchemeNameForUserCodingSchemeName(reference.getCodingSchemeURN(), 
					reference.getCodingSchemeVersion());
		
		return indexInterface.getIndexReader(internalCodeSystemName, reference.getCodingSchemeVersion()).document(documentId);
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setIndexInterface(IndexInterface indexInterface) {
		this.indexInterface = indexInterface;
	}

	public IndexInterface getIndexInterface() {
		return indexInterface;
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

	@Override
	public List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions() {
		return DaoUtility.createList(LexEvsIndexFormatVersion.class, supportedIndexVersion2009, supportedIndexVersion2010);
	}
	
	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}
}
