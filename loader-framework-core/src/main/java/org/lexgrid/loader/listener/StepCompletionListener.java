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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.lexgrid.loader.logging.LoggingBean;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * The listener interface for receiving stepCompletion events.
 * The class that is interested in processing a stepCompletion
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addStepCompletionListener<code> method. When
 * the stepCompletion event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see StepCompletionEvent
 */
public class StepCompletionListener extends LoggingBean implements StepExecutionListener {

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#afterStep(org.springframework.batch.core.StepExecution)
	 */
	public ExitStatus afterStep(StepExecution step) {
		getLogger().info("Step: " + step.getStepName() + " completed.");
		getLogger().info("Summary for Step: " +step.getStepName() + "\n" + step.getSummary().replace(",", "\n - Step Property: "));
		
		List<Throwable> exceptionList = step.getFailureExceptions();
		if(exceptionList.size() > 0){
			getLogger().warn("Exceptions for Step: " +step.getStepName() + " " + step.getSummary());
			for(Throwable ex : exceptionList){
				getLogger().warn("- Exception: " +  ex + "\n" + getStackTrace(ex));
			}
		}

		return step.getExitStatus();
	}

	/* (non-Javadoc)
	 * @see org.springframework.batch.core.StepExecutionListener#beforeStep(org.springframework.batch.core.StepExecution)
	 */
	public void beforeStep(StepExecution step) {
		getLogger().info("Step: " + step.getStepName() + " starting.");
	}
	
	/**
	 * Gets the stack trace.
	 * 
	 * @param t the t
	 * 
	 * @return the stack trace
	 */
	private static String getStackTrace(Throwable t) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		pw.flush();
		sw.flush();
		return sw.toString();
	}
}