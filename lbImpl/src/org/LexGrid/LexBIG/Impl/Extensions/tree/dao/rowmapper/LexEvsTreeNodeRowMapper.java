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
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.rowmapper;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.springframework.jdbc.core.RowMapper;

/**
 * The Class LexEvsTreeNodeRowMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsTreeNodeRowMapper implements RowMapper, Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -843416909209638045L;

	/* (non-Javadoc)
	 * @see org.springframework.jdbc.core.RowMapper#mapRow(java.sql.ResultSet, int)
	 */
	public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		LexEvsTreeNode node = new LexEvsTreeNode();
		node.setCode(rs.getString("childEntityCode"));
		node.setEntityDescription(rs.getString("description"));
		node.setNamespace(rs.getString("entityCodeNamespace"));
		
		return node;
	}
}
