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
package org.lexgrid.loader.fieldsetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.LexGrid.persistence.model.EntityProperty;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * The Class EntityPropertyFieldSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyFieldSetter implements ItemPreparedStatementSetter<EntityProperty>{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
	public void setValues(EntityProperty prop, PreparedStatement ps)
			throws SQLException {
		ps.setString(1, prop.getId().getCodingSchemeName());
		ps.setString(2, prop.getId().getEntityCodeNamespace());
		ps.setString(3, prop.getId().getEntityCode());
		ps.setString(4, prop.getId().getPropertyId());
		ps.setString(5, prop.getPropertyType());
		ps.setString(6, prop.getPropertyName());
		ps.setString(7, prop.getLanguage());
		ps.setString(8, prop.getFormat());
		
		if(prop.getIsPreferred() == null){
			ps.setObject(9, Types.NULL);
		} else {
			ps.setObject(9, prop.getIsPreferred());
		}
		
		ps.setString(10, prop.getDegreeOfFidelity());
		
		if(prop.getMatchIfNoContext() == null){
			ps.setObject(11, Types.NULL);
		} else {
			ps.setObject(11, prop.getMatchIfNoContext());
		}
		
		ps.setString(12, prop.getRepresentationalForm());
		
		if(prop.getIsActive() == null){
			ps.setObject(13, Types.NULL);
		} else {
			ps.setObject(13, prop.getIsActive());
		}
		
		if(prop.getEntryStateId() == null){
			ps.setObject(14, Types.NULL);
		} else {
			ps.setObject(14, prop.getEntryStateId());
		}	
		
		ps.setString(15, prop.getPropertyValue());	
	}
}