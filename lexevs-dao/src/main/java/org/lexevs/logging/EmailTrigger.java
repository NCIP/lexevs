/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.logging;

import java.util.Date;

import org.apache.commons.collections.map.LRUMap;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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