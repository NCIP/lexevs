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

import org.LexGrid.persistence.model.CodingScheme;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * The Class CodingSchemeFieldSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeFieldSetter implements ItemPreparedStatementSetter<CodingScheme>{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
	public void setValues(CodingScheme codingScheme, PreparedStatement ps)
			throws SQLException {
		ps.setString(1, codingScheme.getCodingSchemeName());
		ps.setString(2, codingScheme.getCodingSchemeUri());
		ps.setString(3, codingScheme.getRepresentsVersion());
		ps.setString(4, codingScheme.getFormalName());
		ps.setString(5, codingScheme.getDefaultLanguage());
		
		if(codingScheme.getApproxNumConcepts() == null){
			ps.setObject(6, Types.NULL);
		} else {
			ps.setObject(6, codingScheme.getApproxNumConcepts());
		}
		
		if(codingScheme.getIsActive() == null){
			ps.setObject(7, Types.NULL);
		} else {
			ps.setObject(7, codingScheme.getIsActive());
		}
		
		if(codingScheme.getEntryStateId() == null){
			ps.setObject(8, Types.NULL);
		} else {		
			ps.setObject(8, codingScheme.getEntryStateId());
		}
		
		ps.setString(9, codingScheme.getReleaseUri());
		ps.setString(10, codingScheme.getEntityDescription());
		ps.setString(11, codingScheme.getCopyright());
	}
}