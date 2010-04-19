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
package org.lexevs.system.service;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.utility.MyClassLoader;

/**
 * The Class DelegatingSystemResourceService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DelegatingSystemResourceService implements SystemResourceService {
	
	/** The primary system resource service. */
	private SystemResourceService primarySystemResourceService;
	
	/** The delegate system resource service. */
	private SystemResourceService delegateSystemResourceService;
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#containsCodingSchemeResource(java.lang.String, java.lang.String)
	 */
	public boolean containsCodingSchemeResource(String uri, String version)
			throws LBParameterException {
		return primarySystemResourceService.containsCodingSchemeResource(uri, version) ||
		delegateSystemResourceService.containsCodingSchemeResource(uri, version);
	}
	
	public boolean containsValueSetDefinitionResource(String uri, String version)
		throws LBParameterException {
		return primarySystemResourceService.containsValueSetDefinitionResource(uri, version);
	}
	
	public boolean containsPickListDefinitionResource(String pickListId, String version)
		throws LBParameterException {
		return primarySystemResourceService.containsPickListDefinitionResource(pickListId, version);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#containsNonCodingSchemeResource(java.lang.String)
	 */
	public boolean containsNonCodingSchemeResource(String uri)
			throws LBParameterException {
		return primarySystemResourceService.containsNonCodingSchemeResource(uri) ||
			delegateSystemResourceService.containsNonCodingSchemeResource(uri);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#createNewTablesForLoad()
	 */
	public String createNewTablesForLoad() {
		return primarySystemResourceService.createNewTablesForLoad();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getClassLoader()
	 */
	public MyClassLoader getClassLoader() {
		return primarySystemResourceService.getClassLoader();
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getInternalCodingSchemeNameForUserCodingSchemeName(java.lang.String, java.lang.String)
	 */
	public String getInternalCodingSchemeNameForUserCodingSchemeName(
			String codingSchemeName, String version)
			throws LBParameterException {
		try {
			return primarySystemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version);
		} catch (Exception e) {
			return delegateSystemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getInternalVersionStringForTag(java.lang.String, java.lang.String)
	 */
	public String getInternalVersionStringForTag(String codingSchemeName,
			String tag) throws LBParameterException {
		try {
			return primarySystemResourceService.getInternalVersionStringForTag(codingSchemeName, tag);
		} catch (Exception e) {
			return delegateSystemResourceService.getInternalVersionStringForTag(codingSchemeName, tag);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getUriForUserCodingSchemeName(java.lang.String)
	 */
	public String getUriForUserCodingSchemeName(String codingSchemeName) throws LBParameterException {
		try {
			return primarySystemResourceService.getUriForUserCodingSchemeName(codingSchemeName);
		} catch (Exception e) {
			return delegateSystemResourceService.getUriForUserCodingSchemeName(codingSchemeName);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#removeCodingSchemeResourceFromSystem(java.lang.String, java.lang.String)
	 */
	public void removeCodingSchemeResourceFromSystem(String uri, String version)
			throws LBParameterException {
		if(primarySystemResourceService.containsCodingSchemeResource(uri, version)){
			primarySystemResourceService.removeCodingSchemeResourceFromSystem(uri, version);
		} else if (delegateSystemResourceService.containsCodingSchemeResource(uri, version)){
			delegateSystemResourceService.removeCodingSchemeResourceFromSystem(uri, version);
		} else {
			throw new LBParameterException("Could not find CodingScheme:" + uri + " - " + version);
		}
		
	}
	
	public void removeValueSetDefinitionResourceFromSystem(String uri, String version)
		throws LBParameterException {
		if(primarySystemResourceService.containsValueSetDefinitionResource(uri, version)){
			primarySystemResourceService.removeValueSetDefinitionResourceFromSystem(uri, version);
		} else {
			throw new LBParameterException("Could not find value set definition : " + uri + " - " + version);
		}
	
	}
	
	public void removePickListDefinitionResourceFromSystem(String pickListId, String version)
	throws LBParameterException {
	if(primarySystemResourceService.containsPickListDefinitionResource(pickListId, version)){
		primarySystemResourceService.removePickListDefinitionResourceFromSystem(pickListId, version);
	} else {
		throw new LBParameterException("Could not find pick list definition : " + pickListId + " - " + version);
	}

}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#removeNonCodingSchemeResourceFromSystem(java.lang.String)
	 */
	public void removeNonCodingSchemeResourceFromSystem(String uri)
			throws LBParameterException {
		if(primarySystemResourceService.containsNonCodingSchemeResource(uri)){
			primarySystemResourceService.containsNonCodingSchemeResource(uri);
		} else if (delegateSystemResourceService.containsNonCodingSchemeResource(uri)){
			delegateSystemResourceService.containsNonCodingSchemeResource(uri);
		} else {
			throw new LBParameterException("Could not find Resource:" + uri);
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateCodingSchemeResourceTag(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, java.lang.String)
	 */
	public void updateCodingSchemeResourceTag(AbsoluteCodingSchemeVersionReference codingScheme, String newTag) 
		throws LBParameterException {
		if(primarySystemResourceService.containsCodingSchemeResource(
				codingScheme.getCodingSchemeURN(), 
				codingScheme.getCodingSchemeVersion())){
			primarySystemResourceService.updateCodingSchemeResourceTag(codingScheme, newTag);
		} else if (delegateSystemResourceService.containsCodingSchemeResource(
				codingScheme.getCodingSchemeURN(), 
				codingScheme.getCodingSchemeVersion())){
			delegateSystemResourceService.updateCodingSchemeResourceTag(codingScheme, newTag);
		} else {
			throw new LBParameterException("Could not find Resource:" + 
					codingScheme.getCodingSchemeURN() + ", " +
					codingScheme.getCodingSchemeVersion());
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateCodingSchemeResourceStatus(org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference, org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus)
	 */
	public void updateCodingSchemeResourceStatus(
			AbsoluteCodingSchemeVersionReference codingScheme,
			CodingSchemeVersionStatus status) throws LBParameterException {
		
		if(primarySystemResourceService.containsCodingSchemeResource(
				codingScheme.getCodingSchemeURN(), 
				codingScheme.getCodingSchemeVersion())){
			primarySystemResourceService.updateCodingSchemeResourceStatus(codingScheme, status);
		} else if (delegateSystemResourceService.containsCodingSchemeResource(
				codingScheme.getCodingSchemeURN(), 
				codingScheme.getCodingSchemeVersion())){
			delegateSystemResourceService.updateCodingSchemeResourceStatus(codingScheme, status);
		} else {
			throw new LBParameterException("Could not find Resource:" + 
					codingScheme.getCodingSchemeURN() + ", " +
					codingScheme.getCodingSchemeVersion());
		}	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateNonCodingSchemeResourceStatus(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus)
	 */
	public void updateNonCodingSchemeResourceStatus(String uri,
			CodingSchemeVersionStatus status) throws LBParameterException {
		
		if(primarySystemResourceService.containsNonCodingSchemeResource(uri)){
			primarySystemResourceService.updateNonCodingSchemeResourceStatus(uri, status);
		} else if (delegateSystemResourceService.containsNonCodingSchemeResource(uri)){
			delegateSystemResourceService.updateNonCodingSchemeResourceStatus(uri, status);
		} else {
			throw new LBParameterException("Could not find Resource: " + uri);
		}	
		
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#addCodingSchemeResourceToSystem(java.lang.String, java.lang.String)
	 */
	public void addCodingSchemeResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addCodingSchemeResourceToSystem(uri, version);
	}
	
	public void addValueSetDefinitionResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addValueSetDefinitionResourceToSystem(uri, version);
	}
	
	public void addPickListDefinitionResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addPickListDefinitionResourceToSystem(uri, version);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#addCodingSchemeResourceToSystem(org.LexGrid.codingSchemes.CodingScheme)
	 */
	public void addCodingSchemeResourceToSystem(CodingScheme codingScheme)
		throws LBParameterException {
		//primarySystemResourceService.addCodingSchemeResourceToSystem(codingScheme);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateNonCodingSchemeResourceTag(java.lang.String, java.lang.String)
	 */
	public void updateNonCodingSchemeResourceTag(String uri, String newTag)
			throws LBParameterException {
		
		if(primarySystemResourceService.containsNonCodingSchemeResource(uri)){
			primarySystemResourceService.updateNonCodingSchemeResourceTag(uri, newTag);
		} else if (delegateSystemResourceService.containsNonCodingSchemeResource(uri)){
			delegateSystemResourceService.updateNonCodingSchemeResourceTag(uri, newTag);
		} else {
			throw new LBParameterException("Could not find Resource: " + uri);
		}	
	}
	
	/**
	 * Gets the primary system resource service.
	 * 
	 * @return the primary system resource service
	 */
	public SystemResourceService getPrimarySystemResourceService() {
		return primarySystemResourceService;
	}
	
	/**
	 * Sets the primary system resource service.
	 * 
	 * @param primarySystemResourceService the new primary system resource service
	 */
	public void setPrimarySystemResourceService(
			SystemResourceService primarySystemResourceService) {
		this.primarySystemResourceService = primarySystemResourceService;
	}
	
	/**
	 * Gets the delegate system resource service.
	 * 
	 * @return the delegate system resource service
	 */
	public SystemResourceService getDelegateSystemResourceService() {
		return delegateSystemResourceService;
	}
	
	/**
	 * Sets the delegate system resource service.
	 * 
	 * @param delegateSystemResourceService the new delegate system resource service
	 */
	public void setDelegateSystemResourceService(
			SystemResourceService delegateSystemResourceService) {
		this.delegateSystemResourceService = delegateSystemResourceService;
	}

	@Override
	public SystemVariables getSystemVariables() {
		return this.primarySystemResourceService.getSystemVariables();
	}

	@Override
	public void initialize() {
		primarySystemResourceService.initialize();
		delegateSystemResourceService.initialize();
	}
	
	@Override
	public void refresh() {
		primarySystemResourceService.refresh();
		delegateSystemResourceService.refresh();
	}
}
