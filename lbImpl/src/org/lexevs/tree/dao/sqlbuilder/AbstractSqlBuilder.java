
package org.lexevs.tree.dao.sqlbuilder;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.tree.dao.prefixresolver.PrefixResolver;
import org.LexGrid.LexBIG.Impl.namespace.NamespaceHandlerFactory;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.lexevs.dao.database.access.DaoManager;
import org.lexevs.dao.database.service.daocallback.DaoCallbackService.DaoCallback;
import org.lexevs.locator.LexEvsServiceLocator;

import com.healthmarketscience.sqlbuilder.dbspec.basic.DbSchema;
import com.healthmarketscience.sqlbuilder.dbspec.basic.DbTable;

/**
 * The Class AbstractSqlBuilder.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Deprecated
public abstract class AbstractSqlBuilder implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1800198238638721223L;
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;

	/**
	 * Gets the table name.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param versionOrTag the version or tag
	 * @param tableName the table name
	 * 
	 * @return the table name
	 */
	protected String getTableName(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag, String tableName) {
		try {
			return prefixResolver.getPrefix(codingSchemeName, versionOrTag) + tableName;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String getCodingSchemeUid(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag, String namespace) {
		try {
			AbsoluteCodingSchemeVersionReference adjustedRef;
			try {
				AbsoluteCodingSchemeVersionReference ref = 
					ServiceUtility.getAbsoluteCodingSchemeVersionReference(codingSchemeName, versionOrTag, true);
				
				adjustedRef = NamespaceHandlerFactory.getNamespaceHandler().
					getCodingSchemeForNamespace(
						ref.getCodingSchemeURN(), 
						ref.getCodingSchemeVersion(), 
						namespace);
			} catch (LBParameterException e) {
				throw new RuntimeException(e);
			}
			
			final String uri = adjustedRef.getCodingSchemeURN();
			final String version = adjustedRef.getCodingSchemeVersion();
			
			return LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getDaoCallbackService().executeInDaoLayer(new DaoCallback<String>() {

				@Override
				public String execute(DaoManager manager) {
					return manager.getCodingSchemeDao(uri, version).getCodingSchemeUIdByUriAndVersion(uri, version);
				}
				
			});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * Creates the table.
	 * 
	 * @param schema the schema
	 * @param codingSchemeName the coding scheme name
	 * @param versionOrTag the version or tag
	 * @param tableName the table name
	 * @param columns the columns
	 * 
	 * @return the db table
	 */
	protected DbTable createTable(DbSchema schema, String codingSchemeName, 
			CodingSchemeVersionOrTag versionOrTag, String tableName, String... columns){
		  DbTable table = new DbTable(schema, getTableName(codingSchemeName, versionOrTag, tableName));
		   for(String column : columns){
			   table.addColumn(column);
		   }	
		   return table;
	}
	
	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver() {
		return prefixResolver;
	}

	/**
	 * Sets the prefix resolver.
	 * 
	 * @param prefixResolver the new prefix resolver
	 */
	public void setPrefixResolver(PrefixResolver prefixResolver) {
		this.prefixResolver = prefixResolver;
	}
}