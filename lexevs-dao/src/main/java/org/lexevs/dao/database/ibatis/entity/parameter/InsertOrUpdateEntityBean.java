
package org.lexevs.dao.database.ibatis.entity.parameter;

import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertEntityBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertOrUpdateEntityBean extends IdableParameterBean {

	/** The entity. */
	private Entity entity;
	
	/** The coding scheme uid. */
	private String codingSchemeUId;
	
	private String entityTypeTablePrefix;
	
	private String forwardName = null;
	
	private String reverseName = null;
	
	private Boolean isNavigable = null;
	
	private Boolean isTransitive = null;
	
	/**
	 * Gets the entity.
	 * 
	 * @return the entity
	 */
	public Entity getEntity() {
		return entity;
	}
	
	/**
	 * Sets the entity.
	 * 
	 * @param entity the new entity
	 */
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	/**
	 * Gets the coding scheme id.
	 * 
	 * @return the coding scheme id
	 */
	public String getCodingSchemeUId() {
		return codingSchemeUId;
	}
	
	/**
	 * Sets the coding scheme id.
	 * 
	 * @param codingSchemeId the new coding scheme id
	 */
	public void setCodingSchemeUId(String codingSchemeUId) {
		this.codingSchemeUId = codingSchemeUId;
	}

	public void setEntityTypeTablePrefix(String entityTypeTablePrefix) {
		this.entityTypeTablePrefix = entityTypeTablePrefix;
	}

	public String getEntityTypeTablePrefix() {
		return entityTypeTablePrefix;
	}

	/**
	 * @return the forwardName
	 */
	public String getForwardName() {
		return forwardName;
	}

	/**
	 * @param forwardName the forwardName to set
	 */
	public void setForwardName(String forwardName) {
		this.forwardName = forwardName;
	}

	/**
	 * @return the reverseName
	 */
	public String getReverseName() {
		return reverseName;
	}

	/**
	 * @param reverseName the reverseName to set
	 */
	public void setReverseName(String reverseName) {
		this.reverseName = reverseName;
	}

	/**
	 * @return the isNavigable
	 */
	public Boolean getIsNavigable() {
		return isNavigable;
	}

	/**
	 * @param isNavigable the isNavigable to set
	 */
	public void setIsNavigable(Boolean isNavigable) {
		this.isNavigable = isNavigable;
	}

	/**
	 * @return the isTransitive
	 */
	public Boolean getIsTransitive() {
		return isTransitive;
	}

	/**
	 * @param isTransitive the isTransitive to set
	 */
	public void setIsTransitive(Boolean isTransitive) {
		this.isTransitive = isTransitive;
	}
}