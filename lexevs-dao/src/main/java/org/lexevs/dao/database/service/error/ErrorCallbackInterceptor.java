/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.database.service.error;

import java.io.Serializable;

import junit.framework.Assert;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * The Class LazyLoadingCodeToReturnInterceptor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ErrorCallbackInterceptor implements MethodInterceptor, Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8816574427519953487L;

    /** The callback. */
    private ErrorCallbackListener errorCallbackListener;
    
    /**
     * Instantiates a new dao validating interceptor.
     * 
     * @param errorCallbackListener the error callback listener
     */
    public ErrorCallbackInterceptor(ErrorCallbackListener errorCallbackListener){
        this.errorCallbackListener = errorCallbackListener;
    }
    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
    	Assert.assertFalse("Cannot use Validating Listerner on methods that return anything other than NULL",
    			methodInvocation.getMethod().getReturnType().getClass().equals(Void.class));
    	
    	DatabaseErrorIdentifier errorId = 
    		AnnotationUtils.findAnnotation(methodInvocation.getMethod(), DatabaseErrorIdentifier.class);
    	
    	String errorCode = errorId.errorCode();
    	
        Object returnObj = null;
        try {
            methodInvocation.proceed();
        } catch (Exception e) {
        	errorCallbackListener.onDatabaseError(new DefaultDatabaseError(errorCode, methodInvocation.getArguments(), e)); 
        }
        return returnObj;
    }
}
