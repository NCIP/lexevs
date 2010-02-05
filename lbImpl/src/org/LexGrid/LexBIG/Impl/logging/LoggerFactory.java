/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Impl.logging;

import org.LexGrid.LexBIG.Impl.dataAccess.ResourceManager;

/**
 * @author <A HREF="mailto:rokickik@mail.nih.gov">Konrad Rokicki</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class LoggerFactory {

    private static boolean lightweight = false;

    private static LgLoggerIF logger;

    public static void setLightweight(boolean lightweight) {
        LoggerFactory.lightweight = lightweight;
    }

    /**
     * Returns a lightweight logger if lightweight is true, otherwise returns
     * the ResourceManager's logger.
     * 
     * @return
     */
    public static LgLoggerIF getLogger() {

        if (lightweight) {
            if (logger == null) {
                logger = new SimpleLogger();
            }
            return logger;
        } else {
            return ResourceManager.instance().getLogger();
        }
    }
}