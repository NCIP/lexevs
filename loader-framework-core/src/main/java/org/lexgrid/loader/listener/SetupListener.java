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