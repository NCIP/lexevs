
package org.lexevs.dao.database.service.property;

import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.commonTypes.Property;

/**
 * The Interface PropertyService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PropertyService {
	
	/** The Constant INSERT_BATCH_PROPERTY_ERROR. */
	public static final String INSERT_BATCH_PROPERTY_ERROR = "INSERT-BATCH-PROPERTY-ERROR";
	
	/** The Constant INSERT_CODINGSCHEME_PROPERTY_ERROR. */
	public static final String INSERT_CODINGSCHEME_PROPERTY_ERROR = "INSERT-CODING-SCHEME-PROPERTY-ERROR";
	
	/** The Constant INSERT_CODINGSCHEME_PROPERTY_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_CODINGSCHEME_PROPERTY_VERSIONABLE_CHANGES_ERROR = "INSERT-CODING-SCHEME-PROPERTY-VERSIONABLE-CHANGES-ERROR";
	
	/** The Constant UPDATE_CODINGSCHEME_PROPERTY_ERROR. */
	public static final String UPDATE_CODINGSCHEME_PROPERTY_ERROR = "UPDATE-CODING-SCHEME-PROPERTY-ERROR";
	
	/** The Constant REMOVE_CODINGSCHEME_PROPERTY_ERROR. */
	public static final String REMOVE_CODINGSCHEME_PROPERTY_ERROR = "REMOVE-CODING-SCHEME-PROPERTY-ERROR";
	
	/** The Constant INSERT_ENTITY_PROPERTY_ERROR. */
	public static final String INSERT_ENTITY_PROPERTY_ERROR = "INSERT-ENTITY-PROPERTY-ERROR";
	
	/** The Constant INSERT_ENTITY_PROPERTY_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_ENTITY_PROPERTY_VERSIONABLE_CHANGES_ERROR = "INSERT-ENTITY-PROPERTY-VERSIONABLE-CHANGES-ERROR";
	
	/** The Constant UPDATE_ENTITY_PROPERTY_ERROR. */
	public static final String UPDATE_ENTITY_PROPERTY_ERROR = "UPDATE-ENTITY-PROPERTY-ERROR";
	
	/** The Constant REMOVE_ENTITY_PROPERTY_ERROR. */
	public static final String REMOVE_ENTITY_PROPERTY_ERROR = "REMOVE-ENTITY-PROPERTY-ERROR";
	
	/** The Constant INSERT_RELATION_PROPERTY_ERROR. */
	public static final String INSERT_RELATION_PROPERTY_ERROR = "INSERT-RELATION-PROPERTY-ERROR";
	
	/** The Constant INSERT_RELATION_PROPERTY_VERSIONABLE_CHANGES_ERROR. */
	public static final String INSERT_RELATION_PROPERTY_VERSIONABLE_CHANGES_ERROR = "INSERT-RELATION-PROPERTY-VERSIONABLE-CHANGES-ERROR";
	
	/** The Constant UPDATE_RELATION_PROPERTY_ERROR. */
	public static final String UPDATE_RELATION_PROPERTY_ERROR = "UPDATE-RELATION-PROPERTY-ERROR";
	
	/** The Constant REMOVE_RELATION_PROPERTY_ERROR. */
	public static final String REMOVE_RELATION_PROPERTY_ERROR = "REMOVE-RELATION-PROPERTY-ERROR";
	
	/**
	 * Insert coding scheme property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param property the coding scheme property.
	 */
	public void insertCodingSchemeProperty(String codingSchemeUri,
			String version, Property property);

	/**
	 * update coding schme property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param property the coding scheme property.
	 */
	public void updateCodingSchemeProperty(String codingSchemeUri,
			String version, Property property);

	/**
	 * remove coding schme property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param property the coding scheme property.
	 */
	public void removeCodingSchemeProperty(String codingSchemeUri,
			String version, Property property);

	/**
	 * Insert entity property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param property the property
	 */
	public void insertEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property);

	/**
	 * Update entity property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param property the property
	 */
	public void updateEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property);

	/**
	 * Remove entity property.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param property the property
	 */
	public void removeEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property);

	/**
	 * Insert batch entity properties.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param batch the batch
	 */
	public void insertBatchEntityProperties(String codingSchemeUri,
			String version, String entityCode, String entityCodeNamespace,
			List<Property> batch);

	/**
	 * Insert relation property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param relationContainerName the relations container name.
	 * @param property the relation property object.
	 */
	public void insertRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property);

	/**
	 * Update relation property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param relationContainerName the relations container name.
	 * @param property the relation property object.
	 */
	public void updateRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property);

	/**
	 * Remove relation property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param relationContainerName the relations container name.
	 * @param property the relation property object.
	 */
	public void removeRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property);

	/**
	 * Revise a coding scheme property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param property the coding scheme property object.
	 * 
	 * @throws LBException the LB exception
	 */
	public void reviseCodingSchemeProperty(String codingSchemeUri,
			String version, Property property) throws LBException;

	/**
	 * Revise a entity property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param property the entity property object.
	 * 
	 * @throws LBException the LB exception
	 */
	public void reviseEntityProperty(String codingSchemeUri, String version,
			String entityCode, String entityCodeNamespace, Property property)
			throws LBException;

	/**
	 * Revise a relations property.
	 * 
	 * @param codingSchemeUri the coding scheme uri.
	 * @param version the coding scheme version.
	 * @param relationContainerName the relations container name.
	 * @param property the relation property object.
	 * 
	 * @throws LBException the LB exception
	 */
	public void reviseRelationProperty(String codingSchemeUri, String version,
			String relationContainerName, Property property) throws LBException;
	
	/**
	 * Resolve properties of coding scheme by revision.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param revisionId the revision id
	 * 
	 * @return the list< property>
	 */
	public List<Property> resolvePropertiesOfCodingSchemeByRevision(
			String codingSchemeURI,
			String version,
			String revisionId);
	
	/**
	 * Resolve properties of entity by revision.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 * @param revisionId the revision id
	 * 
	 * @return the list< property>
	 */
	public List<Property> resolvePropertiesOfEntityByRevision(
			String codingSchemeURI,
			String version, 
			String entityCode, 
			String entityCodeNamespace,
			String revisionId);
	
	/**
	 * Resolve properties of relation by revision.
	 * 
	 * @param codingSchemeURI the coding scheme uri
	 * @param version the version
	 * @param relationsName the relations name
	 * @param revisionId the revision id
	 * 
	 * @return the list< property>
	 */
	public List<Property> resolvePropertiesOfRelationByRevision(
			String codingSchemeURI,
			String version, 
			String relationsName,
			String revisionId);
}