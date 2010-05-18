package org.lexevs.dao.index.lucene.v2010.metadata;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.index.access.metadata.MetadataDao;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.dao.index.metadata.BaseMetaDataLoader;

import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;
import edu.mayo.informatics.indexer.lucene.LuceneIndexReader;

public class LuceneMetadataDao implements MetadataDao {
	
	private BaseMetaDataLoader baseMetaDataLoader;
	
	private IndexInterface indexInterface;

	@Override
	public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes() {
	       AbsoluteCodingSchemeVersionReferenceList result = new AbsoluteCodingSchemeVersionReferenceList();

           try {
			LuceneIndexReader ir = indexInterface.getMetaDataIndexReader();
			   TermEnum te = ir.getBaseIndexReader().terms(new Term("codingSchemeNameVersion", ""));

			   SearchServiceInterface ssi = indexInterface.getMetaDataSearcher();

			   boolean hasNext = true;
			   while (hasNext && te.term().field().equals("codingSchemeNameVersion")) {
			       Query temp = new TermQuery(new Term(te.term().field(), te.term().text()));

			       Document[] d = ssi.search(temp, null, true, 1);
			       if (d.length > 0) {

			           Document doc = d[0];
			           AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
			           acsvr.setCodingSchemeURN(doc.get("codingSchemeRegisteredName"));
			           acsvr.setCodingSchemeVersion(doc.get("codingSchemeVersion"));

			           result.addAbsoluteCodingSchemeVersionReference(acsvr);
			       }
			       hasNext = te.next();
			   }
			   te.close();
			   
			   return result;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	@Override
	public void removeMetadata(String codingSchemeUri, String version) {
		try {
			baseMetaDataLoader.removeMeta(codingSchemeUri, version);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public MetadataPropertyList search(Query query) {
		SearchServiceInterface ssi = indexInterface.getMetaDataSearcher();
        try {
        	Document[] d = ssi.search(query, null, false, -1);
	
		 MetadataPropertyList mdpl = new MetadataPropertyList();

         // assemble the result object
         for (int i = 0; i < d.length; i++) {
             MetadataProperty curr = new MetadataProperty();
             curr.setCodingSchemeURI(d[i].get("codingSchemeRegisteredName"));
             curr.setCodingSchemeVersion(d[i].get("codingSchemeVersion"));
             curr.setName(d[i].get("propertyName"));
             curr.setValue(d[i].get("propertyValue"));

             String temp = d[i].get("parentContainers");
             curr.setContext(temp.split(BaseMetaDataLoader.STRING_TOKEINZER_TOKEN));

             mdpl.addMetadataProperty(curr);
         }
         
         return mdpl;
             
     	} catch (InternalIndexerErrorException e) {
			throw new RuntimeException(e);
		}
	}

	public IndexInterface getIndexInterface() {
		return indexInterface;
	}

	public void setIndexInterface(IndexInterface indexInterface) {
		this.indexInterface = indexInterface;
	}

	public void setBaseMetaDataLoader(BaseMetaDataLoader baseMetaDataLoader) {
		this.baseMetaDataLoader = baseMetaDataLoader;
	}

	public BaseMetaDataLoader getBaseMetaDataLoader() {
		return baseMetaDataLoader;
	}
}
