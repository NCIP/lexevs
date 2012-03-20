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
package org.lexgrid.loader.rxn.reader.support;

//import org.LexGrid.persistence.dao.LexEvsDao;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.processor.support.EntityCodeResolver;

/**
 * The Class AbstractSabSkippingPolicy.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSabAndCodeSkippingPolicy<I> extends AbstractSabSkippingPolicy<I> {
	

	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeNameSetter;
	private EntityCodeResolver<I> entityCodeResolver;
	private DatabaseServiceManager databaseServiceManager;




	/* (non-Javadoc)
	 * @see org.lexgrid.loader.reader.support.SkipPolicy#toSkip(java.lang.Object)
	 */
	public boolean toSkip(I item) {
		if(super.toSkip(item)){
			return true;
		}
		if (doesEntityNotExist(item)) {
			return true;
		}
		return false;
	}
	
	
	private boolean doesEntityNotExist(I item)  {

		String code = entityCodeResolver.getEntityCode(item);
		String codingSchemeUri = 
				codingSchemeNameSetter.getCodingSchemeUri();
			
			String version = 
				codingSchemeNameSetter.getCodingSchemeVersion();
		
		String codingSchemeId = getCodingSchemeId(codingSchemeUri, version);
		
		String entityId = null;
		

		try {

			entityId = getEntityIdForCode(codingSchemeId, code, codingSchemeNameSetter.getCodingSchemeName());
		} catch (Exception ex) {
			ex.printStackTrace();			
		}
		if (entityId == null) {
			return true;
		} else {
			return false;
		}
	}	
	
	
	
	protected String getEntityIdForCode(final String codingSchemeId, final String entityCode, final String entityCodeNamespace) {
		return this.databaseServiceManager.getDaoCallbackService().
			executeInDaoLayer(new DaoCallback<String>() {

			public String execute(DaoManager daoManager) {
				return 
					daoManager.getCurrentEntityDao().getEntityUId(codingSchemeId, entityCode, entityCodeNamespace);
			}
			
		});
	}

	protected String getCodingSchemeId(final String codingSchemeUri, final String version) {
		return this.databaseServiceManager.getDaoCallbackService().
			executeInDaoLayer(new DaoCallback<String>() {

			public String execute(DaoManager daoManager) {
				return 
					daoManager.getCurrentCodingSchemeDao().getCodingSchemeUIdByUriAndVersion(codingSchemeUri, version);
			}
		});
	}
	
	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeNameSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeIdSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}	
	
	public EntityCodeResolver<I> getEntityCodeResolver() {
		return entityCodeResolver;
	}


	public void setEntityCodeResolver(EntityCodeResolver<I> entityCodeResolver) {
		this.entityCodeResolver = entityCodeResolver;
	}	
	
	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}


	public void setDatabaseServiceManager(
			DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}


}
