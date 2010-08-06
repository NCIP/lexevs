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
package org.lexgrid.loader.dao.template;

import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.LexGrid.naming.URIMap;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.data.codingScheme.SimpleCodingSchemeIdSetter;
import org.lexgrid.loader.wrappers.CodingSchemeIdHolder;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * The Class CachingSupportedAttribuiteTemplate.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CachingSupportedAttribuiteTemplate extends AbstractSupportedAttributeTemplate implements JobExecutionListener {
	
	private DatabaseServiceManager databaseServiceManager;

	/** The attribute cache. */
	private ConcurrentHashMap<String,CodingSchemeIdHolder<URIMap>> attributeCache = new ConcurrentHashMap<String,CodingSchemeIdHolder<URIMap>>();

	
	@Override
	public void afterJob(JobExecution arg0) {
		this.flushCache();
	}

	@Override
	public void beforeJob(JobExecution arg0) {
		//
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.dao.template.AbstractSupportedAttributeTemplate#insert(org.LexGrid.persistence.model.CodingSchemeSupportedAttrib)
	 */
	@Override
	protected void insert(String codingSchemeUri, String codingSchemeVersion, final URIMap uriMap){
	
		String key = this.buildCacheKey(uriMap);
		
		CodingSchemeIdHolder<URIMap> holder = new CodingSchemeIdHolder<URIMap>(
					createCodingSchemeIdSetter(codingSchemeUri, codingSchemeVersion), uriMap);

		attributeCache.putIfAbsent(key, holder);
	}
	
	protected CodingSchemeIdSetter createCodingSchemeIdSetter(String uri, String version) {
		return new SimpleCodingSchemeIdSetter(uri,version);
	}

	public void flushCache() {
		this.getLogger().info("Flushing SupportedAttribute Cache.");
		
		this.getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<Void>() {

			@Override
			public Void execute(DaoManager daoManager) {
				Enumeration<CodingSchemeIdHolder<URIMap>> elements = attributeCache.elements();

				while(elements.hasMoreElements()) {
					CodingSchemeIdHolder<URIMap> element = elements.nextElement();
					String codingSchemeUri = element.getCodingSchemeIdSetter().getCodingSchemeUri();
					String codingSchemeVersion = element.getCodingSchemeIdSetter().getCodingSchemeVersion();

					CodingSchemeDao dao = 
						daoManager.getCodingSchemeDao(codingSchemeUri, codingSchemeVersion);

					String codingSchemeUid = dao.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);

					dao.insertOrUpdateURIMap(codingSchemeUid, element.getItem());
				}
				
				return null;
			}
		});
	}
	
	protected String buildCacheKey(URIMap map){
		return map.getClass().getName() +
			map.getContent() +
			map.getLocalId() +
			map.getUri();
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}	
}
