
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.apache.log4j.Logger;

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
public class GetNodeSqlBuilder extends AbstractSqlBuilder {

	private static final long serialVersionUID = 1L;
	/** The logger. */
	private static Logger logger = Logger.getLogger(GetChildrenSqlBuilder.class);

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