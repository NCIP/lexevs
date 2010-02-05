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
package org.lexgrid.loader.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.persistence.model.EntityProperty;
import org.LexGrid.persistence.model.EntityPropertyId;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

/**
 * The Class EntityPropertyRowMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyRowMapper implements ParameterizedRowMapper<EntityProperty> {

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.simple.ParameterizedRowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public EntityProperty mapRow(ResultSet arg0, int arg1) throws SQLException {
		int index = 1;

		EntityProperty prop = new EntityProperty();
		EntityPropertyId propId = new EntityPropertyId();

		propId.setCodingSchemeName(arg0.getString(index++));
		propId.setEntityCodeNamespace(arg0.getString(index++));
		propId.setEntityCode(arg0.getString(index++));
		propId.setPropertyId(arg0.getString(index++));
		prop.setPropertyType(arg0.getString(index++));
		prop.setPropertyName(arg0.getString(index++));
		prop.setLanguage(arg0.getString(index++));
		prop.setFormat(arg0.getString(index++));	
		prop.setIsPreferred(arg0.getBoolean(index++));	
		prop.setDegreeOfFidelity(arg0.getString(index++));
		prop.setMatchIfNoContext(arg0.getBoolean(index++));	
		prop.setRepresentationalForm(arg0.getString(index++));		
		prop.setIsActive(arg0.getBoolean(index++));
		prop.setEntryStateId(arg0.getLong(index++));
		prop.setPropertyValue(arg0.getString(index++));	
		prop.setId(propId);
		return prop;
	}
}
