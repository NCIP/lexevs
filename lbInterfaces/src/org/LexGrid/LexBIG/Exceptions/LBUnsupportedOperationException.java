
package org.LexGrid.LexBIG.Exceptions;

/**
 * The exception to throw when an unsupported operation is attempted against
 * a LexBIG service.
 */
public class LBUnsupportedOperationException extends LBInvocationException {
	private static final long serialVersionUID = -7716954955982025288L;

	public LBUnsupportedOperationException(String message, String logId) {
		super(message, logId);
	}
	
}