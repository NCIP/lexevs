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
package edu.mayo.informatics.resourcereader.core.impl;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;
import org.lexevs.logging.messaging.impl.CachingMessageDirectorImpl;
import org.lexevs.logging.messaging.impl.CommandLineMessageDirector;

import edu.mayo.informatics.resourcereader.core.IF.TopResource;

/**
 * The class provides for a single logger to log messages
 * 
 * @author <A HREF="mailto:sharma.deepak2@mayo.edu">Deepak Sharma</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
@SuppressWarnings("static-access")
public class Resource implements TopResource {
    protected static LgMessageDirectorIF logger = new CachingMessageDirectorImpl(new CommandLineMessageDirector(
            "LexGridResourceReaderLog"));

    public static LgMessageDirectorIF getLogger() {
        return logger;
    }

    protected void setLogger(LgMessageDirectorIF logger) {
        if (logger != null)
            this.logger = logger;
    }
}