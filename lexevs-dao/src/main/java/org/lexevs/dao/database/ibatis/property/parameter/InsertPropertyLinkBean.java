/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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