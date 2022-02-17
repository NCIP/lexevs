
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when an unsupported operation is attempated against
 * a LexBIG service.
 */
public class LBUnsupportedOperationException extends LBInvocationException {

	public LBUnsupportedOperationException(String message, String logId) {
		super(message, logId);
	}
	
}