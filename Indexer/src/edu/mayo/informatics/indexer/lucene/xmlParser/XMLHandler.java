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
package edu.mayo.informatics.indexer.lucene.xmlParser;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import edu.mayo.informatics.indexer.utility.Utility;

/**
 * Parses an XML file into a lucene document.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class XMLHandler extends DefaultHandler {

    public Document document;
    private Attributes currentAttributes;
    private StringBuffer currentValue;

    public void startDocument() {
        document = new Document();
        currentValue = new StringBuffer();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        currentValue.setLength(0);

        if (localName.equals("document")) {
            document.add(new Field(edu.mayo.informatics.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD,
                    attributes.getValue("documentID"), Store.YES, Index.UN_TOKENIZED));
        } else {
            currentAttributes = attributes;
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (localName.equals("textField")) {
            // TODO add support for new compressed fields
            boolean store = Boolean.valueOf(currentAttributes.getValue("store")).booleanValue();
            boolean index = Boolean.valueOf(currentAttributes.getValue("index")).booleanValue();
            boolean tokenize = Boolean.valueOf(currentAttributes.getValue("tokenize")).booleanValue();

            Field.Store storeC;
            Field.Index indexC;
            if (store) {
                storeC = Store.YES;
            } else {
                storeC = Store.NO;
            }

            if (index && tokenize) {
                indexC = Field.Index.TOKENIZED;
            } else if (index && !tokenize) {
                indexC = Field.Index.UN_TOKENIZED;
            } else {
                indexC = Field.Index.NO;
            }
            document.add(new Field(currentAttributes.getValue("name"), currentValue.toString(), storeC, indexC));
        } else if (localName.equals("intField")) {
            boolean store = Boolean.valueOf(currentAttributes.getValue("store")).booleanValue();
            boolean index = Boolean.valueOf(currentAttributes.getValue("index")).booleanValue();

            Field.Store storeC;
            Field.Index indexC;
            if (store) {
                storeC = Store.YES;
            } else {
                storeC = Store.NO;
            }

            if (index) {
                indexC = Field.Index.UN_TOKENIZED;
            } else {
                indexC = Field.Index.NO;
            }
            document.add(new Field(currentAttributes.getValue("name"), Utility.padStringBuffer(currentValue, '0',
                    Integer.parseInt(currentAttributes.getValue("padTo")), true), storeC, indexC));

        } else if (localName.equals("dateField")) {
            boolean store = Boolean.valueOf(currentAttributes.getValue("store")).booleanValue();
            boolean index = Boolean.valueOf(currentAttributes.getValue("index")).booleanValue();

            Field.Store storeC;
            Field.Index indexC;
            if (store) {
                storeC = Store.YES;
            } else {
                storeC = Store.NO;
            }

            if (index) {
                indexC = Field.Index.UN_TOKENIZED;
            } else {
                indexC = Field.Index.NO;
            }
            document.add(new Field(currentAttributes.getValue("name"), Utility.replaceStringsInString(currentValue,
                    "-", ""), storeC, indexC));
        }
    }

    public void characters(char[] ch, int start, int length) {
        currentValue.append(ch, start, length);
    }
}