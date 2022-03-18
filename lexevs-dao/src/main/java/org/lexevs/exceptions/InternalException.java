
package org.lexevs.exceptions;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.logging.LoggerFactory;

/**
 * Parent class for the all internal exceptions..
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class InternalException extends Exception {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5155019519725184687L;
    
    /** The log id. */
    private String logId;

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Gets the log id.
     * 
     * @return the log id
     */
    @LgClientSideSafe
    public String getLogId() {
        return this.logId;
    }

    /**
     * Instantiates a new internal exception.
     * 
     * @param message the message
     */
    public InternalException(String message) {
        super(message);
        logId = getLogger().error(message);
    }

    /**
     * Instantiates a new internal exception.
     * 
     * @param message the message
     * @param cause the cause
     */
    public InternalException(String message, Throwable cause) {
        super(message, cause);
        logId = getLogger().error(message, cause);
    }
}