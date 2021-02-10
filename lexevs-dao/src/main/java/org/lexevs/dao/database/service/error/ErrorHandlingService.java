
package org.lexevs.dao.database.service.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface ErrorHandlingService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ErrorHandlingService {

	/**
	 * Match all methods.
	 * 
	 * @return true, if successful
	 */
	boolean matchAllMethods() default false;

	/**
	 * Match annotated methods.
	 * 
	 * @return the class<?>[]
	 */
	Class<?>[] matchAnnotatedMethods() default {};
}