
package edu.mayo.informatics.lexgrid.convert.exceptions;

/**
 * Error to be thrown when something unexpected happens.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 5296 $ checked in on $Date: 2007-05-16
 *          21:55:43 +0000 (Wed, 16 May 2007) $
 */
public class UnexpectedError extends Exception {
    /**
	 * 
	 */
    private static final long serialVersionUID = -1736890144726913511L;

    public UnexpectedError(String reason, Exception e) {
        super(reason, e);
    }

    public UnexpectedError(String reason) {
        super(reason);
    }

    public String toString() {
        Throwable cause = super.getCause();

        return this.getMessage() + " " + (cause == null ? "" : cause.toString());
    }

}