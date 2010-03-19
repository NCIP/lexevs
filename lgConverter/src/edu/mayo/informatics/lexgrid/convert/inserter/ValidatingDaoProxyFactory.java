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
package edu.mayo.informatics.lexgrid.convert.inserter;

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;

import edu.mayo.informatics.lexgrid.convert.validator.error.LoadValidationError;
import edu.mayo.informatics.lexgrid.convert.validator.resolution.ErrorResolutionReport;

/**
 * A factory for creating ValidatingDaoProxy objects.
 */
public class ValidatingDaoProxyFactory<T> {

    /** The method match pattern. */
    private String methodMatchPattern;
    
    /** The advice. */
    private Advice advice;
    
    /** The dao. */
    private T dao;
    
    /**
     * Builds the error reporting dao.
     * 
     * @return the error reporting dao< t>
     */
    public ErrorReportingDao<T> buildErrorReportingDao(){
        ErrorReportCallback callback = new ErrorReportCallback();
        ErrorReportingDao<T>  errorReportingDao = new ErrorReportingDao<T>();
        
        T proxy = makeProxy(dao, callback);
        
        return errorReportingDao;
    }
    
    /**
     * Make proxy.
     * 
     * @param dao the dao
     * @param callback the callback
     * 
     * @return the t
     */
    protected T makeProxy(T dao, ErrorReportCallback callback){
        JdkRegexpMethodPointcut pc = new JdkRegexpMethodPointcut();

        pc.setPattern(methodMatchPattern);
          
        Advisor advisor = new DefaultPointcutAdvisor(pc, advice);
            
        ProxyFactory pf = new ProxyFactory(dao);
        pf.setProxyTargetClass(true);
        
        pf.addAdvisor(advisor);
        
        Object obj = pf.getProxy();
        return (T)obj;
    }
    
    /**
     * The Class ErrorReportingDao.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class ErrorReportingDao<T> {
        
        /** The dao. */
        private T dao;
        
        /** The error report callback. */
        private ErrorReportCallback errorReportCallback;
         
    }
    
    /**
     * The Class ErrorReportCallback.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    public static class ErrorReportCallback {
        
        /** The load validation errors. */
        List<LoadValidationError> loadValidationErrors = new ArrayList<LoadValidationError>();

        /**
         * Gets the load validation errors.
         * 
         * @return the load validation errors
         */
        public List<LoadValidationError> getLoadValidationErrors() {
            return loadValidationErrors;
        }

        /**
         * Sets the load validation errors.
         * 
         * @param loadValidationErrors the new load validation errors
         */
        public void setLoadValidationErrors(List<LoadValidationError> loadValidationErrors) {
            this.loadValidationErrors = loadValidationErrors;
        }
    }
}

