
package org.lexgrid.loader.listener;

import java.util.Date;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.loader.constants.LoaderConstants;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.logging.LoggingBean;
import org.lexgrid.loader.properties.impl.PropertiesFactory;
import org.lexgrid.loader.setup.JobRepositoryManager;
import org.lexgrid.loader.staging.StagingManager;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * The listener interface for receiving cleanup events.
 * The class that is interested in processing a cleanup
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCleanupListener<code> method. When
 * the cleanup event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see CleanupEvent
 */
public class CleanupListener extends LoggingBean implements JobExecutionListener {

	/** The connection manager. */
	private SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
	
	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	/** The staging manager. */
	private StagingManager stagingManager;
	
	/** The job repository manager. */
	private JobRepositoryManager jobRepositoryManager;
	
	private String prefix;
	
	private String database;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.JobExecutionListener#afterJob(org.springframework.batch.core.JobExecution)
	 */
	public void afterJob(JobExecution jobExecution) {
		
		if(jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)){
			getLogger().info("Job completed, dropping tables");
			try {
				stagingManager.dropAllStagingDatabases();
				
				AbsoluteCodingSchemeVersionReference ref = 
					DaoUtility.createAbsoluteCodingSchemeVersionReference(this.getCurrentCodingSchemeUri(), this.getCurrentCodingSchemeVersion());
				
				systemResourceService.updateCodingSchemeResourceStatus(ref, CodingSchemeVersionStatus.INACTIVE);
			
				super.getLogger().getProcessStatus().setState(ProcessState.COMPLETED);
			} catch (Exception e) {
				throw new RuntimeException("Not all Staging Databases have been dropped.", e);
			}
		} else if(jobExecution.getExitStatus().getExitCode().equals(LoaderConstants.NON_RECOVERABLE)){
			getLogger().info("Non recoverable error in Job Processing. Job is not restartable, dropping all tables.");
			try {	
				
				stagingManager.dropAllStagingDatabases();
				jobRepositoryManager.dropJobRepositoryDatabasesOnClose();
				
				super.getLogger().getProcessStatus().setState(ProcessState.FAILED);
			} catch (Exception e) {
				throw new RuntimeException("Not all Staging Databases have been cleaned up.", e);
			}
		}
		
		super.getLogger().getProcessStatus().setEndTime(new Date());
	}
	
	private String getParameterFromJobExecution(String parameter, JobExecution jobExecution){
		return jobExecution.getJobInstance().getJobParameters().getString(parameter);
	}
	

	public void beforeJob(JobExecution arg0) {
		//no-op -- handle this logging elsewhere	
	}

	/**
	 * Gets the staging manager.
	 * 
	 * @return the staging manager
	 */
	public StagingManager getStagingManager() {
		return stagingManager;
	}

	/**
	 * Sets the staging manager.
	 * 
	 * @param stagingManager the new staging manager
	 */
	public void setStagingManager(StagingManager stagingManager) {
		this.stagingManager = stagingManager;
	}
	
	/**
	 * Gets the current coding scheme uri.
	 * 
	 * @return the current coding scheme uri
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeUri() throws Exception {	
		return this.getCodingSchemeIdSetter().getCodingSchemeUri();
	}
	
	/**
	 * Gets the current coding scheme version.
	 * 
	 * @return the current coding scheme version
	 * 
	 * @throws Exception the exception
	 */
	protected String getCurrentCodingSchemeVersion() throws Exception {
		return this.getCodingSchemeIdSetter().getCodingSchemeVersion();
	}


	/**
	 * Gets the job repository manager.
	 * 
	 * @return the job repository manager
	 */
	public JobRepositoryManager getJobRepositoryManager() {
		return jobRepositoryManager;
	}

	/**
	 * Sets the job repository manager.
	 * 
	 * @param jobRepositoryManager the new job repository manager
	 */
	public void setJobRepositoryManager(JobRepositoryManager jobRepositoryManager) {
		this.jobRepositoryManager = jobRepositoryManager;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	public void setCodingSchemeIdSetter(CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}