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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

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
import edu.mayo.informatics.indexer.lucene.xmlParser.exceptions.TransformException;

/**
 * This class will transform an xml document with the provided xsd into a lucene
 * document. The resulting XML must pass the schema provided in the resources
 * folder.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class DocumentFromXSLGenerator {
    XMLReader reader_;
    private XMLHandler contentHandler_;
    private Transformer transformer_;
    PipedInputStream in_;
    private PipedOutputStream out_;
    private ParsingThread parsingThread_;

    private final Logger logger = Logger.getLogger("Indexer.Generators");

    /**
     * Construct this document creator from a streamSource that is the xsd file
     * 
     * @param xslFile
     *            The file to perform the transfrom with
     * @param validate
     *            whether or not to validate the resulting transformation
     * @throws InternalErrorException
     *             If something goes wrong while initializing
     */
    public DocumentFromXSLGenerator(StreamSource xslFile, boolean validate) throws InternalErrorException {
        initialize(xslFile, validate);
    }

    /**
     * Construct this document creator from a xsd file at the location of
     * xslFileLocation
     * 
     * @param xslFileLocation
     *            The file to perform transforms with
     * @param validate
     *            whether or not to validate the resulting xml
     * @throws InternalErrorException
     *             If something goes wrong initializing
     */
    public DocumentFromXSLGenerator(String xslFileLocation, boolean validate) throws InternalErrorException {
        File temp = new File(xslFileLocation);
        initialize(new StreamSource(temp), validate);
    }

    /**
     * Construct this document creator from a xsd file.
     * 
     * @param xslFile
     *            The file to perform transforms with
     * @param validate
     *            whether or not to validate the resulting xml
     * @throws InternalErrorException
     *             If something goes wrong initializing
     */
    public DocumentFromXSLGenerator(File xslFile, boolean validate) throws InternalErrorException {
        initialize(new StreamSource(xslFile), validate);
    }

    private void initialize(StreamSource xslFile, boolean validate) throws InternalErrorException {
        logger.info("Initializing the DocumentFromXSLGenerator.");
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

            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory
                    .newInstance();

            transformer_ = transformerFactory.newTransformer(xslFile);
            parsingThread_ = new ParsingThread();

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
    public Document create(File file) throws FileNotFoundException, ParsingException, ReadingException,
            TransformException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return create(new StreamSource(reader));
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
    public Document create(String fileLocation) throws FileNotFoundException, ParsingException, ReadingException,
            TransformException {
        BufferedReader reader = new BufferedReader(new FileReader(fileLocation));
        return create(new StreamSource(reader));
    }

    /**
     * This class is used to do the parsing and transforming at the same time
     * with seperate threads.
     */
    private class ParsingThread implements Runnable {
        public Exception exception = null;

        public void run() {
            exception = null;
            try {
                reader_.parse(new InputSource(in_));
            } catch (IOException e1) {
                exception = e1;
            } catch (SAXException e1) {
                exception = e1;
            }

        }
    }

    /**
     * Creates a indexer ready document from an InputSource (probably a stream)
     * 
     * @param source
     *            The source to index
     * @return the document
     * @throws ParsingException
     * @throws ReadingException
     * @throws TransformException
     */
    public Document create(StreamSource source) throws ParsingException, ReadingException, TransformException {
        try {
            out_ = new PipedOutputStream();
            in_ = new PipedInputStream(out_);

            Thread thread = new Thread(parsingThread_);
            thread.start();

            transformer_.transform(source, new StreamResult(out_));

            out_.flush();
            out_.close();

            thread.join();
            if (parsingThread_.exception != null) {
                throw new ParsingException("There was an error parsing the input - "
                        + parsingThread_.exception.toString());
            }
        } catch (IOException e) {
            if (parsingThread_.exception != null) {
                throw new ParsingException("There was an error parsing the input - "
                        + parsingThread_.exception.toString());
            }

            throw new ReadingException("There was an error reading the input - " + e.toString());
        } catch (TransformerException e) {
            if (parsingThread_.exception != null) {
                throw new ParsingException("There was an error parsing the input - "
                        + parsingThread_.exception.toString());
            }

            throw new TransformException("There was an error transforming the input - " + e.toString());
        } catch (InterruptedException e) {
            if (parsingThread_.exception != null) {
                throw new ParsingException("There was an error parsing the input - "
                        + parsingThread_.exception.toString());
            }

            throw new ParsingException("There was an error parsing the input - " + e.toString());
        }
        return contentHandler_.document;
    }
}