
package org.lexevs.dao.index.service.search;

import org.LexGrid.annotations.LgAdminFunction;
import org.LexGrid.concepts.Entity;

public interface AssertedValueSetSearchIndexService extends SearchIndexService {

/**
	 * Updates an indexed entity by first deleting the old entity, then adding the
	 * new entity
	 * 
	 * @param codingSchemeUri - source asserted value set source system uri
	 * @param codingSchemeVersion - source asserted value set source system version
	 * @param vsURI - coding scheme representation of value set URI
	 * @param vsName - coding scheme representation of value set name
	 * @param entity - full entity representation of a value in a value set
	 */
@LgAdminFunction
	public void updateIndexForEntity(String codingSchemeUri,String codingSchemeVersion,
			String vsURI, String vsName, Entity entity);
	
	/**
	 * Adds an entity to the index targeted by source coding scheme and defined
	 * further by the value set URI
	 * 
	 * @param codingSchemeUri - source asserted value set source system uri
	 * @param codingSchemeVersion - source asserted value set source system version
	 * @param vsURI - coding scheme representation of value set URI
	 * @param vsName - coding scheme representation of value set name
	 * @param entity - full entity representation of a value in a value set
	 */
	@LgAdminFunction
	public void addEntityToIndex(String codingSchemeUri, String codingSchemeVersion,
			String vsURI, String vsName, Entity entity);
	
	/**
	 * Deletes an entity to the index targeted by source coding scheme and defined
	 * further by the value set URI
	 * 
	 * @param codingSchemeUri - source asserted value set source system uri
	 * @param codingSchemeVersion - source asserted value set source system version
	 * @param vsURI - coding scheme representation of value set URI
	 * @param vsName - coding scheme representation of value set name
	 * @param entity - full entity representation of a value in a value set
	 */
	@LgAdminFunction
	public void deleteEntityFromIndex(String codingSchemeUri, String codingSchemeVersion, 
			String vsURI, String vsName, Entity entity);
	

}