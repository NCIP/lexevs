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
package org.lexevs.dao.index.lucene.v2010.metadata;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.util.BytesRef;
import org.lexevs.dao.index.access.metadata.MetadataDao;
import org.lexevs.dao.index.lucenesupport.LuceneIndexTemplate;
import org.lexevs.dao.index.lucenesupport.BaseLuceneIndexTemplate.IndexReaderCallback;
import org.lexevs.dao.index.lucenesupport.MetaDataIndexTemplate;
import org.lexevs.dao.index.metadata.BaseMetaDataLoader;

public class LuceneMetadataDao implements MetadataDao {
	
	private BaseMetaDataLoader baseMetaDataLoader;
	
	private LuceneIndexTemplate luceneIndexTemplate;
	
	@Override
	public void addDocuments(String codingSchemeUri, String version,
			List<Document> documents, Analyzer analyzer) {
		this.luceneIndexTemplate.addDocuments(documents, analyzer);		
	}
	
	@Override
	public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes() {

	       AbsoluteCodingSchemeVersionReferenceList result = new AbsoluteCodingSchemeVersionReferenceList();
	       
           try {
        	   final TermsEnum te = luceneIndexTemplate.executeInIndexReader(new IndexReaderCallback<TermsEnum>() {

				@Override
				public TermsEnum doInIndexReader(IndexReader indexReader)
						throws Exception {
					TermsEnum termsEnum = null;
					Fields fields = MultiFields.getFields(indexReader);
					if(fields != null){
						Terms terms = fields.terms("codingSchemeNameVersion");
						if(terms != null){
				
							termsEnum = terms.iterator();
						}
					}
					// TODO - Predetermine the number of hits - remove the 1000
//					TopScoreDocCollector collector = TopScoreDocCollector.create(1000);
//					
//					IndexSearcher indexSearcher = new IndexSearcher(indexReader);
//					Query temp = new TermQuery(new Term("codingSchemeNameVersion","*"));
//					indexSearcher.search(temp, collector);					
//					TopDocs topDocs = collector.topDocs();

					return termsEnum;
				}  
        	   });
        	   
//        	   ScoreDoc[] scoreDocs = td.scoreDocs;
//        	   
//        	   for (ScoreDoc scoreDoc : scoreDocs) {
//        		   AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
//        		   Document document = luceneIndexTemplate.getDocumentById(scoreDoc.doc);
//        		   acsvr.setCodingSchemeURN(document.get("codingSchemeRegisteredName"));
//		           acsvr.setCodingSchemeVersion(document.get("codingSchemeVersion"));
//
//		           result.addAbsoluteCodingSchemeVersionReference(acsvr);
//        	   }
// TODO see Multifield for a better implementation of this.
			  BytesRef text = null;
			   while ((te != null) && (text = te.next()) != null) {
			       Query temp = new TermQuery(new Term("codingSchemeNameVersion", text.utf8ToString()));

			       List<ScoreDoc> d = this.luceneIndexTemplate.search(temp, null);
			       if (d.size() > 0) {

			           ScoreDoc doc = d.get(0);
			           AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
			           
			           Document document = luceneIndexTemplate.getDocumentById(doc.doc);
			           acsvr.setCodingSchemeURN(document.get("codingSchemeRegisteredName"));
			           acsvr.setCodingSchemeVersion(document.get("codingSchemeVersion"));

			           result.addAbsoluteCodingSchemeVersionReference(acsvr);
			       }

			   }

			   
			   return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public void removeMetadata(String codingSchemeUri, String version) {
		this.luceneIndexTemplate.removeDocuments(
				new Term("codingSchemeNameVersion",
						codingSchemeUri
						+ BaseMetaDataLoader.CONCATINATED_VALUE_SPLIT_TOKEN + version));
	}

	@Override
	public MetadataPropertyList search(Query query) {
		
		 List<ScoreDoc> docs = this.luceneIndexTemplate.search(query, null);
	
		 MetadataPropertyList mdpl = new MetadataPropertyList();

         // assemble the result object
         for (ScoreDoc doc : docs) {
        	 Document d = luceneIndexTemplate.getDocumentById(doc.doc);
        	 
             MetadataProperty curr = new MetadataProperty();
             curr.setCodingSchemeURI(d.get("codingSchemeRegisteredName"));
             curr.setCodingSchemeVersion(d.get("codingSchemeVersion"));
             curr.setName(d.get("propertyName"));
             curr.setValue(d.get("propertyValue"));

             String temp = d.get("parentContainers");
             curr.setContext(temp.split(BaseMetaDataLoader.STRING_TOKENIZER_TOKEN));

             mdpl.addMetadataProperty(curr);
         }
         
         return mdpl;

	}


	public BaseMetaDataLoader getBaseMetaDataLoader() {
		return baseMetaDataLoader;
	}

	public void setBaseMetaDataLoader(BaseMetaDataLoader baseMetaDataLoader) {
		this.baseMetaDataLoader = baseMetaDataLoader;
	}

	public void setLuceneIndexTemplate(LuceneIndexTemplate luceneIndexTemplate) {
		this.luceneIndexTemplate = luceneIndexTemplate;
	}

	public LuceneIndexTemplate getLuceneIndexTemplate() {
		return luceneIndexTemplate;
	}
	
	
}