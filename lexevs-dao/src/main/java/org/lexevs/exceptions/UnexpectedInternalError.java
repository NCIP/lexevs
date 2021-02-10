
package org.lexevs.exceptions;

/**
 * An exception to throw when something unplanned goes wrong.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class UnexpectedInternalError extends InternalException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 9156881998271855018L;

    /**
     * Instantiates a new unexpected internal error.
     * 
     * @param message the message
     */
    public UnexpectedInternalError(String message) {
        super(message);
    }

    /**
     * Instantiates a new unexpected internal error.
     * 
     * @param message the message
     * @param cause the cause
     */
    public UnexpectedInternalError(String message, Throwable cause) {
        super(message, cause);
    }
}