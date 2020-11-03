package org.lexgrid.loader.dao.template;

import org.LexGrid.naming.URIMap;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;

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
	
	/**
	 * Insert.
	 * 
	 * @param attrib the attrib
	 */
	@Override
	public void insert(String codingSchemeUri, String codingSchemeVersion, URIMap uriMap){
		
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
