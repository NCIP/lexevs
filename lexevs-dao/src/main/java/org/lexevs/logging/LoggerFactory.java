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