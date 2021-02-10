
package org.LexGrid.LexBIG.Exceptions;

/**
 * Thrown when a resource required by the requested LexBIG operation
 * cannot be located or resolved.
 * 
 */
public class LBResourceUnavailableException extends LBException {
	private static final long serialVersionUID = -690663064337397771L;

	public LBResourceUnavailableException(String message, Throwable cause) {
		super(message, cause);
	}

	public LBResourceUnavailableException(String message) {
		super(message);
	}

}