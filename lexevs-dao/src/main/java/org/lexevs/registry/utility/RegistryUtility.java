
package org.lexevs.registry.utility;

import java.sql.Timestamp;
import java.util.Date;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

/**
 * The Class RegistryUtility.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RegistryUtility {

	/**
	 * Coding scheme to registry entry.
	 * 
	 * @param codingScheme the coding scheme
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry codingSchemeToRegistryEntry(CodingScheme codingScheme){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(codingScheme.getCodingSchemeURI());
		entry.setResourceVersion(codingScheme.getRepresentsVersion());
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		//TODO: Fix this for multiple sets of tables
		entry.setPrefix("");
		
		return entry;
	}
	
	/**
	 * Coding scheme to registry entry.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry codingSchemeToRegistryEntry(String uri, String version){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		return entry;
	}
	
	/**
	 * Non Coding scheme entries like Concept Domain, Usage Context etc. to registry entry.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * @param resourceType entry type
	 * @return the registry entry
	 */
	public static RegistryEntry nonCodingSchemeToRegistryEntry(String uri, String version, ResourceType resourceType){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(resourceType);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		return entry;
	}
	
	public static RegistryEntry nciHistoryToRegistryEntry(String uri){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.NCI_HISTORY);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		return entry;
	}
	
	/**
	 * Value Set Definition to registry entry.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry valueSetDefinitionToRegistryEntry(String uri, String version){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.VALUESET_DEFINITION);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		//TODO: Fix this for multiple sets of tables
		entry.setPrefix("");
		
		return entry;
	}
	
	/**
	 * Pick List Definition to registry entry.
	 * 
	 * @param uri the uri
	 * @param version the version
	 * 
	 * @return the registry entry
	 */
	public static RegistryEntry pickListDefinitionToRegistryEntry(String uri, String version){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.PICKLIST_DEFINITION);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		//TODO: Fix this for multiple sets of tables
		entry.setPrefix("");
		
		return entry;
	}
}