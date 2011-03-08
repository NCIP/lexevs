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
package org.cts2.utility;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.cts2.service.core.QueryControl;

import com.google.common.util.concurrent.SimpleTimeLimiter;

/**
 * The Class ExecutionUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ExecutionUtils {

	private static SimpleTimeLimiter TIME_LIMITER = new SimpleTimeLimiter();
	
	/**
	 * Call with timeout.
	 *
	 * @param callable the callable
	 * @param timeoutDuration the timeout duration
	 * @param timeoutUnit the timeout unit
	 * @return T
	 */
	public static <T> T callWithTimeout(Callable<T> callable, long timeoutDuration, TimeUnit timeoutUnit){
		
		try {
			return TIME_LIMITER.callWithTimeout(callable, timeoutDuration, timeoutUnit, false);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Call with timeout.
	 *
	 * @param callable the callable
	 * @param queryControl the query control
	 * @return T
	 */
	public static <T> T callWithTimeout(Callable<T> callable, QueryControl queryControl){
		return callWithTimeout(callable, queryControl, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * Call with timeout.
	 *
	 * @param callable the callable
	 * @param queryControl the query control
	 * @param timeoutUnit the timeout unit
	 * @return T
	 */
	public static <T> T callWithTimeout(Callable<T> callable, QueryControl queryControl, TimeUnit timeoutUnit){
		if(queryControl == null || queryControl.getTimeLimit() == null){
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return callWithTimeout(callable, queryControl.getTimeLimit(), timeoutUnit);
		}
	}
}
