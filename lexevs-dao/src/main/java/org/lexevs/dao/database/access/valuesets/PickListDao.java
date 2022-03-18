
package org.lexevs.dao.database.access.valuesets;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBRevisionException;
import org.LexGrid.naming.Mappings;
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
	public String getPickListGuidFromPickListId(String pickListId);
	
	
	/**
	 * Returns the plEntryGUID for pickListId and pickListEntryId.
	 * 
	 * @param pickListId
	 * @param plEntryId
	 * @return the plEntryGuid
	 */
	public String getPickListEntryNodeGuidByPickListIdAndPLEntryId(String pickListId, String plEntryId);
	
	/**
	 * 
	 * @param entityCode
	 * @param entityCodeNameSpace
	 * @param propertyId
	 * @param extractPickListName
	 * @return
	 * @throws LBException
	 */
	public List<String> getPickListDefinitionIdForEntityReference(String entityCode, String entityCodeNameSpace, String propertyId);
	
	/**
	 * Returns all the pickListIds that contain supplied supported tag and value.
	 * 
	 * @param supportedTag like SupportedCodingScheme, SupportedAssociation etc.
	 * @param value value to look for
	 * @return list of pickListIds that contains supportedTag with value.
	 */
	public List<String> getPickListDefinitionIdForSupportedTagAndValue(String supportedTag, String value);
	
	/**
	 * Insert pick list definition.
	 * 
	 * @param systemReleaseUri the system release uri
	 * @param definition the definition
	 * 
	 * @return the string
	 */
	public String insertPickListDefinition(PickListDefinition definition, String systemReleaseUri, Mappings mappings);

	/**
	 * Gets the pick list ids.
	 * 
	 * @return the pick list ids
	 */
	public List<String> getPickListIds();
	
	/**
	 * Delete pick list definition by pick list id.
	 * 
	 * @param pickListDefinitionId the pick list definition id
	 */
	public void removePickListDefinitionByPickListId(String pickListDefinitionId);

	public String insertHistoryPickListDefinition(String pickListDefUId, String pickListId);

	public String updatePickListDefinition(String pickListDefUId,
			PickListDefinition definition);

	public String updateVersionableAttributes(String pickListDefUId, PickListDefinition definition);

	public String getPickListEntryStateUId(String pickListDefUId);

	public void updateEntryStateUId(String pickListDefUId, String entryStateUId);

	public String getLatestRevision(String pickListDefUId);

	public boolean entryStateExists(String entryStateUId);
	
	public PickListDefinition resolvePickListByRevision(String pickListId,
			String revisionId, Integer sortType) throws LBRevisionException;

}