
package org.lexevs.dao.database.access.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.PropertyLink;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;

/**
 * The Interface PropertyDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyDao extends LexGridSchemaVersionAwareDao {
	
	/**
	 * The Enum PropertyType.
	 * 
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	public enum PropertyType {
		/** The CODINGSCHEME. */
		CODINGSCHEME,
		/** The VALUEDOMAIN. */
		VALUEDOMAIN,
		/** The ENTITY. */
		ENTITY,
		/** The RELATION*/
		RELATION}
	
	/**
	 * Gets the all properties of parent.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param parentId the parent id
	 * @param type the type
	 * 
	 * @return the all properties of parent
	 */
	public List<Property> getAllPropertiesOfParent(String codingSchemeId,
			String parentId, PropertyType type);
	
	public List<String> getAllHistoryPropertyUidsOfParentByRevisionId(String codingSchemeId,
			String parentId, String revisionId);
	
	public Property getHistoryPropertyByRevisionId(
			String codingSchemeId,
			String propertyUid, 
			String revisionId);

	public List<Property> getPropertiesOfParents(String codingSchemeId, List<String> parentUids);

	public List<Property> getPropertiesOfParents(
			String codingSchemeId, 
			List<String> propertyNames, 
			List<String> propertyTypes,
			List<String> parentUids);
	
	public Property getPropertyByUid(
			String codingSchemeId, 
			String propertyUid);
	/**
	 * Insert property qualifier.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyId the property id
	 * @param qualifier the qualifier
	 */
	public void insertPropertyQualifier(
			String codingSchemeId, 
			String propertyId,
			PropertyQualifier qualifier);
	
	/**
	 * Insert property.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param parentUId the parent id
	 * @param type the type
	 * @param property the property
	 * 
	 * @return the string
	 */
	public String insertProperty(
			String codingSchemeUId, 
			String parentUId,
			PropertyType type,
			Property property);
	
	/**
	 * insert property data into history.
	 * 
	 * @param codingSchemeUId
	 * @param propertyUId
	 * @param type
	 * @param property
	 * @return
	 */
	public String insertHistoryProperty(String codingSchemeUId,
			String propertyUId, Property property);
	
	/**
	 * Insert property source.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyId the property id
	 * @param source the source
	 */
	public void insertPropertySource(String codingSchemeId, String propertyId, Source source);
	
	/**
	 * Insert property usage context.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param propertyId the property id
	 * @param usageContext the usage context
	 */
	public void insertPropertyUsageContext(String codingSchemeId, String propertyId, String usageContext);

	/**
	 * Delete all coding scheme properties of coding scheme.
	 * 
	 * @param codingSchemeUId the coding scheme id.
	 */
	public void deleteAllCodingSchemePropertiesOfCodingScheme(
			String codingSchemeUId);
	
	/**
	 * Delete all entity properties of coding scheme.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 */
	public void deleteAllEntityPropertiesOfCodingScheme(
			String codingSchemeUId);
	
	/**
	 * Delete all relation properties of coding scheme.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 */
	public void deleteAllRelationPropertiesOfCodingScheme(
			String codingSchemeUId);
	
	/**
	 * Delete all entity properties of coding scheme.
	 * 
	 * @param codingSchemeUId the coding scheme uid.
	 * @param parentUId the parent uid.
	 * @param parentType the parent type
	 */
	public void deleteAllPropertiesOfParent(
			String codingSchemeUId, String parentUId, PropertyType parentType);
	
	/**
	 * Insert batch properties.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * @param type the type
	 * @param batch the batch
	 */
	public void insertBatchProperties(
			String codingSchemeId, 
			PropertyType type,
			List<PropertyBatchInsertItem> batch);
	
	/**
	 * Insert property link.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param propertyId the property id
	 * @param propertyLink the property link
	 */
	public void insertPropertyLink(
			String codingSchemeUId, 
			String parentUId,
			PropertyLink propertyLink);
	
	
	/**
	 * Update property.
	 * 
	 * @param codingSchemeUId the coding scheme id
	 * @param parentUId the parent id
	 * @param propertyUId the property id
	 * @param type the type
	 * @param property the property
	 */
	public String updateProperty(
			String codingSchemeUId, 
			String parentUId,
			String propertyUId,
			PropertyType type,
			Property property);

	public String updatePropertyVersionableAttrib(
			String codingSchemeUId,
			String propertyUId, 
			Property property);
	
	public boolean entryStateExists(String codingSchemeUId, String entryStateUId);
	
	public String getPropertyUIdByPropertyIdAndName(String codingSchemeUId, String referenceUId, String propertyId,
			String propertyName);

	public void removePropertyByUId(String codingSchemeUId, String propertyUId);

	public String getLatestRevision(String csUId, String propertyUId);
	
	public String getEntryStateUId(String codingSchemeUId, String propertyUId);
}