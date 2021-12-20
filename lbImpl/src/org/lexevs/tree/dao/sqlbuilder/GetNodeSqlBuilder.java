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
package org.lexevs.tree.dao.sqlbuilder;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

/**
 * The Class GetNodeSqlBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public class GetNodeSqlBuilder extends AbstractSqlBuilder {

	private static final long serialVersionUID = 1L;
	/** The logger. */
	private static Logger logger = LogManager.getLogger(GetChildrenSqlBuilder.class);

	/**
	 * Builds the sql.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param versionOrTag the version or tag
	 * @param code the code
	 * @param namespace the namespace
	 * 
	 * @return the string
	 */
	public String buildSql(String codingSchemeName, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace) {
		 	DbSpec spec = new DbSpec();
		    DbSchema schema = spec.addDefaultSchema();

		    SelectQuery query = new SelectQuery();

		    DbTable entityTable = createTable(schema, codingSchemeName, versionOrTag, "entity", "codingSchemeGuid", "entityGuid", "entityCode", "description", "entityCodeNamespace");
		   
		    query.addAllTableColumns(entityTable);
		    
		    query.addCondition(BinaryCondition.equalTo(entityTable.findColumn("entityCode"), BinaryCondition.QUESTION_MARK));
		    
		    Condition codingScheme = BinaryCondition.equalTo(entityTable.findColumn("codingSchemeGuid"), 
		    		this.getCodingSchemeUid(codingSchemeName, versionOrTag, namespace));

			query.addCondition(codingScheme);
			
			if(namespace != null){
			Condition namespaceCondition = BinaryCondition.equalTo(entityTable.findColumn("entityCodeNamespace"), BinaryCondition.QUESTION_MARK);
		    query.addCondition(namespaceCondition);
			}
			
		    String sql = query.toString();
		    	
		    logger.debug(sql);

		    return sql;
	}
}
