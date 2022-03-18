
package org.lexevs.exceptions;

/**
 * An exception to throw when things go wrong during startup of a class.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class InitializationException extends InternalException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7340972251523163860L;

    /**
     * Instantiates a new initialization exception.
     * 
     * @param message the message
     */
    public InitializationException(String message) {
        super(message);
    }

    /**
     * Instantiates a new initialization exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}