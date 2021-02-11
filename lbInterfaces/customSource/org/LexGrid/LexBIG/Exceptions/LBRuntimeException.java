
package org.LexGrid.LexBIG.Exceptions;

/**
 * Superclass for unchecked exceptions issued by the LexBIG runtime.
 * 
 * @version 1.0
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBRuntimeException extends RuntimeException {

	public LBRuntimeException(String message){
		super(message);
	}

	public LBRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}