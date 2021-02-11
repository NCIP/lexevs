
package edu.mayo.informatics.lexgrid.convert.exceptions;

/**
 * Error to be thrown when a connection can't be made.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5296 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class ConnectionFailure extends Exception {
    /**
	 * 
	 */
    private static final long serialVersionUID = -967019722301812041L;

    public ConnectionFailure(String reason, Exception e) {
        super(reason, e);
    }

    public ConnectionFailure(String reason) {
        super(reason);
    }

    public String toString() {
        Throwable cause = super.getCause();

        return this.getMessage() + " " + (cause == null ? "" : cause.toString());
    }

}