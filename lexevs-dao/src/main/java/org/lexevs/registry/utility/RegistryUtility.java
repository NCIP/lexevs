package org.lexevs.registry.utility;

import java.sql.Timestamp;
import java.util.Date;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;

public class RegistryUtility {

	public static RegistryEntry codingSchemeToRegistryEntry(CodingScheme codingScheme){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(codingScheme.getCodingSchemeURI());
		entry.setResourceVersion(codingScheme.getRepresentsVersion());
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		return entry;
	}
	
	public static RegistryEntry codingSchemeUriAndVersion(String uri, String version){
		RegistryEntry entry = new RegistryEntry();
		entry.setResourceType(ResourceType.CODING_SCHEME);
		entry.setDbSchemaDescription(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_DESCRIPTION);
		entry.setDbSchemaVersion(DatabaseConstants.CURRENT_LEXGRID_SCHEMA_VERSION);
		entry.setResourceUri(uri);
		entry.setResourceVersion(version);
		entry.setLastUpdateDate(new Timestamp(new Date().getTime()));
		
		return entry;
	}
}
