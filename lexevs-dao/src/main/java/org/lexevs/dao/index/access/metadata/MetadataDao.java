
package org.lexevs.dao.index.access.metadata;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.MetadataPropertyList;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;

public interface MetadataDao {
	
	public MetadataPropertyList search(Query query);
	
	public void removeMetadata(
			String codingSchemeUri,
			String version);
	
	public AbsoluteCodingSchemeVersionReferenceList listCodingSchemes();
	
	public void addDocuments(String codingSchemeUri, String version, List<Document> documents, Analyzer analyzer);
}