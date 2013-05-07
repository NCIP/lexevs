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

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.lexevs.logging.LoggerFactory;

/**
 * The Class LoggingAspect.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
public class LoggingAspect {

	private LgLoggerIF logger;
	
	/**
	 * Log method.
	 *
	 * @param pjp the pjp
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around("@within(org.cts2.internal.logging.annotation.Loggable) && " +
			"( @annotation(org.cts2.internal.logging.annotation.Loggable) )")
	public Object logMethod(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature sig = (MethodSignature)pjp.getSignature();
		
		String msg = this.logMethod(sig);
		
		this.logger.info(msg);
		
		return pjp.proceed();	
	}
	
	/**
	 * Log method.
	 *
	 * @param signature the signature
	 * @return the string
	 */
	protected String logMethod(MethodSignature signature){
		return signature.toLongString();
	}

	public void setLogger(LgLoggerIF logger) {
		this.logger = logger;
	}

	public LgLoggerIF getLogger() {
		return logger;
	}
}
