
package org.LexGrid.LexBIG.Exceptions;

/**
 * Superclass for checked exceptions declared and thrown by the LexBIG runtime.
 */
public class LBException extends Exception {
	private static final long serialVersionUID = -7544914453590815080L;

	public LBException(String message){
		super(message);
	}

	public LBException(String message, Throwable cause){
		super(message, cause);
	}

}