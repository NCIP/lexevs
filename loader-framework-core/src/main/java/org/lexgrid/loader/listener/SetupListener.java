
package org.lexgrid.loader.listener;

import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * The listener interface for receiving setup events.
 * The class that is interested in processing a setup
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addSetupListener<code> method. When
 * the setup event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see SetupEvent
 */
public class SetupListener extends LoggingBean implements JobExecutionListener{
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.JobExecutionListener#afterJob(org.springframework.batch.core.JobExecution)
	 */
	public void afterJob(JobExecution job) {
		
	String message = null;
	message = job.getStatus().name() == "FAILED"? job.getStatus().name() + " " + exitFailedDescription(job): job.getExitStatus().getExitCode();
  //
		getLogger().info("Load Job ended in state: " + message ) ;
	}
	
	public String exitFailedDescription(JobExecution job){
		String message =""
				+ "\n********************************************************************"
				+ "\n****** FAILED TO Complete LOAD of RRF ******************************"
				+ "\n****** One or more threads of the batch execution have failed ******"
				+ "\n****** Please Review Logs for More Information *********************"
				+ "\n********************************************************************";
		return message;
	}
	/* (non-Javadoc)
	 * @see org.springframework.batch.core.JobExecutionListener#beforeJob(org.springframework.batch.core.JobExecution)
	 */
	public void beforeJob(JobExecution job) {
		getLogger().info("Executing Load Job Id: " + job.getId() + " at " + job.getStartTime()) ;
	}
}