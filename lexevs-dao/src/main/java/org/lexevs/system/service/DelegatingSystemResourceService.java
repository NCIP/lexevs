package org.lexevs.system.service;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
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
