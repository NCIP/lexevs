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
package org.LexGrid.LexBIG.Impl.helpers;

import org.LexGrid.annotations.LgClientSideSafe;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.xml.sax.SAXParseException;

/**
 * Simple handler that prints all encountered errors and retains the most recent
 * encountered error or fatal error.
 * 
 * @author <A HREF="mailto:scott.bauer@mayo.edu">Scott Bauer</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 */
public class SAXPrintHandler implements org.xml.sax.ErrorHandler {
    private SAXParseException err_ = null;

    private LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }

    /**
     * Construct a new handler printing to System.out as target.
     */
    public SAXPrintHandler() {
        super();
    }

    /**
     * Returns the most recent error or fatal error.
     * 
     * @return SAXParseException
     */
    @LgClientSideSafe
    public SAXParseException getError() {
        return err_;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException) This
     * corresponds to the definition of "error" in section 1.2 of the W3C XML
     * 1.0
     */
    @LgClientSideSafe
    public void error(SAXParseException sap) throws org.xml.sax.SAXException {
        getLogger().loadLogError("PARSING ERROR", sap);
        err_ = sap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
     * The application must assume that the document is unusable after the
     * parser has invoked this method
     */
    @LgClientSideSafe
    public void fatalError(SAXParseException sap) throws org.xml.sax.SAXException {
        getLogger().loadLogError("PARSING ERROR", sap);
        err_ = sap;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException) SAX
     * parsers will use this method to report conditions that are not errors or
     * fatal errors as defined by the XML recommendation.
     */
    @LgClientSideSafe
    public void warning(SAXParseException sap) throws org.xml.sax.SAXException {
        getLogger().loadLogWarn("PARSING ERROR", sap);
    }

}