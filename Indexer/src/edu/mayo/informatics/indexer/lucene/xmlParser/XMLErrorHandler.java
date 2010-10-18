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
package edu.mayo.informatics.indexer.lucene.xmlParser;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This is my error handler class for parsing XML documents.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class XMLErrorHandler implements org.xml.sax.ErrorHandler {
    private final Logger logger = Logger.getLogger("Indexer.Index");

    public void warning(SAXParseException exception) {
        logger.warn("Warning - this was generated while parsing the XML", exception);
    }

    public void error(SAXParseException exception) throws SAXException {
        logger.error("There was an error parsing the XML", exception);
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        logger.error("There was an error parsing the XML", exception);
        throw exception;
    }
}