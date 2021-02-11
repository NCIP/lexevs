
package org.lexevs.dao.database.operation;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer;
import org.lexevs.dao.database.operation.transitivity.TransitivityBuilder.TransitivityTableState;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The Interface PersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsDatabaseOperations {

	public enum RootOrTail {ROOT,TAIL}
	
	public enum TraverseAssociations {TOGETHER,INDIVIDUALLY}
	
	public void createCommonTables();
	
	public void createCodingSchemeTables();
	
	public void createValueSetsTables();
	
	public void createValueSetHistoryTables();
	
	public void createCodingSchemeHistoryTables();
	
	public void createCodingSchemeHistoryTables(String prefix);
	
	public void createCodingSchemeTables(String prefix);
	
	public void createNciHistoryTables();

	public void dropAllTables();
	
	public void createAllTables();
	
	public void dropCommonTables();
	
	public void dropValueSetsTables();
	
	public void dropValueSetHistoryTables();
	
	public void dropCodingSchemeHistoryTables(String codingSchemeUri, String version);
	
	public void dropCodingSchemeTables(String codingSchemeUri, String version);
	
	public void dropCodingSchemeTablesByPrefix(String prefix);
	
	public void dropCodingSchemeHistoryTables();
	
	public void dropCodingSchemeTables();
	
	public void dropNciHistoryTables();
	
	public void dumpSqlScripts(DatabaseType databaseType, String outputPath, String prefix) throws IOException;
	
	public boolean isCodingSchemeLoaded(String codingSchemeUri, String version);
	
	public void reComputeTransitiveTable(String codingSchemeUri,
			String codingSchemeVersion);
	
	public TransitivityTableState isTransitiveTableComputed(String codingSchemeUri,
			String codingSchemeVersion);

	/**
	 * Gets the database utility.
	 * 
	 * @return the database utility
	 */
	public DatabaseUtility getDatabaseUtility();
	
	public PrimaryKeyIncrementer getPrimaryKeyIncrementer();
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource();
	
	/**
	 * Gets the transaction manager.
	 * 
	 * @return the transaction manager
	 */
	public PlatformTransactionManager getTransactionManager();
	
	/**
	 * Gets the prefix resolver.
	 * 
	 * @return the prefix resolver
	 */
	public PrefixResolver getPrefixResolver();
	
	/**
	 * Gets the database type.
	 * 
	 * @return the database type
	 */
	public DatabaseType getDatabaseType();
	
	/**
	 * Compute transitive table.
	 * 
	 * @param codingSchemeName the coding scheme name
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void computeTransitiveTable(String codingSchemeUri, String codingSchemeVersion);
	
	public void addRootRelationNode(String codingSchemeUri, String codingSchemeVersion, 
			List<String> associationNames, String relationContainerName, 
			RootOrTail rootOrTail, TraverseAssociations traverse);

	void dropCodingSchemeHistoryTablesByPrefix(String prefix);

	
}