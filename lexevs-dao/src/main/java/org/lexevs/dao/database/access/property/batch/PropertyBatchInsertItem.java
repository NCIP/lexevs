
package org.lexevs.dao.database.access.property.batch;

import org.LexGrid.commonTypes.Property;

/**
 * The Class PropertyBatchInsertItem.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PropertyBatchInsertItem {

	/** The parent id. */
	private String parentId;
	
	/** The property. */
	private Property property;
	
	/**
	 * Instantiates a new property batch insert item.
	 */
	public PropertyBatchInsertItem(){
		super();
	}
	
	/**
	 * Instantiates a new property batch insert item.
	 * 
	 * @param parentId the parent id
	 * @param property the property
	 */
	public PropertyBatchInsertItem(String parentId, Property property) {
		super();
		this.parentId = parentId;
		this.property = property;
	}

	/**
	 * Gets the parent id.
	 * 
	 * @return the parent id
	 */
	public String getParentId() {
		return parentId;
	}

	/**
	 * Sets the parent id.
	 * 
	 * @param parentId the new parent id
	 */
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	/**
	 * Gets the property.
	 * 
	 * @return the property
	 */
	public Property getProperty() {
		return property;
	}
	
	/**
	 * Sets the property.
	 * 
	 * @param property the new property
	 */
	public void setProperty(Property property) {
		this.property = property;
	}
}