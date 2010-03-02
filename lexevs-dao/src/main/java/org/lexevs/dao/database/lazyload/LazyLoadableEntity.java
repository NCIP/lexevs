package org.lexevs.dao.database.lazyload;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao.IndividualDaoCallback;
import org.lexevs.dao.database.access.property.PropertyDao;
import org.lexevs.dao.database.access.property.PropertyDao.PropertyType;

public class LazyLoadableEntity extends Entity {
	
	private PropertyDao propertyDao;
	private boolean isHydrated = false;

	private static final long serialVersionUID = -3893677635627151845L;
	private String entityId;
	private String codingSchemeId;
	
	
	@Override
	public Presentation[] getPresentation() {
		this.hydrate();
		return super.getPresentation();
	}

	protected void hydrate() {
		if(!isHydrated) {
			doHydrate();
			isHydrated = true;
		}
	}
	
	protected void doHydrate() {
		List<Property> properties = propertyDao.executeInTransaction(new IndividualDaoCallback<List<Property>>() {

			public List<Property> execute() {
				return propertyDao.getAllPropertiesOfParent(codingSchemeId, entityId, PropertyType.ENTITY);
			}
		
		});
		this.addAnyProperties(properties);
	}
	
	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getCodingSchemeId() {
		return codingSchemeId;
	}

	public PropertyDao getPropertyDao() {
		return propertyDao;
	}

	public void setPropertyDao(PropertyDao propertyDao) {
		this.propertyDao = propertyDao;
	}

	
}
