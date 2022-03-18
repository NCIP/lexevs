
package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.Extensions.tree.dao.LexEvsTreeDao.Direction;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandlerFactory;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.healthmarketscience.sqlbuilder.BinaryCondition;
import com.healthmarketscience.sqlbuilder.ComboCondition;
import com.healthmarketscience.sqlbuilder.Condition;
import com.healthmarketscience.sqlbuilder.FunctionCall;
import com.healthmarketscience.sqlbuilder.SelectQuery;
import com.healthmarketscience.sqlbuilder.UnaryCondition;
import com.healthmarketscience.sqlbuilder.SelectQuery.JoinType;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbColumn;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSpec;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

/**
 * The Class GetChildrenSqlBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class GetChildrenSqlBuilder extends AbstractSqlBuilder {

	/** The logger. */
	private static Logger logger = LogManager.getLogger(GetChildrenSqlBuilder.class);
	
	/** The exclude anonymous. */
	private boolean excludeAnonymous = true;
	
	/** The table columns. */
	private String[] tableColumns = new String[]{
	    		"associationPredicateGuid",
	    		"sourceEntityCode",
	    		"targetEntityCode"};

	public String buildSql(String codingSchemeName, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace,
			Direction direction,
			List<String> associationNames,
			List<String> knownCodes,
			int start, 
			int end,
			boolean countOnly) {
		String sql = this.doBuildSql(
				codingSchemeName, 
				versionOrTag, 
				code, 
				namespace, 
				direction, 
				associationNames, 
				knownCodes, 
				start, 
				end, 
				countOnly);

		return sql;
	}
	
	protected String doBuildSql(String codingSchemeName, 
			CodingSchemeVersionOrTag versionOrTag, 
			String code,
			String namespace,
			Direction direction,
			List<String> associationNames,
			List<String> knownCodes,
			int start, 
			int end,
			boolean countOnly) {
			AbsoluteCodingSchemeVersionReference adjustedRef;
			try {
				AbsoluteCodingSchemeVersionReference ref = 
					ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingSchemeName, versionOrTag, true);
	
				
				adjustedRef =  NamespaceHandlerFactory.getNamespaceHandler().
				getCodingSchemeForNamespace(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion(), 
						namespace);
				
			} catch (LBParameterException e) {
				throw new RuntimeException(e);
			}

			codingSchemeName = adjustedRef.getCodingSchemeURN();
			versionOrTag = Constructors.createCodingSchemeVersionOrTagFromVersion(adjustedRef.getCodingSchemeVersion());
			
		 	DbSpec spec = new DbSpec();
		    DbSchema schema = spec.addDefaultSchema();

		    SelectQuery query = new SelectQuery();

		    DbTable entityAssnsToEntityTable = createTable(schema, codingSchemeName, versionOrTag, "entityAssnsToEntity", tableColumns);
		    DbTable entityTable = createTable(schema, codingSchemeName, versionOrTag, "entity", "entityCode", "description", "entityCodeNamespace", "isAnonymous");
		    DbTable associationPredicateTable = createTable(schema, codingSchemeName, versionOrTag, "associationPredicate", "associationPredicateGuid", "relationGuid", "associationName");
		    DbTable relationTable = createTable(schema, codingSchemeName, versionOrTag, "relation", "codingSchemeGuid", "relationGuid");
		    if(countOnly){
		    	query.addCustomColumns(FunctionCall.countAll());
		    } else {

		    	query.addAliasedColumn(entityAssnsToEntityTable.findColumn(
		    			this.getTargetColumnName(direction)), "childEntityCode");
		    	query.addAliasedColumn(entityTable.findColumn(
		    	"description"), "description");
		    	query.addAliasedColumn(entityTable.findColumn(
		    	"entityCodeNamespace"), "entityCodeNamespace");
		    }

		    query.addJoin(
		    		JoinType.LEFT_OUTER, 
		    		entityAssnsToEntityTable, 
		    		entityTable, 
		    		entityAssnsToEntityTable.findColumn(this.getTargetColumnName(direction)), 
		    		entityTable.findColumn("entityCode"));

		    query.addCondition(BinaryCondition.equalTo(entityAssnsToEntityTable.findColumn(
		    		this.getSourceColumnName(direction)), code));
		    
		    query.addJoin(
		    		JoinType.INNER, 
		    		entityAssnsToEntityTable, 
		    		associationPredicateTable, 
		    		entityAssnsToEntityTable.findColumn("associationPredicateGuid"),
		    		associationPredicateTable.findColumn("associationPredicateGuid"));
		    
		    query.addJoin(
		    		JoinType.INNER, 
		    		associationPredicateTable, 
		    		relationTable, 
		    		associationPredicateTable.findColumn("relationGuid"),
		    		relationTable.findColumn("relationGuid"));

		    query.addCondition(addAssociationWhereClause(associationNames, associationPredicateTable));
		    
		    if(excludeAnonymous){
		    	query.addCondition(buildAnonymousCodeExclusion(entityTable.findColumn("isAnonymous")));
		    }
		    
		    if(knownCodes != null && knownCodes.size() > 0){
		    	query.addCondition(buildKnownCodesCondition(knownCodes, 
		    			entityAssnsToEntityTable.findColumn(
			    		this.getTargetColumnName(direction))));
		    }

		    String sql;
		    if(!countOnly){
		    	sql = addLimitClause(query.toString(), start, end);
			} else {
				sql = query.toString();
			}

		    logger.debug(sql);

		    return sql;
	}

	/**
	 * Adds the limit clause.
	 * 
	 * @param sql the sql
	 * @param start the start
	 * @param end the end
	 * 
	 * @return the string
	 */
	protected String addLimitClause(String sql, int start, int end){
		StringBuffer sb = new StringBuffer(sql);

		if(end == -1){
			end = Integer.MAX_VALUE;
		}
		
		sb.append(" LIMIT " + end + " OFFSET " + start);

		return sb.toString();
	}
	
	/**
	 * Gets the target column name.
	 * 
	 * @param direction the direction
	 * 
	 * @return the target column name
	 */
	protected String getTargetColumnName(Direction direction){
		String joinColumn = getSourceColumnName(direction);
		if(joinColumn.equals("sourceEntityCode")){
			return "targetEntityCode";
		}
		else {
			return "sourceEntityCode";
		}	
	}

	/**
	 * Gets the source column name.
	 * 
	 * @param direction the direction
	 * 
	 * @return the source column name
	 */
	protected String getSourceColumnName(Direction direction){
    		if(direction.equals(Direction.FORWARD)){
    			return "sourceEntityCode";
    		} else {
    			return "targetEntityCode";
    		}
	}
	
	/**
	 * Builds the anonymous code exclusion.
	 * 
	 * @param column the column
	 * 
	 * @return the condition
	 */
	protected Condition buildAnonymousCodeExclusion(DbColumn column){
		Condition c1 = BinaryCondition.notEqualTo(column, true);
		Condition c2 = UnaryCondition.isNull(column);
		
		return ComboCondition.or(c1, c2);
	}
	
	/**
	 * Builds the known codes condition.
	 * 
	 * @param knownCodes the known codes
	 * @param column the column
	 * 
	 * @return the condition
	 */
	protected Condition buildKnownCodesCondition(List<String> knownCodes, DbColumn column){
		Condition[] conditions = new Condition[knownCodes.size()];
		
		for(int i=0;i<knownCodes.size();i++){
			conditions[i] = BinaryCondition.notEqualTo(column, knownCodes.get(i));
		}
		
		return ComboCondition.and(conditions);
	}
	
	/**
	 * Adds the association where clause.
	 * 
	 * @param associations the associations
	 * @param table the table
	 * 
	 * @return the condition
	 */
	protected Condition addAssociationWhereClause(List<String> associations, DbTable table){
		Condition[] conditions = new Condition[associations.size()];
		
		for(int i=0;i<associations.size();i++){
			conditions[i] = BinaryCondition.equalTo(table.findColumn("associationName"), associations.get(i));
		}
		
		return ComboCondition.or(conditions);
	}
	
	/**
	 * Checks if is exclude anonymous.
	 * 
	 * @return true, if is exclude anonymous
	 */
	public boolean isExcludeAnonymous() {
		return excludeAnonymous;
	}

	/**
	 * Sets the exclude anonymous.
	 * 
	 * @param excludeAnonymous the new exclude anonymous
	 */
	public void setExcludeAnonymous(boolean excludeAnonymous) {
		this.excludeAnonymous = excludeAnonymous;
	}
}