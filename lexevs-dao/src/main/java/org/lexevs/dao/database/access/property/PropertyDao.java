package org.lexevs.dao.database.access.property;

import java.util.List;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.PropertyLink;
import org.lexevs.dao.database.access.LexGridSchemaVersionAwareDao;
import org.lexevs.dao.database.access.property.batch.PropertyBatchInsertItem;

public interface PropertyDao extends LexGridSchemaVersionAwareDao {
	
	public enum PropertyType {CODINGSCHEME,VALUEDOMAIN,ENTITY}

	public String insertProperty(
			String codingSchemeName, 
			String version, 
			String parentId,
			PropertyType type,
			Property property);
	
	
	public void insertPropertyQualifier(
			String codingSchemeName, 
			String version, 
			String propertyId,
			PropertyQualifier qualifier);
	
	public String insertProperty(
			String codingSchemeId, 
			String parentId,
			PropertyType type,
			Property property);
	
	public void insertBatchProperties(
			String codingSchemeId, 
			PropertyType type,
			List<PropertyBatchInsertItem> batch);
	
	public void insertPropertyLink(
			String codingSchemeId, 
			String propertyId,
			PropertyLink propertyLink);
	
	
	public void updateProperty(
			String codingSchemeName, 
			String parentId,
			String propertyId,
			PropertyType type,
			Property property);
	
}

