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
package org.LexGrid.LexBIG.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Extensions.Load.Loader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.BaseLoader;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.lexevs.dao.database.operation.LexEvsDatabaseOperations;
import org.lexevs.locator.LexEvsServiceLocator;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * The Class LexEvsTestRunner.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsTestRunner extends BlockJUnit4ClassRunner {

	/**
	 * Instantiates a new lex evs test runner.
	 *
	 * @param clazz the clazz
	 * @throws InitializationError the initialization error
	 */
	public LexEvsTestRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
	}
	
	

	/* (non-Javadoc)
	 * @see org.junit.runners.BlockJUnit4ClassRunner#runChild(org.junit.runners.model.FrameworkMethod, org.junit.runner.notification.RunNotifier)
	 */
	@Override
	protected void runChild(FrameworkMethod method, RunNotifier notifier){
	    LoadContent content = method.getAnnotation(LoadContent.class);
	    
	    LexEvsDatabaseOperations 
	        dbOps = LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations();
	     try {
	    	dbOps.createAllTables();
        } catch (Exception e1) {
            //
        }

	    if(content != null){
	        try {
	            load(content);
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    super.runChild(method, notifier);
	    
	    LexEvsServiceLocator.getInstance().getLexEvsDatabaseOperations().dropAllTables();
	}
	
	/**
	 * The Interface LoadContent.
	 *
	 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
	 */
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface LoadContent {
		
		/**
		 * The Loader Extension Name.
		 * 
		 * Defaultes to the LexEVS XML Loader.
		 *
		 * @return the string
		 */
		String loader() default "LexGrid_Loader";
		
		/**
		 * Content path.
		 * Use
		 *    classpath:classpath/to/content for classpath resources
		 *    file:filepath/to/content for file-based resources
		 *
		 * @return the string
		 */
		String contentPath();
	}
	
    /**
     * Load.
     *
     * @param content the content
     * @throws Exception the exception
     */
    protected void load(LoadContent content) throws Exception {
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance().getServiceManager(null);
        
        Resource resource = this.getResource(content.contentPath());

        Loader loader =  lbsm.getLoader(content.loader());
        
        loader.getOptions().getBooleanOption(BaseLoader.ASYNC_OPTION).setOptionValue(false);
        
        loader.load(resource.getURI());

        assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
        assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

        lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

        lbsm.setVersionTag(loader.getCodingSchemeReferences()[0], LBConstants.KnownTags.PRODUCTION.toString());
    }
    
    /**
     * Inits the url handler.
     */
    public Resource getResource(String path){
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader(
                LexEvsServiceLocator.getInstance().getSystemResourceService().getClassLoader());
        
        return resourceLoader.getResource(path);
    }
}
