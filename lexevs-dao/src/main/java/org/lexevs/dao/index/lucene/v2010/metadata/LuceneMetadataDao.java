package org.lexevs.dao.index.lucene.v2010.metadata;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.LexGrid.LexBIG.DataModel.Core.MetadataProperty;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.access.AbstractBaseIndexDao;
import org.lexevs.dao.index.access.metadata.MetadataDao;
import org.lexevs.dao.index.connection.IndexInterface;
import org.lexevs.dao.index.metadata.BaseMetaDataLoader;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;

public class LuceneMetadataDao implements MetadataDao {
	
	private BaseMetaDataLoader baseMetaDataLoader;
	
	private IndexInterface indexInterface;

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
