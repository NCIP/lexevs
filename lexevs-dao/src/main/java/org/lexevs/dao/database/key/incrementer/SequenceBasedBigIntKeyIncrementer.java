
package org.lexevs.dao.database.key.incrementer;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.lexevs.dao.database.prefix.PrefixResolver;
import org.lexevs.dao.database.type.DatabaseType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.HsqlSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.PostgreSQLSequenceMaxValueIncrementer;

/**
 * The Class SequenceBasedBigIntKeyIncrementer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SequenceBasedBigIntKeyIncrementer 
	extends AbstractBigIntKeyIncrementer {

	private static List<DatabaseType> SUPPORTED_DATABASE_TYPES = 
		Arrays.asList(DatabaseType.DB2, DatabaseType.HSQL, DatabaseType.ORACLE, DatabaseType.POSTGRES);
	
	private static String SEQUENCE_NAME = "incrementer_sequence";
	
	private static String SEQUENCE_NAME_PLACEHOLDER = "@SEQUENCE_NAME_PLACEHOLDER@";
	
	/** The create sequence sql. */
	private String createSequenceSql = "create sequence " + SEQUENCE_NAME_PLACEHOLDER;
	
	/** The drop sequence sql. */
	private String dropSequenceSql = "drop sequence " + SEQUENCE_NAME_PLACEHOLDER;
	
	/** The database type. */
	private DatabaseType databaseType;
	
	/** The data source. */
	private DataSource dataSource;
	
	/** The prefix resolver. */
	private PrefixResolver prefixResolver;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.AbstractKeyIncrementer#createDataFieldMaxValueIncrementer()
	 */
	@Override
	protected DataFieldMaxValueIncrementer createDataFieldMaxValueIncrementer() {
		DataFieldMaxValueIncrementer dataFieldMaxValueIncrementer;
		switch(databaseType) {
			case HSQL : {
				dataFieldMaxValueIncrementer = new HsqlSequenceMaxValueIncrementer(this.getDataSource(), getSequenceName());
				break;
			}
			case DB2 : {
				dataFieldMaxValueIncrementer = new DB2SequenceMaxValueIncrementer(this.getDataSource(), getSequenceName());
				break;
			}
			case ORACLE : {
				dataFieldMaxValueIncrementer = new OracleSequenceMaxValueIncrementer(this.getDataSource(), getSequenceName());
				break;
			}
			case POSTGRES : {
				dataFieldMaxValueIncrementer = new PostgreSQLSequenceMaxValueIncrementer(this.getDataSource(), getSequenceName());
				break;
			}
			default : {
				throw new RuntimeException("Could not create a DataFieldMaxValueIncrementer.");
			}
		}	
		return dataFieldMaxValueIncrementer;
	}
	
	/**
	 * Gets the sequence name.
	 * 
	 * @return the sequence name
	 */
	protected String getSequenceName() {
		return this.getPrefixResolver().resolveDefaultPrefix() + SEQUENCE_NAME;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#destroy()
	 */
	@Override
	public void destroy() {
		this.dropSequence(this.getSequenceName());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.PrimaryKeyIncrementer#initialize()
	 */
	@Override
	public void initialize() {
		this.createSequence(this.getSequenceName());
	}

	/**
	 * Creates the sequence.
	 * 
	 * @param sequenceName the sequence name
	 */
	protected void createSequence(String sequenceName) {
		this.executeSql(adjustForSequenceName(sequenceName, this.createSequenceSql));
	}
	
	/**
	 * Drop sequence.
	 * 
	 * @param sequenceName the sequence name
	 */
	protected void dropSequence(String sequenceName) {
		this.executeSql(adjustForSequenceName(sequenceName, this.dropSequenceSql));
	}
	
	/**
	 * Execute sql.
	 * 
	 * @param sql the sql
	 */
	private void executeSql(String sql) {
		new JdbcTemplate(this.getDataSource()).execute(sql);
	}
	
	/**
	 * Adjust for sequence name.
	 * 
	 * @param sequenceName the sequence name
	 * @param sql the sql
	 * 
	 * @return the string
	 */
	private String adjustForSequenceName(String sequenceName, String sql) {
		return sql.replaceAll(SEQUENCE_NAME_PLACEHOLDER, sequenceName);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.key.incrementer.AbstractKeyIncrementer#getSupportedDatabaseTypes()
	 */
	@Override
	protected List<DatabaseType> getSupportedDatabaseTypes() {
		return SUPPORTED_DATABASE_TYPES;
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Gets the database type.
	 * 
	 * @return the database type
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