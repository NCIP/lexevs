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
package org.lexevs.dao.database.access.picklist;

import java.util.List;

import org.LexGrid.valueSets.PickListDefinition;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;

/**
 * The Interface PickListDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PickListDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * Gets the pick list definition by id.
	 * 
	 * @param pickListId the pick list id
	 * 
	 * @return the pick list definition by id
	 */
	public PickListDefinition getPickListDefinitionById(String pickListId);
	
	/**
	 * Gets the list of pick list definitions that are derived by supplied value set definition URI.
	 * 
	 * @param valueSet the uri of value set definition
	 * @return the pick list id
	 */
	public List<String> getPickListDefinitionIdForValueSetDefinitionURI(String valueSetDefURI);
	
	/**
	 * Gets the guid from pick list id.
	 * 
	 * @param pickListId the pick list id
	 * 
	 * @return the guid from pick list id
	 */
	public String getGuidFromPickListId(String pickListId);
	
	/**
	 * Insert pick list definition.
	 * 
	 * @param systemReleaseUri the system release uri
	 * @param definition the definition
	 * 
	 * @return the string
	 */
	public String insertPickListDefinition(String systemReleaseUri, PickListDefinition definition);

	/**
	 * Gets the pick list ids.
	 * 
	 * @return the pick list ids
	 */
	public List<String> getPickListIds();
	
	/**
	 * Insert pick list entry.
	 * 
	 * @param pickListGuid the pick list guid
	 * @param definition the definition
	 * 
	 * @return the string
	 */
	public String insertPickListEntry(String pickListGuid, PickListDefinition definition);
	
	/**
	 * Delete pick list definition by pick list id.
	 * 
	 * @param pickListDefinitionId the pick list definition id
	 */
	public void removePickListDefinitionByPickListId(String pickListDefinitionId);
}
