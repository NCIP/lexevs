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
package org.lexevs.dao.database.ibatis.valuesets.parameter;

import java.util.List;

import org.LexGrid.valueSets.PickListEntryNode;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class to handle insert or update PickListEntry table.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class InsertOrUpdatePickListEntryBean extends IdableParameterBean {

	/** The pick list entry node. */
	private PickListEntryNode pickListEntryNode;
	
	/** The pickListGuid. */
	private String pickListUId;
	
	private String entityCodeNamespace;
	
	private String entityCode;
	
	private Long entryOrder;
	
	private Boolean isDefault;
	
	private Boolean matchIfNoContext;
	
	private String propertyId;
	
	private String langauage;
	
	private Boolean include;
	
	private String pickText;
	
	private List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList = null;
	
	/**
	 * @return the pickListEntryNode
	 */
	public PickListEntryNode getPickListEntryNode() {
		return pickListEntryNode;
	}

	/**
	 * @param pickListEntryNode the pickListEntryNode to set
	 */
	public void setPickListEntryNode(PickListEntryNode pickListEntryNode) {
		this.pickListEntryNode = pickListEntryNode;
	}

	/**
	 * @return the pickListGuid
	 */
	public String getPickListUId() {
		return pickListUId;
	}

	/**
	 * @param pickListUId the pickListGuid to set
	 */
	public void setPickListUId(String pickListUId) {
		this.pickListUId = pickListUId;
	}

	/**
	 * @return the entityCodeNamespace
	 */
	public String getEntityCodeNamespace() {
		return entityCodeNamespace;
	}

	/**
	 * @param entityCodeNamespace the entityCodeNamespace to set
	 */
	public void setEntityCodeNamespace(String entityCodeNamespace) {
		this.entityCodeNamespace = entityCodeNamespace;
	}

	/**
	 * @return the entityCode
	 */
	public String getEntityCode() {
		return entityCode;
	}

	/**
	 * @param entityCode the entityCode to set
	 */
	public void setEntityCode(String entityCode) {
		this.entityCode = entityCode;
	}

	/**
	 * @return the entryOrder
	 */
	public Long getEntryOrder() {
		return entryOrder;
	}

	/**
	 * @param entryOrder the entryOrder to set
	 */
	public void setEntryOrder(Long entryOrder) {
		this.entryOrder = entryOrder;
	}

	/**
	 * @return the isDefault
	 */
	public Boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault the isDefault to set
	 */
	public void setDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * @return the matchIfNoContext
	 */
	public Boolean isMatchIfNoContext() {
		return matchIfNoContext;
	}

	/**
	 * @param matchIfNoContext the matchIfNoContext to set
	 */
	public void setMatchIfNoContext(Boolean matchIfNoContext) {
		this.matchIfNoContext = matchIfNoContext;
	}

	/**
	 * @return the propertyId
	 */
	public String getPropertyId() {
		return propertyId;
	}

	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	/**
	 * @return the include
	 */
	public Boolean isInclude() {
		return include;
	}

	/**
	 * @param include the include to set
	 */
	public void setInclude(Boolean include) {
		this.include = include;
	}

	/**
	 * @return the pickText
	 */
	public String getPickText() {
		return pickText;
	}

	/**
	 * @param pickText the pickText to set
	 */
	public void setPickText(String pickText) {
		this.pickText = pickText;
	}

	/**
	 * @return the langauage
	 */
	public String getLangauage() {
		return langauage;
	}

	/**
	 * @param langauage the langauage to set
	 */
	public void setLangauage(String langauage) {
		this.langauage = langauage;
	}

	/**
	 * @return the vsMultiAttribList
	 */
	public List<InsertOrUpdateValueSetsMultiAttribBean> getVsMultiAttribList() {
		return vsMultiAttribList;
	}

	/**
	 * @param vsMultiAttribList the vsMultiAttribList to set
	 */
	public void setVsMultiAttribList(
			List<InsertOrUpdateValueSetsMultiAttribBean> vsMultiAttribList) {
		this.vsMultiAttribList = vsMultiAttribList;
	}

	
}