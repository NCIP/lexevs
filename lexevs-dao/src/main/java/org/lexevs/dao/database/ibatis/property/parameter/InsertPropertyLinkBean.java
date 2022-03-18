
package org.lexevs.dao.database.ibatis.property.parameter;

import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertPropertyLinkBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertPropertyLinkBean extends IdableParameterBean {

	/** The entity id. */
	private String entityUId;
	
	/** The link. */
	private String link;
	
	/** The source property id. */
	private String sourcePropertyUId;
	
	/** The target property id. */
	private String targetPropertyUId;
	
	/**
	 * Gets the entity id.
	 * 
	 * @return the entity id
	 */
	public String getEntityUId() {
		return entityUId;
	}
	
	/**
	 * Sets the entity id.
	 * 
	 * @param entityUId the new entity id
	 */
	public void setEntityUId(String entityUId) {
		this.entityUId = entityUId;
	}
	
	/**
	 * Gets the link.
	 * 
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	
	/**
	 * Sets the link.
	 * 
	 * @param link the new link
	 */
	public void setLink(String link) {
		this.link = link;
	}
	
	/**
	 * Gets the source property id.
	 * 
	 * @return the source property id
	 */
	public String getSourcePropertyUId() {
		return sourcePropertyUId;
	}
	
	/**
	 * Sets the source property id.
	 * 
	 * @param sourcePropertyUId the new source property id
	 */
	public void setSourcePropertyUId(String sourcePropertyUId) {
		this.sourcePropertyUId = sourcePropertyUId;
	}
	
	/**
	 * Gets the target property id.
	 * 
	 * @return the target property id
	 */
	public String getTargetPropertyUId() {
		return targetPropertyUId;
	}
	
	/**
	 * Sets the target property id.
	 * 
	 * @param targetPropertyUId the new target property id
	 */
	public void setTargetPropertyUId(String targetPropertyUId) {
		this.targetPropertyUId = targetPropertyUId;
	}
}