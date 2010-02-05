package org.lexevs.dao.database.access.property;

import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.PropertyLink;

public interface PropertyDao {
	
	public enum PropertyType {CODINGSCHEME,VALUEDOMAIN,ENTITY}

	public void insertProperty(
			String codingSchemeName, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			PropertyType type,
			Property property);
	
	public String insertProperty(
			String codingSchemeName, 
			String version, 
			String entityCodeId,
			PropertyType type,
			Property property);
	
	public void insertPropertyQualifier(
			String codingSchemeName, 
			String version, 
			String propertyId,
			PropertyQualifier qualifier);
	
	public String insertProperty(
			String codingSchemeId, 
			String entityCodeId,
			PropertyType type,
			Property property);
	
	public void insertPropertyLink(
			String codingSchemeId, 
			String propertyId,
			PropertyLink propertyLink);
	
	
	public void updateProperty(
			String codingSchemeName, 
			String version, 
			String entityCode,
			String entityCodeNamespace,
			String propertyId,
			PropertyType type,
			Property property);
	
}

