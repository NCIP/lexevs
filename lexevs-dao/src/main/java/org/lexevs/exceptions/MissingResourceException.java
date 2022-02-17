
package org.lexevs.exceptions;

/**
 * An exception to throw when something that is required is not present.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class MissingResourceException extends InternalException {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -3162541086854760339L;

    /**
     * Instantiates a new missing resource exception.
     * 
     * @param message the message
     */
    public MissingResourceException(String message) {
        super(message);
    }

    /**
     * Instantiates a new missing resource exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public MissingResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}