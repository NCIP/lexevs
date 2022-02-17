
package org.LexGrid.LexBIG.Exceptions;

/**
 * Superclass for checked exceptions issued by the LexBIG runtime.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 * @created 27-Jan-2006 9:19:37 PM
 */
public class LBException extends Exception {

	public LBException(String message){
		super(message);
	}

	public LBException(String message, Throwable cause){
		super(message, cause);
	}

}