
package org.lexevs.system.service;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.event.SystemEventListener;
import org.lexevs.system.utility.MyClassLoader;

/**
 * The Interface SystemResourceService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface SystemResourceService {
	
	/**
	 * Gets the class loader.
	 * 
	 * @return the class loader
	 */
	public MyClassLoader getClassLoader();
	
	public void initialize();
	
	public void refresh();
	
	public void shutdown();

	/**
	 * Creates the new tables for load.
	 * 
	 * @return the string
	 */
	public String createNewTablesForLoad();
	
	
	/**
	 * The Interface CodingSchemeMatcher.
	 */
	public static interface CodingSchemeMatcher {
		public boolean isMatch(CodingSchemeAliasHolder aliasHolder);
	}
	
	/**
	 * Gets the matching coding scheme resources.
	 *
	 * @param codingSchemeMatcher the coding scheme matcher
	 * @return the matching coding scheme resources
	 */
	public List<AbsoluteCodingSchemeVersionReference> getMatchingCodingSchemeResources(CodingSchemeMatcher codingSchemeMatcher);
	
	/**
	 * Adds the coding scheme resource to system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addCodingSchemeResourceToSystem(String uri, String version) throws LBParameterException;
	
	/**
	 * Adds the value set definition resource to system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addValueSetDefinitionResourceToSystem(String uri, String version) throws LBParameterException;
	
	/**
	 * Adds the pick list resource to system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addPickListDefinitionResourceToSystem(String uri, String version) throws LBParameterException;
	
	/**
	 * Adds the coding scheme resource to system.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void addNciHistoryResourceToSystem(String uri) throws LBParameterException;
	
	/**
	 * Removes the coding scheme resource from system.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void removeCodingSchemeResourceFromSystem(String uri, String version) throws LBParameterException;
	
	/**
	 * Removes the value set definition resource from system.
	 * 
	 * @param uri the value set URI
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void removeValueSetDefinitionResourceFromSystem(String valueSetDefinitionURI, String version) throws LBParameterException;
	
	/**
	 * Removes the pick list definition resource from system.
	 * 
	 * @param pickListId the pick list definition ID
	 * @param version the version
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void removePickListDefinitionResourceFromSystem(String pickListId, String version) throws LBParameterException;
	
	public void removeNciHistoryResourceToSystemFromSystem(String uri);
	/**
	 * Gets the internal version string for tag.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param tag the tag
	 * 
	 * @return the internal version string for tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public String getInternalVersionStringForTag(String codingSchemeName, String tag) throws LBParameterException ;
	
	/**
	 * Gets the internal coding scheme name for user coding scheme name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version the version
	 * 
	 * @return the internal coding scheme name for user coding scheme name
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public String getInternalCodingSchemeNameForUserCodingSchemeName(String codingSchemeName, String version) throws LBParameterException ;
	
	/**
	 * Gets the uri for user coding scheme name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param version TODO
	 * 
	 * @return the uri for user coding scheme name
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public String getUriForUserCodingSchemeName(String codingSchemeName, String version) throws LBParameterException ;

	/**
	 * Contains coding scheme resource.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean containsCodingSchemeResource(String uri, String version) throws LBParameterException;
	
	/**
	 * Contains value set definition resource.
	 * 
	 * @param uri the value set definition URI
	 * @param version the version
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean containsValueSetDefinitionResource(String uri, String version) throws LBParameterException;
	
	/**
	 * Contains pick list definition resource.
	 * 
	 * @param pickListId the pick list definition ID
	 * @param version the version
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean containsPickListDefinitionResource(String pickListId, String version) throws LBParameterException;
	
	/**
	 * Contains non coding scheme resource.
	 * 
	 * @param uri the uri
	 * 
	 * @return true, if successful
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public boolean containsNonCodingSchemeResource(String uri) throws LBParameterException;
	
	/**
	 * Update coding scheme resource tag.
	 * 
	 * @param codingScheme the coding scheme
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateCodingSchemeResourceTag(AbsoluteCodingSchemeVersionReference codingScheme, String newTag) throws LBParameterException;

	/**
	 * Update non coding scheme resource tag.
	 * 
	 * @param uri the uri
	 * @param newTag the new tag
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateNonCodingSchemeResourceTag(String uri, String newTag) throws LBParameterException;
	
	/**
	 * Update coding scheme resource status.
	 * 
	 * @param codingScheme the coding scheme
	 * @param status the status
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateCodingSchemeResourceStatus(AbsoluteCodingSchemeVersionReference codingScheme, CodingSchemeVersionStatus status) throws LBParameterException;
	
	public void registerCodingSchemeSupplement(AbsoluteCodingSchemeVersionReference parentScheme, AbsoluteCodingSchemeVersionReference supplement) throws LBParameterException;
	
	public void unRegisterCodingSchemeSupplement(AbsoluteCodingSchemeVersionReference parentScheme, AbsoluteCodingSchemeVersionReference supplement) throws LBParameterException;
	
	/**
	 * Update non coding scheme resource status.
	 * 
	 * @param uri the uri
	 * @param status the status
	 * 
	 * @throws LBParameterException the LB parameter exception
	 */
	public void updateNonCodingSchemeResourceStatus(String uri, CodingSchemeVersionStatus status) throws LBParameterException;

	public SystemVariables getSystemVariables();
	
	public void addSystemEventListeners(SystemEventListener listener);
}