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
package edu.mayo.informatics.lexgrid.convert.formats;

import edu.mayo.informatics.lexgrid.convert.exceptions.ConnectionFailure;
import edu.mayo.informatics.lexgrid.convert.exceptions.UnexpectedError;

/**
 * Defines the interface for input formats.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: 7198 $ checked in on $Date: 2008-02-15
 *          18:08:36 +0000 (Fri, 15 Feb 2008) $
 */
public interface InputFormatInterface {
    /**
     * Get the supported Output formats for this input format.
     * 
     * @return
     */
    public String[] getSupportedOutputFormats();

    /**
     * Get the description of the input type.
     * 
     * @return
     */
    public String getDescription();

    /**
     * Get the additional options that must be supplied before the reading can
     * happen.
     * 
     * @return
     */
    public Option[] getOptions();

    /**
     * Get a summary of the connection parameters.
     * 
     * @return the summary.
     */
    public String getConnectionSummary();

    /**
     * See if the provided parameters make a valid connection
     * 
     * @return An optional warning about the connection - for example, SQL
     *         implementations may want to return a warning if the existing sql
     *         tables are a different version than what is expected.
     * @throws ConnectionFailure
     *             If a connection can't be made.
     */
    public String testConnection() throws ConnectionFailure;

    public String[] getAvailableTerminologies() throws ConnectionFailure, UnexpectedError;
}