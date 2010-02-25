package org.lexevs.system.service;

import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.system.utility.MyClassLoader;

public interface SystemResourceService {
	
	public MyClassLoader getClassLoader();

	public String createNewTablesForLoad();
	
	public void removeCodingSchemeResourceFromSystem(String uri, String version) throws LBParameterException;
	
	public void removeNonCodingSchemeResourceFromSystem(String uri) throws LBParameterException;;
	
	public String getInternalVersionStringForTag(String codingSchemeName, String tag) throws LBParameterException ;
	
	public String getInternalCodingSchemeNameForUserCodingSchemeName(String codingSchemeName, String version) throws LBParameterException ;
	
	public String getUriForUserCodingSchemeName(String codingSchemeName) throws LBParameterException ;

	public boolean containsCodingSchemeResource(String uri, String version) throws LBParameterException;
	
	public boolean containsNonCodingSchemeResource(String uri) throws LBParameterException;;
	
}


