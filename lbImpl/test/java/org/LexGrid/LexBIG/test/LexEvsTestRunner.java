
package org.LexGrid.LexBIG.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.junit.runners.model.InitializationError;
import org.lexevs.dao.test.BaseInMemoryLexEvsTest;
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