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

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * The Class EntityAssnsToEntityFieldSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityFieldSetter implements ItemPreparedStatementSetter<EntityAssnsToEntity>{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
	public void setValues(EntityAssnsToEntity assoc, PreparedStatement ps)
	throws SQLException {
		if(assoc != null){
			ps.setString(1, assoc.getCodingSchemeName());
			ps.setString(2, assoc.getContainerName());
			ps.setString(3, assoc.getEntityCodeNamespace());
			ps.setString(4, assoc.getEntityCode());
			ps.setString(5, assoc.getSourceEntityCodeNamespace());
			ps.setString(6, assoc.getSourceEntityCode());
			ps.setString(7, assoc.getTargetEntityCodeNamespace());
			ps.setString(8, assoc.getTargetEntityCode());
			ps.setString(9, assoc.getMultiAttributesKey());
			ps.setString(10, assoc.getAssociationInstanceId());
			ps.setBoolean(11, assoc.getIsDefining());
			ps.setBoolean(12, assoc.getIsInferred());
			ps.setBoolean(13, assoc.getIsActive());
			
			if(assoc.getEntryStateId() == null){
				ps.setObject(14, Types.NULL);
			} else {
				ps.setObject(14, assoc.getEntryStateId());
			}
		}
	}
}