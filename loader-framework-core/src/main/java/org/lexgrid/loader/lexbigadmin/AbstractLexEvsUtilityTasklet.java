
package org.lexgrid.loader.lexbigadmin;

import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * The Class IndexingTasklet.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLexEvsUtilityTasklet extends LoggingBean implements Tasklet {

	/** The connection manager. */
	private LexEvsDatabaseOperations connectionManager = LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations();
	
	private DatabaseServiceManager databaseServiceManager;
	
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {
			return doExecute(contribution, chunkContext);
	}
		
	protected abstract RepeatStatus doExecute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception;
	/**
	 * Gets the current coding scheme uri.
	 * 
	 * @return the current coding scheme uri
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeUri() throws Exception {	
		return getCurrentCodingScheme().getCodingSchemeURI();
	}
	
	/**
	 * Gets the current coding scheme version.
	 * 
	 * @return the current coding scheme version
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeVersion() throws Exception {
		return getCurrentCodingScheme().getRepresentsVersion();
	}
	
	
	/**
	 * Gets the current coding scheme.
	 * 
	 * @return the current coding scheme
	 * 
	 * @throws Exception the exception
	 */
	protected CodingScheme getCurrentCodingScheme() throws Exception {
		return this.getDatabaseServiceManager().getCodingSchemeService().getCodingSchemeByUriAndVersion(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				this.getCodingSchemeIdSetter().getCodingSchemeVersion());
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(
			CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}

	public void setConnectionManager(LexEvsDatabaseOperations connectionManager) {
		this.connectionManager = connectionManager;
	}

	public LexEvsDatabaseOperations getConnectionManager() {
		return connectionManager;
	}

	public void setDatabaseServiceManager(DatabaseServiceManager databaseServiceManager) {
		this.databaseServiceManager = databaseServiceManager;
	}

	public DatabaseServiceManager getDatabaseServiceManager() {
		return databaseServiceManager;
	}	
}