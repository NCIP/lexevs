
package org.lexevs.logging;

import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.lexevs.system.constants.SystemVariables;

/**
 * The Class LoggerFactory.
 * 
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoggerFactory {

    /** The logger. */
    private static LgLoggerIF logger;

    /**
     * Returns a lightweight logger if lightweight is true, otherwise returns
     * the SystemResourceService's logger.
     * 
     * @return the logger
     */
    public static synchronized LgLoggerIF getLogger() {

            if (logger == null) {
            	Logger loggerInstance = new Logger();
            	loggerInstance.setDebugEnabled(SystemVariables.isDebugEnabled());
                logger = loggerInstance;
            }
            
            return logger;    
    }
}