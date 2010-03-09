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
    private static final long serialVersionUID = -5155019519725184687L;
    private String logId;

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    @LgClientSideSafe
    public String getLogId() {
        return this.logId;
    }

    public InternalException(String message) {
        super(message);
        logId = getLogger().error(message);
    }

    public InternalException(String message, Throwable cause) {
        super(message, cause);
        logId = getLogger().error(message, cause);
    }
}