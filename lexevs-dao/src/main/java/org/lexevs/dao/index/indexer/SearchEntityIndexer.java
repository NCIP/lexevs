package org.lexevs.dao.index.indexer;

import java.util.Arrays;
import java.util.List;

import org.LexGrid.concepts.Entity;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.service.SystemResourceService;

public class SearchEntityIndexer implements EntityIndexer {
	
	/** The current index version. */
	private String currentIndexVersion = "2013";
	
	private SystemResourceService systemResourceService;

	@Override
	public List<Document> indexEntity(
			String codingSchemeUri,
			String codingSchemeVersion, 
			Entity entity) {
		Document document = new Document();
		
		String codeSystemKey = 
			LuceneLoaderCode.createCodingSchemeUriVersionKey(codingSchemeUri, codingSchemeVersion);
		
		String entityKey = 
			LuceneLoaderCode.
			createCodingSchemeUriVersionCodeNamespaceKey(
					codingSchemeUri, 
					codingSchemeVersion, 
					entity.getEntityCode(), 
					entity.getEntityCodeNamespace());
			
		document.add(
			this.toField(
					LuceneLoaderCode.CODING_SCHEME_URI_VERSION_KEY_FIELD, 
					codeSystemKey,
					Field.Store.NO,
					Field.Index.NOT_ANALYZED));
		
		document.add(
				this.toField(
						LuceneLoaderCode.CODING_SCHEME_URI_VERSION_CODE_NAMESPACE_KEY_FIELD, 
						entityKey,
						Field.Store.NO,
						Field.Index.NOT_ANALYZED));

		document.add(
				this.toField("description", 
						entity.getEntityDescription().getContent(),
						Field.Store.YES, 
						Field.Index.ANALYZED));
		
		document.add(
				this.toField("code", 
						entity.getEntityCode(),
						Field.Store.YES, 
						Field.Index.NOT_ANALYZED));
		
		return Arrays.asList(document);
	}
	
	protected Field toField(String fieldName, String text, Field.Store store, Field.Index index){
		return new Field(fieldName, text, store, index);
	}

	@Override
	public Analyzer getAnalyzer() {
		PerFieldAnalyzerWrapper analyzer =
		new PerFieldAnalyzerWrapper(new StandardAnalyzer());
		analyzer.addAnalyzer("code", new KeywordAnalyzer());
		
		return analyzer;
	}

	@Override
	public LexEvsIndexFormatVersion getIndexerFormatVersion() {
		return LexEvsIndexFormatVersion.parseStringToVersion(this.currentIndexVersion);
	}

	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

}
