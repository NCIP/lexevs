
package org.lexgrid.loader;

import java.io.File;
import java.net.URI;
import java.util.Enumeration;
import java.util.Properties;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.LexBIG.Utility.logging.CachingMessageDirectorIF;
import org.lexevs.dao.database.spring.DynamicPropertyApplicationContext;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.system.utility.MyClassLoader;
import org.lexgrid.loader.logging.SpringBatchMessageDirector;
import org.lexgrid.loader.properties.impl.PropertiesFactory;
import org.lexgrid.loader.setup.JobRepositoryManager;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * The Class AbstractSpringBatchLoader.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSpringBatchLoader extends BaseLoader implements Loader {

	private static final long serialVersionUID = -4179393859521278360L;
	
	private JobExecution jobExecution;
	
	private URNVersionPair[] loadedCodingSchemes;
	
	protected AbstractSpringBatchLoader(){
		super();
	}

	/**
	 * Launch job.
	 * 
	 * NOT THREAD SAFE!
	 * 
	 * ONLY ONE Load is allowed at a time.
	 * 
	 * @param connectionProperties the connection properties
	 * @param jobConfigFile the job config file
	 * @param jobName the job name
	 * 
	 * @return the job execution
	 * 
	 * @throws Exception the exception
	 */
	protected void launchJob(Properties connectionProperties, String jobConfigFile, String jobName) throws Exception {
		super.setInUse();
		
		Thread.currentThread().setContextClassLoader(MyClassLoader.instance());

		DynamicPropertyApplicationContext ctx = new DynamicPropertyApplicationContext(jobConfigFile, connectionProperties);
		ctx.setClassLoader(MyClassLoader.instance());
		
		//Register a shutdown hook to make sure beans get disposed.
		ctx.registerShutdownHook();
		
		JobLauncher jobLauncher = (JobLauncher)ctx.getBean("jobLauncher");
		Job job = (Job)ctx.getBean(jobName);
		
		CachingMessageDirectorIF springBatchMessageDirector = (CachingMessageDirectorIF)ctx.getBean("logger");
		this.setCachingMessageDirectorIF(springBatchMessageDirector);
		
		printStartLogInfo(connectionProperties, jobConfigFile, jobName);
		
		jobExecution = jobLauncher.run(job, buildJobParameters(connectionProperties));	
		
		this.setLoadedCodingSchemes(
				this.getLoadedCodingSchemes(ctx));

		if(jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)){
			JobRepositoryManager jobRepositoryManager = (JobRepositoryManager)ctx.getBean("jobRepositoryManager");
			jobRepositoryManager.dropJobRepositoryDatabases();
		} else {
			this.getStatus().setErrorsLogged(true);
			this.getStatus().setWarningsLogged(true);
			this.getStatus().setState(ProcessState.PENDING);
		}
	}
	
	protected abstract URNVersionPair[] getLoadedCodingSchemes(ApplicationContext context);
	
	protected void printStartLogInfo(Properties connectionProperties, String jobConfigFile, String jobName){
		this.getMessageDirector().info("Starting Loader Job :" + jobName);
		printJobProperties(connectionProperties);
	}
	
	protected void printJobProperties(Properties jobProperties){
		StringBuffer connectionProps = new StringBuffer();
		connectionProps.append("Job Properties : \n");	
		Enumeration<Object> keys = jobProperties.keys();
		while(keys.hasMoreElements()){
			Object key = keys.nextElement();
			connectionProps.append("Property: " + key.toString() + "\n");
			String propertyValue = null;
			
			propertyValue = jobProperties.getProperty(key.toString());

			connectionProps.append(" - Value: " + propertyValue + "\n");
		}
		this.getMessageDirector().info(connectionProps.toString());
	}
	
	/**
	 * Builds the job parameters.
	 * 
	 * @param props the props
	 * 
	 * @return the job parameters
	 */
	protected JobParameters buildJobParameters(Properties props){	
		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addString(PropertiesFactory.PREFIX, props.getProperty(PropertiesFactory.PREFIX));
		
		return builder.toJobParameters();	
	}

	protected ProcessState processStateConverter(BatchStatus status){
		if(status.equals(BatchStatus.COMPLETED)){
			return ProcessState.COMPLETED;
		}
		if(status.equals(BatchStatus.STOPPED)){
			return ProcessState.CANCELED;
		}
		if(status.equals(BatchStatus.STARTED)){
			return ProcessState.PROCESSING;
		}
		if(status.equals(BatchStatus.STARTING)){
			return ProcessState.PENDING;
		}
		if(status.equals(BatchStatus.STOPPING)){
			return ProcessState.PENDING;
		}
		else return ProcessState.UNKNOWN;	
	}

	public JobExecution getJobExecution() {
		return jobExecution;
	}
	
	public static URI getURIFromPath(String path){
		URI uri = null;
		// is this a local file?
		File theFile = new File(path);

		if (theFile.exists()) {
			uri = theFile.toURI();
		} else {
			// is it a valid URI (like http://something)
			try {
				uri = new URI(path);
				uri.toURL().openConnection();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return uri;
	}

	public void setLoadedCodingSchemes(URNVersionPair[] loadedCodingSchemes) {
		this.loadedCodingSchemes = loadedCodingSchemes;
	}

	public URNVersionPair[] getLoadedCodingSchemes() {
		return loadedCodingSchemes;
	}
}