
package org.lexevs.system.service;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.cache.annotation.CacheMethod;
import org.lexevs.cache.annotation.Cacheable;
import org.lexevs.cache.annotation.ClearCache;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.event.SystemEventListener;
import org.lexevs.system.event.SystemEventSupport;
import org.lexevs.system.utility.MyClassLoader;

/**
 * The Class DelegatingSystemResourceService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Cacheable(cacheName = "DelegatingSystemResourceServiceCache")
public class DelegatingSystemResourceService extends SystemEventSupport implements SystemResourceService {
	
	/** The primary system resource service. */
	private SystemResourceService primarySystemResourceService;
	
	/** The delegate system resource service. */
	private SystemResourceService delegateSystemResourceService;
	
	@Override
	public List<AbsoluteCodingSchemeVersionReference> getMatchingCodingSchemeResources(CodingSchemeMatcher codingSchemeMatcher) {
		return primarySystemResourceService.getMatchingCodingSchemeResources(codingSchemeMatcher);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#containsCodingSchemeResource(java.lang.String, java.lang.String)
	 */
	@CacheMethod
	public boolean containsCodingSchemeResource(String uri, String version)
			throws LBParameterException {
		return primarySystemResourceService.containsCodingSchemeResource(uri, version) ||
		delegateSystemResourceService.containsCodingSchemeResource(uri, version);
	}
	
	@CacheMethod
	public boolean containsValueSetDefinitionResource(String uri, String version)
		throws LBParameterException {
		return primarySystemResourceService.containsValueSetDefinitionResource(uri, version);
	}
	
	@CacheMethod
	public boolean containsPickListDefinitionResource(String pickListId, String version)
		throws LBParameterException {
		return primarySystemResourceService.containsPickListDefinitionResource(pickListId, version);
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#containsNonCodingSchemeResource(java.lang.String)
	 */
	@CacheMethod
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
	@CacheMethod
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
	@CacheMethod
	public String getInternalVersionStringForTag(String codingSchemeName,
			String tag) throws LBParameterException {
		LBParameterException exception;

		try {
			return primarySystemResourceService.getInternalVersionStringForTag(codingSchemeName, tag);
		} catch (LBParameterException e) {
			exception = e;
			try {
				return delegateSystemResourceService.getInternalVersionStringForTag(codingSchemeName, tag);
			} catch (LBParameterException e1) {
				throw exception;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#getUriForUserCodingSchemeName(java.lang.String)
	 */
	@CacheMethod
	public String getUriForUserCodingSchemeName(String codingSchemeName, String version) throws LBParameterException {
		LBParameterException exception;
		try {
			return primarySystemResourceService.getUriForUserCodingSchemeName(codingSchemeName, version);
		} catch (LBParameterException e) {
			exception = e;
			try {
				return delegateSystemResourceService.getUriForUserCodingSchemeName(codingSchemeName, version);
			} catch (LBParameterException e1) {
				throw exception;
			}
		}
	}

	@Override
	@ClearCache(clearAll=true)
	public void removeNciHistoryResourceToSystemFromSystem(String uri) {
		try {
			if(primarySystemResourceService.containsNonCodingSchemeResource(uri)){
				primarySystemResourceService.removeNciHistoryResourceToSystemFromSystem(uri);
			} else {
				this.delegateSystemResourceService.removeNciHistoryResourceToSystemFromSystem(uri);
			}
		} catch (LBParameterException e) {
			this.delegateSystemResourceService.removeNciHistoryResourceToSystemFromSystem(uri);
		}
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#removeCodingSchemeResourceFromSystem(java.lang.String, java.lang.String)
	 */
	@ClearCache(clearAll=true)
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
	
	@ClearCache(clearAll=true)
	public void removeValueSetDefinitionResourceFromSystem(String uri, String version)
		throws LBParameterException {
		if(primarySystemResourceService.containsValueSetDefinitionResource(uri, version)){
			primarySystemResourceService.removeValueSetDefinitionResourceFromSystem(uri, version);
		} else {
			throw new LBParameterException("Could not find value set definition : " + uri + " - " + version);
		}
	
	}
	
	@ClearCache(clearAll=true)
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
	@ClearCache(clearAll=true)
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
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingDatabaseToXmlRegistryCache"})
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
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingDatabaseToXmlRegistryCache"})
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
	@ClearCache(clearCaches = {"DatabaseRegistryCache","DelegatingDatabaseToXmlRegistryCache"})
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

	@Override
	@ClearCache(clearAll=true)
	public void registerCodingSchemeSupplement(
			AbsoluteCodingSchemeVersionReference parentScheme,
			AbsoluteCodingSchemeVersionReference supplement)
			throws LBParameterException {
		if(
				primarySystemResourceService.containsCodingSchemeResource(parentScheme.getCodingSchemeURN(), parentScheme.getCodingSchemeVersion())
						&&
				primarySystemResourceService.containsCodingSchemeResource(supplement.getCodingSchemeURN(), supplement.getCodingSchemeVersion())){
			
			primarySystemResourceService.registerCodingSchemeSupplement(parentScheme, supplement);
		} else {
			throw new LBParameterException("Could not find Resource");
		}	
	}
	
	@Override
	@ClearCache(clearAll=true)
	public void unRegisterCodingSchemeSupplement(
			AbsoluteCodingSchemeVersionReference parentScheme,
			AbsoluteCodingSchemeVersionReference supplement)
			throws LBParameterException {
		if(
				primarySystemResourceService.containsCodingSchemeResource(parentScheme.getCodingSchemeURN(), parentScheme.getCodingSchemeVersion())
						&&
				primarySystemResourceService.containsCodingSchemeResource(supplement.getCodingSchemeURN(), supplement.getCodingSchemeVersion())){
			
			primarySystemResourceService.unRegisterCodingSchemeSupplement(parentScheme, supplement);
		} else {
			throw new LBParameterException("Could not find Resource");
		}	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#addCodingSchemeResourceToSystem(java.lang.String, java.lang.String)
	 */
	@ClearCache(clearAll=true)
	public void addCodingSchemeResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addCodingSchemeResourceToSystem(uri, version);
	}
	
	@ClearCache(clearAll=true)
	public void addNciHistoryResourceToSystem(String uri)
		throws LBParameterException {
		primarySystemResourceService.addNciHistoryResourceToSystem(uri);
	}
	
	@ClearCache(clearAll=true)
	public void addValueSetDefinitionResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addValueSetDefinitionResourceToSystem(uri, version);
	}
	
	@ClearCache(clearAll=true)
	public void addPickListDefinitionResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addPickListDefinitionResourceToSystem(uri, version);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.system.service.SystemResourceService#updateNonCodingSchemeResourceTag(java.lang.String, java.lang.String)
	 */
	@ClearCache(clearCaches = {"DatabaseRegistry","DelegatingDatabaseToXmlRegistry"})
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
	@ClearCache(clearAll=true)
	public void refresh() {
		primarySystemResourceService.refresh();
		delegateSystemResourceService.refresh();
	}

	@Override
	public void addSystemEventListeners(SystemEventListener listener) {
		primarySystemResourceService.addSystemEventListeners(listener);
	}

	@Override
	public void shutdown() {
		delegateSystemResourceService.shutdown();
		primarySystemResourceService.shutdown();
	}
}