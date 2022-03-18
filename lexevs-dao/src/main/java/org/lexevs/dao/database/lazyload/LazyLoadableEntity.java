
package org.lexevs.dao.database.lazyload;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Definition;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.relations.AssociationEntity;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;

/**
 * The Class LazyLoadableEntity.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LazyLoadableEntity extends AssociationEntity {
	
	/** The property dao. */
	private PropertyDao propertyDao;
	
	/** The is hydrated. */
	private boolean isHydrated = false;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -3893677635627151845L;
	
	/** The entity id. */
	private String entityId;
	
	/** The coding scheme id. */
	private String codingSchemeId;
	
	
	/* (non-Javadoc)
	 * @see org.LexGrid.concepts.Entity#getPresentation()
	 */
	@Override
	public Presentation[] getPresentation() {
		this.hydrate();
		return super.getPresentation();
	}
	
	@Override
	public Definition[] getDefinition() {
		this.hydrate();
		return super.getDefinition();
	}
	
	@Override
	public int getDefinitionCount() {
		this.hydrate();
		return super.getDefinitionCount();
	}

	/**
	 * Hydrate.
	 */
	protected void hydrate() {
		if(!isHydrated) {
			doHydrate();
			isHydrated = true;
		}
	}
	
	/**
	 * Do hydrate.
	 */
	protected void doHydrate() {
		List<Property> properties = propertyDao.executeInTransaction(new IndividualDaoCallback<List<Property>>() {

			public List<Property> execute() {
				return propertyDao.getAllPropertiesOfParent(codingSchemeId, entityId, PropertyType.ENTITY);
			}
		
		});
		this.addAnyProperties(properties);
	}
	
	/**
	 * Gets the entity id.
	 * 
	 * @return the entity id
	 */
	public String getEntityId() {
		return entityId;
	}

	/**
	 * Sets the entity id.
	 * 
	 * @param entityId the new entity id
	 */
	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeId() {
		return codingSchemeId;
	}

	/**
	 * Gets the property dao.
	 * 
	 * @return the property dao
	 */
	public PropertyDao getPropertyDao() {
		return propertyDao;
	}

	/**
	 * Sets the property dao.
	 * 
	 * @param propertyDao the new property dao
	 */
	public void setPropertyDao(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	
}