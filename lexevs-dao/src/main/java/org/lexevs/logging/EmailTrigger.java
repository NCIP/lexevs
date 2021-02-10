
package org.lexevs.logging;

import java.util.Date;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

/**
 * Need to implement this class to make the SMTP appender work the way I want it
 * to.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class EmailTrigger implements TriggeringEventEvaluator {
    
    /** The timestamps. */
    LRUMap timestamps = new LRUMap(5); // Going to keep track of the last 5
                                       // timestamps

    // that an email was sent. Do some rate limiting this way.

    /* (non-Javadoc)
                                        * @see org.apache.log4j.spi.TriggeringEventEvaluator#isTriggeringEvent(org.apache.log4j.spi.LoggingEvent)
                                        */
                                       public boolean isTriggeringEvent(LoggingEvent event) {
        long now = System.currentTimeMillis();
        if (timestamps.size() == 5) {
            // If 5 errors have been emailed, and it has been less than 5 minute
            // since the first one
            // went out, stop e-mailing until at least 5 minutes have passed.
            if ((now - ((Date) timestamps.firstKey()).getTime()) < (5 * 60 * 1000)) {
                return false;
            }
        }
        timestamps.put(new Date(now), null);
        return true;
    }

}