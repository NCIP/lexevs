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
package org.lexevs.logging.messaging.impl;

import org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF;

/**
 * Message director implementation that does nothing with the messages. Used in
 * JUnit testing.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class NullMessageDirector implements LgMessageDirectorIF {
    
    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#busy()
     */
    public void busy() {
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String)
     */
    public String error(String message) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#error(java.lang.String, java.lang.Throwable)
     */
    public String error(String message, Throwable sourceException) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String)
     */
    public String fatal(String message) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatal(java.lang.String, java.lang.Throwable)
     */
    public String fatal(String message, Throwable sourceException) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String)
     */
    public void fatalAndThrowException(String message) throws Exception {
        throw new Exception(message);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#fatalAndThrowException(java.lang.String, java.lang.Throwable)
     */
    public void fatalAndThrowException(String message, Throwable sourceException) throws Exception {
        throw new Exception(message, sourceException);
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#info(java.lang.String)
     */
    public String info(String message) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String)
     */
    public String warn(String message) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#warn(java.lang.String, java.lang.Throwable)
     */
    public String warn(String message, Throwable sourceException) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.LexGrid.LexBIG.Utility.logging.LgMessageDirectorIF#debug(java.lang.String)
     */
    public String debug(String message) {
        return null;
    }

}