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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.lexevs.dao.test.BaseInMemoryLexEvsTest;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class LexEvsTestRunner.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsTestRunner extends SpringJUnit4ClassRunner {

    /**
     * Instantiates a new lex evs test runner.
     * 
     * @param clazz
     *            the clazz
     * @throws InitializationError
     *             the initialization error
     */
    public LexEvsTestRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
        this.checkForAllowedAnnotations(clazz);
        try {
        	System.setProperty(SystemVariables.ALL_IN_MEMORY_SYSTEM_VARIABLE, "true");
            BaseInMemoryLexEvsTest.initInMemory();
        } catch (Exception e) {
            throw new InitializationError(e);
        }
    }

    private void checkForAllowedAnnotations(Class<?> clazz) throws InitializationError {
        boolean classHasAnnotation =
                clazz.isAnnotationPresent(LoadContent.class) ||
                clazz.isAnnotationPresent(LoadContents.class);
        
        if(! classHasAnnotation){
            return;
        }

        for(Method method : clazz.getMethods()){
           if(method.isAnnotationPresent(LoadContent.class) ||
                   method.isAnnotationPresent(LoadContents.class)){
               throw new InitializationError("Cannot have a @LoadContent(s) Annotation on both the Class" +
                    " level and on a method level. It is allowed to be either on the Class or individual" +
                    " methods, but never both.");
           }
        }
    }
    
    @Target({ ElementType.METHOD, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface LoadContents {
        LoadContent[] value();
    }

    /**
     * The Interface LoadContent.
     * 
     * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
     */
    @Target({ ElementType.METHOD, ElementType.TYPE })
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
         * Content path. Use classpath:classpath/to/content for classpath
         * resources file:filepath/to/content for file-based resources
         * 
         * @return the string
         */
        String contentPath();
    }
}
