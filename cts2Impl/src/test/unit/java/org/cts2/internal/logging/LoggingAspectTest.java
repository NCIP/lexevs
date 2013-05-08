/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.logging;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.cts2.test.BaseCts2UnitTest;
import org.cts2.test.NoOpLogger;
import org.cts2.test.TestLoggableClass;
import org.junit.Test;
import org.springframework.test.annotation.DirtiesContext;

public class LoggingAspectTest  extends BaseCts2UnitTest {
	
	@Resource
	private LoggingAspect loggingAspect;
	
	@Resource
	private TestLoggableClass testLoggableClass;
	
	@Test
	public void testNotNull(){
		assertNotNull(this.loggingAspect);
	}

	@DirtiesContext
	@Test
	public void testLoggingIntercepted(){
		TestLogger testLogger = new TestLogger();
		this.loggingAspect.setLogger(testLogger);
		
		testLoggableClass.loggableMethod();
		
		assertTrue(testLogger.called);
	}
	
	@DirtiesContext
	@Test
	public void testLoggingInterceptedNonLoggable(){
		TestLogger testLogger = new TestLogger();
		this.loggingAspect.setLogger(testLogger);
		
		testLoggableClass.unLoggableMethod();
		
		assertFalse(testLogger.called);
	}
	
	private class TestLogger extends NoOpLogger {
		
		private boolean called = false;

		@Override
		public String info(String message) {
			this.called = true;
			
			return null;
		}
	}
}
