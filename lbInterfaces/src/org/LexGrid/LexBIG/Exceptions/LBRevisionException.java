
package org.LexGrid.LexBIG.Exceptions;



public class LBRevisionException extends LBException {

/**
	 * 
	 */
private static final long serialVersionUID = 6335488236350630144L;

	public LBRevisionException(String message, Throwable cause) {
		super(message, cause);
	}

	public LBRevisionException(String message) {
		super(message);
	}
	
	public LBRevisionException(Throwable cause) {
		super(cause.getMessage(), cause);
	}

}