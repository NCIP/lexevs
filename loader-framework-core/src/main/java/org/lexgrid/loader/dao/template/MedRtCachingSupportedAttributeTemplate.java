package org.lexgrid.loader.dao.template;

import org.LexGrid.naming.SupportedCodingScheme;
import org.LexGrid.naming.SupportedNamespace;
import org.LexGrid.naming.URIMap;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class MedRtCachingSupportedAttributeTemplate extends CachingSupportedAttribuiteTemplate {

	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	
	
	@Override
	public ExitStatus afterStep(StepExecution step) {
		this.flushCache();		
		return step.getExitStatus();
	}
	
	@Override
	public void afterJob(JobExecution arg0) {
		this.flushCache();
	}
	
//	/* (non-Javadoc)
//	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedCodingScheme(java.lang.String, java.lang.String, java.lang.String, java.lang.String, boolean)
//	 */
//	@Override
//	public void addSupportedCodingScheme(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
//			String content, boolean isImported) {
//		SupportedCodingScheme scs = new SupportedCodingScheme();
//		scs.setContent(content);
//		scs.setIsImported(isImported);
//		scs.setLocalId(localId);
//		scs.setUri(uri);
//		this.insert(codingSchemeName, codingSchemeVersion, scs);
//	}
//	
//	/* (non-Javadoc)
//	 * @see org.lexgrid.loader.dao.template.SupportedAttributeTemplate#addSupportedNamespace(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void addSupportedNamespace(String codingSchemeName, String codingSchemeVersion, String localId, String uri,
//			String content, String equivalentCodingScheme) {
//		SupportedNamespace sn = new SupportedNamespace();
//		sn.setContent(content);
//		sn.setEquivalentCodingScheme(equivalentCodingScheme);
//		sn.setLocalId(localId);
//		sn.setUri(uri);
//		this.insert(codingSchemeName, codingSchemeVersion, sn);
//	}
	/**
	 * Insert.
	 * 
	 * @param attrib the attrib
	 */
	@Override
	protected void insert(String codingSchemeUri, String codingSchemeVersion, URIMap uriMap){
		
		String key = this.buildCacheKey(uriMap);
		
		CodingSchemeIdHolder<URIMap> holder = new CodingSchemeIdHolder<URIMap>(
					createCodingSchemeIdSetter(
							codingSchemeIdSetter.getCodingSchemeUri(), codingSchemeIdSetter.getCodingSchemeVersion()), uriMap);

		this.getAttributeCache().putIfAbsent(key, holder);
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}
