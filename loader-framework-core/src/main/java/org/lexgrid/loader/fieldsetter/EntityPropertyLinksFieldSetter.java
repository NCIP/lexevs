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

import org.LexGrid.persistence.model.EntityPropertyLinks;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

/**
 * The Class EntityPropertyLinksFieldSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityPropertyLinksFieldSetter implements ItemPreparedStatementSetter<EntityPropertyLinks>{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.database.ItemPreparedStatementSetter#setValues(java.lang.Object, java.sql.PreparedStatement)
	 */
	public void setValues(EntityPropertyLinks propLink, PreparedStatement ps)
			throws SQLException {
		ps.setString(1, propLink.getId().getCodingSchemeName());
		ps.setString(2, propLink.getId().getEntityCodeNamespace());
		ps.setString(3, propLink.getId().getEntityCode());
		ps.setString(4, propLink.getId().getSourcePropertyId());
		ps.setString(5, propLink.getId().getLink());	
		ps.setString(6, propLink.getId().getTargetPropertyId());	
	}
}