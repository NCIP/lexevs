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
package edu.mayo.informatics.indexer.api.generators;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.lucene.xmlParser.XMLErrorHandler;
import edu.mayo.informatics.indexer.lucene.xmlParser.XMLHandler;
import edu.mayo.informatics.indexer.lucene.xmlParser.exceptions.ParsingException;
import edu.mayo.informatics.indexer.lucene.xmlParser.exceptions.ReadingException;

/**
 * This class will parse XML into a lucene document. The XML must pass the
 * schema provided in the resources folder.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class DocumentFromXMLGenerator {
    private XMLReader reader_;
    private XMLHandler contentHandler_;

    private final Logger logger = Logger.getLogger("Indexer.Generators");

    public DocumentFromXMLGenerator(boolean validate) throws InternalErrorException {
        logger.info("Initializing the DocumentFromXMLGenerator");
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            if (validate) {
                factory.setValidating(true);
                factory.setFeature("http://apache.org/xml/features/validation/schema", true);
            } else {
                factory.setValidating(false);
            }

            SAXParser parser = factory.newSAXParser();
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
            parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaSource", new File(
                    "resources/document.xsd"));

            reader_ = parser.getXMLReader();
            contentHandler_ = new XMLHandler();
            reader_.setContentHandler(contentHandler_);
            reader_.setErrorHandler(new XMLErrorHandler());
        } catch (Exception e) {
            logger.error(e);
            throw new InternalErrorException("There was an error initializing the generator - " + e.toString());
        }
    }

    /**
     * Creates an indexer ready document from an xml file on disk.
     * 
     * @param file
     *            The file to index.
     * @return The document
     * @throws FileNotFoundException
     * @throws ParsingException
     * @throws ReadingException
     */
    public Document create(File file) throws FileNotFoundException, ParsingException, ReadingException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return create(new InputSource(reader));
    }

    /**
     * Creates an indexer ready document from a file whos located at
     * fileLocation.
     * 
     * @param fileLocation
     *            Where to read the file from
     * @return The document
     * @throws FileNotFoundException
     * @throws ParsingException
     * @throws ReadingException
     */
    public Document create(String fileLocation) throws FileNotFoundException, ParsingException, ReadingException {
        BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        return create(new InputSource(reader));
    }

    /**
     * Creates a indexer ready document from an InputSource (probably a stream)
     * 
     * @param source
     *            The source to index
     * @return the document
     * @throws ParsingException
     * @throws ReadingException
     */
    public Document create(InputSource source) throws ParsingException, ReadingException {
        try {
            reader_.parse(source);
        } catch (IOException e) {
            logger.error(e);
            throw new ReadingException("There was an error reading the input - " + e.toString());
        } catch (SAXException e) {
            logger.error(e);
            throw new ParsingException("There was an error parsing the input - " + e.toString());
        }
        return contentHandler_.document;
    }

}