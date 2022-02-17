
package org.LexGrid.LexBIG.Exceptions;

/**
 * Superclass for unchecked exceptions issued by the LexBIG runtime.
 * <p>
 * Note: Runtime exceptions are used to indicate a programming problem
 * detected by the runtime system or inappropriate use of the API.
 * Intended to be used sparingly in cases where the cost of requesting
 * a check on the exception exceeds the benefit of catching
 * or declaring it.
 * 
 * @version 1.0
 * @created 27-Jan-2006 9:19:38 PM
 */
public class LBRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 4347639326985296334L;

	public LBRuntimeException(String message){
		super(message);
	}

	public LBRuntimeException(String message, Throwable cause){
		super(message, cause);
	}

}