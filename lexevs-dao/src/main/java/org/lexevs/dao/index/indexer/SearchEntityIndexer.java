package org.lexevs.dao.index.indexer;

import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.KeywordTokenizer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.service.SystemResourceService;

import edu.mayo.informatics.indexer.lucene.analyzers.WhiteSpaceLowerCaseAnalyzer;

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

		for(Presentation presentation : entity.getPresentation()){
			if(presentation.getValue() != null 
				&& StringUtils.isNotBlank(presentation.getValue().getContent())){
				
				String content = presentation.getValue().getContent();
				document.add(this.toField("description", 
						content,
						Field.Store.NO, 
						Field.Index.ANALYZED));
				
				document.add(this.toField("exactDescription", 
						content,
						Field.Store.NO, 
						Field.Index.ANALYZED));
			}
		}
		
		if(entity.getEntityDescription() != null){
			document.add(
					this.toField("entityDescription", 
							entity.getEntityDescription().getContent(),
							Field.Store.YES, 
							Field.Index.NO));
		}
		
		document.add(
				this.toField("code", 
						entity.getEntityCode(),
						Field.Store.YES, 
						Field.Index.NOT_ANALYZED));
		
		document.add(
				this.toField("namespace", 
						entity.getEntityCodeNamespace(),
						Field.Store.YES, 
						Field.Index.NOT_ANALYZED));
		
		document.add(
				this.toField("codingSchemeUri", 
						codingSchemeUri,
						Field.Store.YES, 
						Field.Index.NO));
		
		document.add(
				this.toField("codingSchemeVersion", 
						codingSchemeVersion,
						Field.Store.YES, 
						Field.Index.NO));
		
		document.add(
				this.toField("anonymous", 
						BooleanUtils.toString(entity.getIsAnonymous(), "true", "false", "false"),
						Field.Store.NO, 
						Field.Index.NOT_ANALYZED));
		
		document.add(
				this.toField("active", 
						BooleanUtils.toString(entity.getIsActive(), "true", "false", "true"),
						Field.Store.NO, 
						Field.Index.NOT_ANALYZED));
		
		String codingSchemeName;
		try {
			codingSchemeName = 
				this.systemResourceService.
					getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, codingSchemeVersion);
		} catch (LBParameterException e) {
			throw new RuntimeException(e);
		}
		
		document.add(
				this.toField("codingSchemeName", 
						codingSchemeName,
						Field.Store.YES, 
						Field.Index.NO));
		
		String[] entityTypes = entity.getEntityType();
		if(entityTypes != null){
			for(String entityType : entityTypes){
				document.add(
						this.toField("type", 
								entityType,
								Field.Store.YES, 
								Field.Index.NOT_ANALYZED));
			}
		}

		
		return Arrays.asList(document);
	}
	
	protected Field toField(String fieldName, String text, Field.Store store, Field.Index index){
		return new Field(fieldName, text, store, index);
	}

	@Override
	public Analyzer getAnalyzer() {
		PerFieldAnalyzerWrapper analyzer =
		new PerFieldAnalyzerWrapper(new WhiteSpaceLowerCaseAnalyzer(new String[] {},
                WhiteSpaceLowerCaseAnalyzer.getDefaultCharRemovalSet(), LuceneLoaderCode.lexGridWhiteSpaceIndexSet));
		
		analyzer.addAnalyzer("code", new KeywordAnalyzer());
		analyzer.addAnalyzer("namespace", new KeywordAnalyzer());
		analyzer.addAnalyzer("exactDescription", new LowerCaseKeywordAnalyzer());
		
		return analyzer;
	}
	
	private class LowerCaseKeywordAnalyzer extends Analyzer {

		@Override
		public TokenStream tokenStream(String fieldName, Reader reader) {		
			TokenStream tokenStream = new KeywordTokenizer(reader);
			
			return new LowerCaseFilter(tokenStream);
		}			
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
