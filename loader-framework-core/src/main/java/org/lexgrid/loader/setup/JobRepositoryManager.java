
package org.lexgrid.loader.setup;

import org.lexevs.dao.database.constants.DatabaseConstants;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.type.DatabaseType;
import org.lexevs.dao.database.utility.DefaultDatabaseUtility;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * The Class JobRepositoryManager.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class JobRepositoryManager extends LoggingBean implements InitializingBean, DisposableBean {

	/** The database utility. */
	private LexEvsDatabaseOperations lexEvsDatabaseOperations;
	
	/** The create script. */
	private Resource createScript;
	
	/** The drop script. */
	private Resource dropScript;
	
	private boolean dropOnClose = false;
	
	private DatabaseType databaseType;
	
	private String prefix;
	
	//Not needed now... just in case a subclass might...
	/** The tables. */
	protected String[] tables = {
			" BATCH_JOB_INSTANCE ",
			" BATCH_JOB_EXECUTION ",
			" BATCH_JOB_PARAMS ",
			" BATCH_STEP_EXECUTION ",
			" BATCH_STEP_EXECUTION_CONTEXT ",
			" BATCH_JOB_EXECUTION_CONTEXT ",
			" BATCH_JOB_INSTANCE\\(",
			" BATCH_JOB_EXECUTION\\(",
			" BATCH_JOB_PARAMS\\(",
			" BATCH_STEP_EXECUTION\\(",
			" BATCH_STEP_EXECUTION_CONTEX\\(",
			" BATCH_STEP_EXECUTION_SEQ",
			" BATCH_JOB_EXECUTION_SEQ",
			" BATCH_JOB_EXECUTION_SEQ",
			" BATCH_JOB_SEQ",
			};

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		if(! doJobRepositoryTablesExist()){
			getLogger().info("Creating Job Repository.");
			String script = DefaultDatabaseUtility.convertResourceToString(createScript);
			lexEvsDatabaseOperations.getDatabaseUtility().executeScript(insertPrefixVariable(script), 
					prefix);
		} else {
			getLogger().info("Not Creating Job Repository.");
		}
	}
	
	/**
	 * Insert prefix variable.
	 * 
	 * @param script the script
	 * 
	 * @return the string
	 */
	protected String insertPrefixVariable(String script){
		script = script.replaceAll("BATCH_", DatabaseConstants.PREFIX_PLACEHOLDER);
		if(! databaseType.equals(DatabaseType.DB2)){
			script = script.replaceAll("constraint ", "constraint " + DatabaseConstants.PREFIX_PLACEHOLDER);
		}
		
		return script;
	}
	

	/**
	 * Insert prefix variable.
	 * 
	 * @param script the script
	 * 
	 * @return the string
	 */
	protected boolean doJobRepositoryTablesExist(){
		try {
			lexEvsDatabaseOperations.getDatabaseUtility().executeScript("SELECT * FROM " + prefix + "JOB_INSTANCE");
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * Drop job repository databases.
	 * 
	 * @throws Exception the exception
	 */
	public void dropJobRepositoryDatabases() throws Exception {
		String script = DefaultDatabaseUtility.convertResourceToString(dropScript);
		lexEvsDatabaseOperations.getDatabaseUtility().executeScript(insertPrefixVariable(script), prefix);
	}
	
	public void dropJobRepositoryDatabasesOnClose() throws Exception {
		dropOnClose = true;
	}

	public void destroy() throws Exception {
		if(dropOnClose){
			getLogger().info("Dropping Job Repository Databases on close of Application.");
			dropJobRepositoryDatabases();
		}	
	}
	
	/**
	 * Gets the creates the script.
	 * 
	 * @return the creates the script
	 */
	public Resource getCreateScript() {
		return createScript;
	}

	/**
	 * Sets the creates the script.
	 * 
	 * @param createScript the new creates the script
	 */
	public void setCreateScript(Resource createScript) {
		this.createScript = createScript;
	}

	/**
	 * Gets the drop script.
	 * 
	 * @return the drop script
	 */
	public Resource getDropScript() {
		return dropScript;
	}

	/**
	 * Sets the drop script.
	 * 
	 * @param dropScript the new drop script
	 */
	public void setDropScript(Resource dropScript) {
		this.dropScript = dropScript;
	}

	public boolean isDropOnClose() {
		return dropOnClose;
	}

	public void setDropOnClose(boolean dropOnClose) {
		this.dropOnClose = dropOnClose;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	public LexEvsDatabaseOperations getLexEvsDatabaseOperations() {
		return lexEvsDatabaseOperations;
	}

	public void setLexEvsDatabaseOperations(
			LexEvsDatabaseOperations lexEvsDatabaseOperations) {
		this.lexEvsDatabaseOperations = lexEvsDatabaseOperations;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix;
	}
	
	
}