/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.ibatis.picklist.parameter;

import org.LexGrid.valueSets.PickListDefinition;
import org.lexevs.dao.database.ibatis.parameter.IdableParameterBean;

/**
 * The Class InsertPickListDefinitionBean.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class InsertPickListDefinitionBean extends IdableParameterBean {

	/** The pick list definition. */
	private PickListDefinition pickListDefinition;
	
	/** The system release id. */
	private String systemReleaseId;

	/**
	 * Sets the pick list definition.
	 * 
	 * @param pickListDefinition the new pick list definition
	 */
	public void setPickListDefinition(PickListDefinition pickListDefinition) {
		this.pickListDefinition = pickListDefinition;
	}

	/**
	 * Gets the pick list definition.
	 * 
	 * @return the pick list definition
	 */
	public PickListDefinition getPickListDefinition() {
		return pickListDefinition;
	}

	/**
	 * Sets the system release id.
	 * 
	 * @param systemReleaseId the new system release id
	 */
	public void setSystemReleaseId(String systemReleaseId) {
		this.systemReleaseId = systemReleaseId;
	}

	/**
	 * Gets the system release id.
	 * 
	 * @return the system release id
	 */
	public String getSystemReleaseId() {
		return systemReleaseId;
	}
}

