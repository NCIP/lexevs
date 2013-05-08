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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import junit.framework.Assert;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.LoadStatus;
import org.easymock.classextension.EasyMock;
import org.junit.Test;
import org.lexgrid.loader.logging.StatusTrackingLogger;
import org.lexgrid.loader.test.LoaderFrameworkCoreTestBase;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;

public class StepCompletionListenerTest extends LoaderFrameworkCoreTestBase
{
	@Autowired
	private StepCompletionListener stepComplListener;
	
	@Test
	public void testStepListener()
	{
		assertNotNull(stepComplListener);
	}
	
	
	@Test
	public void testBeforeStep()
	{
		StepExecution se = 
			EasyMock.createMock(StepExecution.class);
		
		EasyMock.expect(se.getStepName()).andReturn("Test Step").anyTimes();
		EasyMock.replay(se);
		
		StatusTrackingLogger originalLogger = this.stepComplListener.getLogger();
	
		stepComplListener.setLogger(new StatusTrackingLogger(){

			public LoadStatus getProcessStatus() {
				// TODO Auto-generated method stub
				return null;
			}

			public void busy() {
				// TODO Auto-generated method stub
				
			}

			public String debug(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String error(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String error(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}

			public String fatal(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String fatal(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}

			public void fatalAndThrowException(String message) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public void fatalAndThrowException(String message,
					Throwable sourceException) throws Exception {
				// TODO Auto-generated method stub
				
			}

			public String info(String message) {
				
				Assert.assertEquals("Step: Test Step starting.", message);
				return message;
			}

			public String warn(String message) {
				// TODO Auto-generated method stub
				return null;
			}

			public String warn(String message, Throwable sourceException) {
				// TODO Auto-generated method stub
				return null;
			}
			
		});
		
		stepComplListener.beforeStep(se);
		
		this.stepComplListener.setLogger(originalLogger);
	}
	
	@Test
	public void testAfterStep()
	{
		StepExecution se = 
			EasyMock.createMock(StepExecution.class);
		
		EasyMock.expect(se.getStepName()).andReturn("Test Step").anyTimes();
		EasyMock.expect(se.getSummary()).andReturn("Test Summary").anyTimes();
		EasyMock.expect(se.getFailureExceptions()).andReturn(new ArrayList<Throwable>()).anyTimes();
		EasyMock.expect(se.getExitStatus()).andReturn(null).anyTimes();
		EasyMock.replay(se);
	
		stepComplListener.afterStep(se);
	}
}