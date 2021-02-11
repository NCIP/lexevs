
package org.lexevs.dao.database.service.error;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface DatabaseErrorIdentifier.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DatabaseErrorIdentifier {

	/**
	 * Error code.
	 * 
	 * @return the string
	 */
	String errorCode();
}