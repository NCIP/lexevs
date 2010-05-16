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

import org.aopalliance.intercept.MethodInvocation;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * The Class LazyLoadingCodeToReturnInterceptor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ErrorCallbackInterceptor extends TransactionInterceptor {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8816574427519953487L;

    /** The callback. */
    private ErrorCallbackListener errorCallbackListener;
    
    private static String UNKNOWN_ERROR_CODE = DatabaseError.UNKNOWN_ERROR_CODE;
    
    /**
     * Instantiates a new dao validating interceptor.
     * 
     * @param errorCallbackListener the error callback listener
     */
    public ErrorCallbackInterceptor(ErrorCallbackListener errorCallbackListener){
        this.errorCallbackListener = errorCallbackListener;
        this.setTransactionManager(
        		LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().getTransactionManager());
       
        this.setTransactionAttributeSource( new AnnotationTransactionAttributeSource());
        this.afterPropertiesSet();
    }
    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
   
    	DatabaseErrorIdentifier errorId = 
    		AnnotationUtils.findAnnotation(methodInvocation.getMethod(), DatabaseErrorIdentifier.class);

    	final String errorCode;
    	if(errorId == null) {
    		errorCode = UNKNOWN_ERROR_CODE;
    	} else {
    		errorCode = errorId.errorCode();
    	}

    	try {
    		return super.invoke(methodInvocation);
    	} catch (Exception e) {
    		errorCallbackListener.onDatabaseError(new DefaultDatabaseError(errorCode, methodInvocation.getArguments(), e)); 
    		return null;
    	} 
    }	
}
