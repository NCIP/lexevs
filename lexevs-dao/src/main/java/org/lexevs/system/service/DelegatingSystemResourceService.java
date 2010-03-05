package org.lexevs.system.service;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.system.utility.MyClassLoader;

public class DelegatingSystemResourceService implements SystemResourceService {
	
	private SystemResourceService primarySystemResourceService;
	private SystemResourceService delegateSystemResourceService;
	
	public boolean containsCodingSchemeResource(String uri, String version)
			throws LBParameterException {
		return primarySystemResourceService.containsCodingSchemeResource(uri, version) ||
		delegateSystemResourceService.containsCodingSchemeResource(uri, version);
	}
	
	public boolean containsNonCodingSchemeResource(String uri)
			throws LBParameterException {
		return primarySystemResourceService.containsNonCodingSchemeResource(uri) ||
			delegateSystemResourceService.containsNonCodingSchemeResource(uri);
	}
	
	public String createNewTablesForLoad() {
		return primarySystemResourceService.createNewTablesForLoad();
	}
	
	public MyClassLoader getClassLoader() {
		return primarySystemResourceService.getClassLoader();
	}
	
	public String getInternalCodingSchemeNameForUserCodingSchemeName(
			String codingSchemeName, String version)
			throws LBParameterException {
		try {
			return primarySystemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version);
		} catch (Exception e) {
			return delegateSystemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeName, version);
		}
	}
	
	public String getInternalVersionStringForTag(String codingSchemeName,
			String tag) throws LBParameterException {
		try {
			return primarySystemResourceService.getInternalVersionStringForTag(codingSchemeName, tag);
		} catch (Exception e) {
			return delegateSystemResourceService.getInternalVersionStringForTag(codingSchemeName, tag);
		}
	}
	
	public String getUriForUserCodingSchemeName(String codingSchemeName) throws LBParameterException {
		try {
			return primarySystemResourceService.getUriForUserCodingSchemeName(codingSchemeName);
		} catch (Exception e) {
			return delegateSystemResourceService.getUriForUserCodingSchemeName(codingSchemeName);
		}
	}
	
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
	
	public void addCodingSchemeResourceToSystem(String uri, String version)
		throws LBParameterException {
		primarySystemResourceService.addCodingSchemeResourceToSystem(uri, version);
	}

	public void addCodingSchemeResourceToSystem(CodingScheme codingScheme)
		throws LBParameterException {
		primarySystemResourceService.addCodingSchemeResourceToSystem(codingScheme);
	}
	
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
	
	public SystemResourceService getPrimarySystemResourceService() {
		return primarySystemResourceService;
	}
	
	public void setPrimarySystemResourceService(
			SystemResourceService primarySystemResourceService) {
		this.primarySystemResourceService = primarySystemResourceService;
	}
	
	public SystemResourceService getDelegateSystemResourceService() {
		return delegateSystemResourceService;
	}
	
	public void setDelegateSystemResourceService(
			SystemResourceService delegateSystemResourceService) {
		this.delegateSystemResourceService = delegateSystemResourceService;
	}
}
