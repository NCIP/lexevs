/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.operation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.apache.ddlutils.platform.CreationParameters;
import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer;
import org.lexevs.dao.database.operation.root.RootBuilder;
import org.lexevs.dao.database.operation.transitivity.TransitivityBuilder;
import org.lexevs.dao.database.operation.transitivity.TransitivityBuilder.TransitivityTableState;
import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.database.utility.DatabaseUtility;
import org.lexevs.dao.index.service.IndexServiceManager;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.xml.sax.InputSource;

/**
 * The Class LexEvsPersistenceConnectionManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultLexEvsDatabaseOperations implements LexEvsDatabaseOperations, DisposableBean {
	
	private interface PlatformActor {
		
		String getSqlFromPlatform(Platform platform, Database database);
	}
	
//	private static CreationParameters MYSQL_CREATION_PARAMETERS = new CreationParameters();
//	{
//		MYSQL_CREATION_PARAMETERS.addParameter(null, "ENGINE", "INNODB");
//		MYSQL_CREATION_PARAMETERS.addParameter(null, "CHARACTER SET", "utf8");
//		MYSQL_CREATION_PARAMETERS.addParameter(null, "COLLATE", systemVariables.getMysql_collation() "utf8_general_ci");
//	}
	
	private class CreateSchemaPlatformActor implements PlatformActor {

		public String getSqlFromPlatform(Platform platform,
			Database database) {
				CreationParameters MYSQL_CREATION_PARAMETERS = new CreationParameters();
				MYSQL_CREATION_PARAMETERS.addParameter(null, "ENGINE", "INNODB");
				MYSQL_CREATION_PARAMETERS.addParameter(null, "CHARACTER SET", "utf8");
				MYSQL_CREATION_PARAMETERS.addParameter(null, "COLLATE", systemVariables.getMysql_collation());
				MYSQL_CREATION_PARAMETERS.addParameter(null, "ROW_FORMAT", "DYNAMIC");
				return platform.getCreateTablesSql(database, MYSQL_CREATION_PARAMETERS, false, true);
		}	
	}
	
	private class DropSchemaPlatformActor implements PlatformActor {

		public String getSqlFromPlatform(Platform platform,
			Database database) {
				return platform.getDropTablesSql(database, true);
		}	
	}
	
	/** The database utility. */
	private DatabaseUtility databaseUtility;
	
	private IndexServiceManager indexServiceManager;
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The transaction manager. */
	private PlatformTransactionManager transactionManager;
	
	private Registry registry;
	
	/** The database type. */
	private DatabaseType databaseType;
	
	private SystemVariables systemVariables;
	
	private TransitivityBuilder transitivityBuilder;
	
	private PrimaryKeyIncrementer primaryKeyIncrementer;
	
	private RootBuilder rootBuilder;
	
	private Resource codingSchemeXmlDdl;
	
	private Resource codingSchemeHistoryXmlDdl;
	
	private Resource commonXmlDdl;
	
	private Resource valueSetXmlDdl;
	
	private Resource valueSetHistoryXmlDdl;
	
	private Resource nciHistoryXmlDdl;

	@Override
	public void addRootRelationNode(String codingSchemeUri,
			String codingSchemeVersion, List<String> associationNames,
			String relationContainerName, RootOrTail rootOrTail,
			TraverseAssociations traverse) {
		rootBuilder.addRootRelationNode(
				codingSchemeUri, 
				codingSchemeVersion, 
				associationNames,
				relationContainerName, 
				rootOrTail, traverse);
	}

	/* (non-Javadoc)
	 * @see org.LexGrid.persistence.connection.PersistenceConnectionManager#isCodingSchemeLoaded(java.lang.String, java.lang.String)
	 */
	public boolean isCodingSchemeLoaded(String codingScheme, String version) {
		return false;
	}

	@Override
	public void createAllTables() {
		this.createCommonTables();
		this.createNciHistoryTables();
		this.createValueSetsTables();
		this.createValueSetHistoryTables();

		if(this.systemVariables.isSingleTableMode()) {
			this.createCodingSchemeTables();
			this.createCodingSchemeHistoryTables();
		}
	}

	@Override
	public void dropAllTables() {
		if(!this.systemVariables.isSingleTableMode()) {
			for(RegistryEntry entry :
				this.registry.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME)) {
				
				String uri = entry.getResourceUri();
				String version = entry.getResourceVersion();
				this.dropCodingSchemeTables(uri, version);
				this.dropCodingSchemeHistoryTables(uri, version);
			}
		} else {
			this.dropCodingSchemeHistoryTables();
			this.dropCodingSchemeTables();
		}
		
		this.dropNciHistoryTables();
		this.dropValueSetHistoryTables();
		this.dropValueSetsTables();
		this.dropCommonTables();
	}

	@Override
	public void createCodingSchemeHistoryTables(String prefix) {
		this.doExecuteSql(this.codingSchemeHistoryXmlDdl, new CreateSchemaPlatformActor(), prefix);	
	}

	@Override
	public void dropCodingSchemeHistoryTables(String codingSchemeUri,
			String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);
		this.doExecuteSql(this.codingSchemeHistoryXmlDdl, new DropSchemaPlatformActor(), prefix);	
	}
	
	@Override
	public void dropCodingSchemeHistoryTablesByPrefix(String prefix) {
		this.doExecuteSql(this.codingSchemeHistoryXmlDdl, new DropSchemaPlatformActor(), prefix);	
	}

	@Override
	public void dropCodingSchemeTables(String codingSchemeUri, String version) {
		String prefix = this.getPrefixResolver().resolvePrefixForCodingScheme(codingSchemeUri, version);

		this.doDropCodingSchemeTables(codingSchemeUri, version, prefix);
	}
	
	protected void doDropCodingSchemeTables(final String codingSchemeUri, final String version, final String prefix) {
		
		TransactionTemplate template = new TransactionTemplate(this.getTransactionManager());
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		
		template.execute(new TransactionCallback() {

			@Override
			public Object doInTransaction(TransactionStatus status) {

				if(! getSystemVariables().isSingleTableMode()) {
					dropCodingSchemeHistoryTables(codingSchemeUri, version);
				}
				
				doExecuteSql(codingSchemeXmlDdl, new DropSchemaPlatformActor(), prefix);	
				
				return null;
			}
		});
	}
	
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void dropCodingSchemeTablesByPrefix(final String prefix){

		TransactionTemplate template = new TransactionTemplate(this.getTransactionManager());
		template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		
		template.execute(new TransactionCallback() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				
				dropCodingSchemeHistoryTablesByPrefix(prefix); 
				doExecuteSql(codingSchemeXmlDdl, new DropSchemaPlatformActor(), prefix);	
				
				return null;
			}
		});
	}
	
	@Override
	public void dropCodingSchemeHistoryTables() {
		this.doExecuteSql(this.codingSchemeHistoryXmlDdl, new DropSchemaPlatformActor());	
	}

	@Override
	public void dropCodingSchemeTables() {
		this.doExecuteSql(this.codingSchemeXmlDdl, new DropSchemaPlatformActor());	
	}

	@Override
	public void dropCommonTables() {
		this.doExecuteSql(this.commonXmlDdl, new DropSchemaPlatformActor());	
	}

	@Override
	public void dropNciHistoryTables() {
		this.doExecuteSql(this.nciHistoryXmlDdl, new DropSchemaPlatformActor());	
	}

	@Override
	public void dropValueSetHistoryTables() {
		this.doExecuteSql(this.valueSetHistoryXmlDdl, new DropSchemaPlatformActor());	
	}

	@Override
	public void dropValueSetsTables() {
		this.doExecuteSql(this.valueSetXmlDdl, new DropSchemaPlatformActor());	
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#createCommonTables()
	 */
	public void createCommonTables() {
		this.doExecuteSql(this.commonXmlDdl, new CreateSchemaPlatformActor());
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#createCodingSchemeTables()
	 */
	public void createCodingSchemeTables() {
		this.createCodingSchemeTables(this.prefixResolver.resolveDefaultPrefix());
	}
	
	@Override
	public void createNciHistoryTables() {
		this.doExecuteSql(this.nciHistoryXmlDdl, new CreateSchemaPlatformActor());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#createCodingSchemeTables(java.lang.String)
	 */
	public void createCodingSchemeTables(String prefix) {
		this.doExecuteSql(this.codingSchemeXmlDdl, new CreateSchemaPlatformActor(), prefix);	
	
		if(! this.getSystemVariables().isSingleTableMode()) {
			this.createCodingSchemeHistoryTables(prefix);
		}
	}
	
	protected void doExecuteSql(Resource xmlSchema, PlatformActor actor) {
		this.doExecuteSql(xmlSchema, actor, null);
	}
	
	protected void doExecuteSql(Resource xmlSchema, PlatformActor actor, String prefix) {
		try {
			String sql = this.doGetSql(xmlSchema, actor);
			if(prefix == null) {
				databaseUtility.executeScript(sql, this.getPrefixResolver().resolveDefaultPrefix());
			} else {
				databaseUtility.executeScript(sql, this.getPrefixResolver().resolveDefaultPrefix(), prefix);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	protected String doGetSql(Resource xmlSchema, PlatformActor actor) {
		return this.doGetSql(null, xmlSchema, actor);
	}
	
	protected String doGetSql(DatabaseType databaseType, Resource xmlSchema, PlatformActor actor) {
		Database db = readDatabase(xmlSchema);
		
		Platform platform;
		if(databaseType == null) {
			platform = PlatformFactory.createNewPlatformInstance(this.dataSource);
		} else {
			platform = PlatformFactory.createNewPlatformInstance(databaseType.getProductName());
			if(platform == null) {
				//if that doesn't work, try to look up by the enum name -- this is mostly for HSQLDB
				platform = PlatformFactory.createNewPlatformInstance(databaseType.toString());
				
				//if that still doesn't work, check aliases
				for(String alias : databaseType.getAliases()) {
					platform = PlatformFactory.createNewPlatformInstance(alias);
					if(platform != null) {break;}
				}
			}
		}
		
		Assert.notNull(platform);

		return actor.getSqlFromPlatform(platform, db);
	}
	
	private Database readDatabase(Resource xmlSchema) {
		DatabaseIO dbio = new NonValidatingDatabaseIO();
		
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(xmlSchema.getInputStream(), writer);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		String schemaString = writer.toString();

		schemaString = schemaString.replaceAll(DatabaseConstants.VARIABLE_KEY_TYPE_PLACEHOLDER, this.primaryKeyIncrementer.getKeyType().toString());
		schemaString = schemaString.replaceAll(DatabaseConstants.VARIABLE_KEY_SIZE_PLACEHOLDER, String.valueOf(this.primaryKeyIncrementer.getKeyLength()));
		
		try {
			return dbio.read(new StringReader(schemaString));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void dumpSqlScripts(DatabaseType databaseType, String path, String prefix) throws IOException {
		List<Resource> scriptResources = 
			DaoUtility.createNonTypedList(
					codingSchemeXmlDdl,
					codingSchemeHistoryXmlDdl,
					commonXmlDdl,
					nciHistoryXmlDdl,
					valueSetXmlDdl,
					valueSetHistoryXmlDdl
					);
		for(Resource resource : scriptResources) {
			this.doDumpSqlScripts(databaseType, resource, path, prefix);
		}
	}
	
	protected void doDumpSqlScripts(DatabaseType databaseType, Resource resource, String destination, String prefix) throws IOException {
		Database db = this.readDatabase(resource);
		String name = db.getName();
		
		String createSql = this.doGetSql(databaseType, resource, new CreateSchemaPlatformActor());
		
		File createFile = new File(destination + File.separator + name + "-" + databaseType.getProductName().toLowerCase() + "-create.sql");
		writeStringToFile(createFile, createSql, prefix);
		
		String dropSql = this.doGetSql(databaseType, resource, new DropSchemaPlatformActor());
		
		File dropFile = new File(destination + File.separator + name + "-" +  databaseType.getProductName().toLowerCase() +  "-drop.sql");
		writeStringToFile(dropFile, dropSql, prefix);	
	}
	
	private void writeStringToFile(File file, String content, String prefix) throws IOException {
		content = content.replaceAll(DatabaseConstants.PREFIX_PLACEHOLDER, prefix);
		
		FileWriter out = new FileWriter(file);
        out.write(content);
        out.close();
	}

	public void createValueSetsTables() {
		this.doExecuteSql(this.valueSetXmlDdl, new CreateSchemaPlatformActor());
	}

	public void createCodingSchemeHistoryTables() {
		this.doExecuteSql(this.codingSchemeHistoryXmlDdl, new CreateSchemaPlatformActor());
	}
	
	public void createValueSetHistoryTables() {
		this.doExecuteSql(this.valueSetHistoryXmlDdl, new CreateSchemaPlatformActor());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#computeTransitiveTable(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void computeTransitiveTable(String codingSchemeUri,
			String codingSchemeVersion) {
		transitivityBuilder.computeTransitivityTable(codingSchemeUri, codingSchemeVersion);
	}
	
	public void reComputeTransitiveTable(String codingSchemeUri,
			String codingSchemeVersion) {
		transitivityBuilder.reComputeTransitivityTable(codingSchemeUri, codingSchemeVersion);
	}
	
	public TransitivityTableState isTransitiveTableComputed(String codingSchemeUri,
			String codingSchemeVersion) {
		return transitivityBuilder.isTransitiveTableComputed(codingSchemeUri, codingSchemeVersion);
	}

	/**
	 * Gets the combined prefix.
	 * 
	 * @param codingSchemePrefix the coding scheme prefix
	 * 
	 * @return the combined prefix
	 */
	protected String getCombinedPrefix(String codingSchemePrefix){
		return prefixResolver.resolveDefaultPrefix() + codingSchemePrefix;
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getDatabaseUtility()
	 */
	public DatabaseUtility getDatabaseUtility() {
		return databaseUtility;
	}

	/**
	 * Sets the database utility.
	 * 
	 * @param databaseUtility the new database utility
	 */
	public void setDatabaseUtility(DatabaseUtility databaseUtility) {
		this.databaseUtility = databaseUtility;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getPrefixResolver()
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

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getDataSource()
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getDatabaseType()
	 */
	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	/**
	 * Sets the database type.
	 * 
	 * @param databaseType the new database type
	 */
	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	/**
	 * Sets the transaction manager.
	 * 
	 * @param transactionManager the new transaction manager
	 */
	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.operation.LexEvsDatabaseOperations#getTransactionManager()
	 */
	public  PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransitivityBuilder(TransitivityBuilder transitivityBuilder) {
		this.transitivityBuilder = transitivityBuilder;
	}

	public TransitivityBuilder getTransitivityBuilder() {
		return transitivityBuilder;
	}

	public void setIndexServiceManager(IndexServiceManager indexServiceManager) {
		this.indexServiceManager = indexServiceManager;
	}

	public IndexServiceManager getIndexServiceManager() {
		return indexServiceManager;
	}

	public void setRootBuilder(RootBuilder rootBuilder) {
		this.rootBuilder = rootBuilder;
	}

	public RootBuilder getRootBuilder() {
		return rootBuilder;
	}

	public Resource getCodingSchemeXmlDdl() {
		return codingSchemeXmlDdl;
	}

	public void setCodingSchemeXmlDdl(Resource codingSchemeXmlDdl) {
		this.codingSchemeXmlDdl = codingSchemeXmlDdl;
	}

	public Resource getCommonXmlDdl() {
		return commonXmlDdl;
	}

	public void setCommonXmlDdl(Resource commonXmlDdl) {
		this.commonXmlDdl = commonXmlDdl;
	}

	public Resource getCodingSchemeHistoryXmlDdl() {
		return codingSchemeHistoryXmlDdl;
	}

	public void setCodingSchemeHistoryXmlDdl(Resource codingSchemeHistoryXmlDdl) {
		this.codingSchemeHistoryXmlDdl = codingSchemeHistoryXmlDdl;
	}

	public Resource getValueSetXmlDdl() {
		return valueSetXmlDdl;
	}

	public void setValueSetXmlDdl(Resource valueSetXmlDdl) {
		this.valueSetXmlDdl = valueSetXmlDdl;
	}

	public Resource getValueSetHistoryXmlDdl() {
		return valueSetHistoryXmlDdl;
	}

	public void setValueSetHistoryXmlDdl(Resource valueSetHistoryXmlDdl) {
		this.valueSetHistoryXmlDdl = valueSetHistoryXmlDdl;
	}

	public Resource getNciHistoryXmlDdl() {
		return nciHistoryXmlDdl;
	}

	public void setNciHistoryXmlDdl(Resource nciHistoryXmlDdl) {
		this.nciHistoryXmlDdl = nciHistoryXmlDdl;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}



	public void setPrimaryKeyIncrementer(PrimaryKeyIncrementer primaryKeyIncrementer) {
		this.primaryKeyIncrementer = primaryKeyIncrementer;
	}

	public PrimaryKeyIncrementer getPrimaryKeyIncrementer() {
		return primaryKeyIncrementer;
	}



	private static class NonValidatingDatabaseIO extends DatabaseIO {
		   
		  public Database read(InputSource inputSource) throws DdlUtilsException
		    {
		        Database model = null;

		        try
		        {
		            model = (Database)getReader().parse(inputSource);
		        }
		        catch (Exception ex)
		        {
		            throw new DdlUtilsException(ex);
		        }
		      
		        Database db = new AliasingDatabase();
		       
		        db.addTables(DaoUtility.createNonTypedList(model.getTables()));
		        db.setName(model.getName());
		        
				return db;
		    }
		  
		  public Database read(Reader reader) throws DdlUtilsException
		    {
		        Database model = null;

		        try
		        {
		            model = (Database)getReader().parse(reader);
		        }
		        catch (Exception ex)
		        {
		            throw new DdlUtilsException(ex);
		        }
		      
		        Database db = new AliasingDatabase();
		       
		        db.addTables(DaoUtility.createNonTypedList(model.getTables()));
		        db.setName(model.getName());
		        
				return db;
		    }
	}
	
	private static class AliasingDatabase extends Database {
	
		private static final long serialVersionUID = 1L;

		@Override
		public Table findTable(String name) {

				try {
					Table table = super.findTable(name);
					if(table == null) {
						return this.createAliasTable(name);
					} else {
						return table;
					}
				} catch (Exception e) {
					return this.createAliasTable(name);
				}
		}
		
		private Table createAliasTable(String alias) {
			Table temp = new Table();
			temp.setName(alias);
			return temp;
		}
	}
	
	public void destroy() throws Exception {
		//Make sure HSQL is properly shutdown on exit
		if(this.getDatabaseType().equals(DatabaseType.HSQL)){
			JdbcTemplate template = new JdbcTemplate(this.dataSource);
			
			template.execute("SHUTDOWN");
		}
	}
}