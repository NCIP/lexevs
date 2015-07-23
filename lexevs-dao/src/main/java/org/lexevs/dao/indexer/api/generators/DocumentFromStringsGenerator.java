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
package org.lexevs.dao.indexer.api.generators;

import java.util.Date;

import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.lexevs.dao.indexer.utility.Utility;

/**
 * This class assists in building 'proper' documents for the index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class DocumentFromStringsGenerator {
    private Document document_;
    public DocumentFromStringsGenerator() {

    }

    public void startNewDocument(String documentIdentifier) throws RuntimeException {
        if (documentIdentifier == null || documentIdentifier.length() < 1) {
            throw new RuntimeException("Document Identifier.is required");
        }
        document_ = null;
        document_ = new Document();
        
        document_.add(new StringField(org.lexevs.dao.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD,
                documentIdentifier, Store.YES));
    }

    public Document getDocument() throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }
        return document_;
    }

    public void addTextField(String name, String value, boolean store, boolean index, boolean tokenize)
            throws RuntimeException {
        
    	if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }

        if (name == null || name.length() < 1) {
            throw new RuntimeException("Name is required.");
        }

        if (value == null || value.length() < 1) {
            throw new RuntimeException("Value is required.");
        }

        Store storeC;
        if (store) {
            storeC = Store.YES;
        } else {
            storeC = Store.NO;
        }

        if (index && tokenize) {
        	document_.add(new TextField(name, value, storeC));
        } else if (index && !tokenize) {
        	document_.add(new StringField(name, value, storeC));
        }
        
    }

    public void addIntField(String name, int value, int padToLength, boolean store, boolean index)
            throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }

        if (value < 0) {
            throw new RuntimeException("Currently, intFields only handle Integers >= 0");
        }

        if (name == null || name.length() < 1) {
            throw new RuntimeException("Name is required.");
        }

        Store storeC;
        if (store) {
            storeC = Store.YES;
        } else {
            storeC = Store.NO;
        }

        document_.add(new IntField(name, Integer.valueOf(Utility.padInt(value, '0', padToLength, true)), storeC));

    }

    public void addDateField(String name, Date value, boolean store) throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }

        if (value == null) {
            throw new RuntimeException("Value is required.");
        }

        if (name == null || name.length() < 1) {
            throw new RuntimeException("Name is required.");
        }

        Store storeC;
        if (store) {
            storeC = Store.YES;
        } else {
            storeC = Store.NO;
        }

        document_.add(new StringField(name, DateTools.dateToString(value, DateTools.Resolution.DAY), storeC));

    }

    public void addDateField(String name, long value, boolean store, boolean index) throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }

        if (name == null || name.length() < 1) {
            throw new RuntimeException("Name is required.");
        }

        Store storeC;
        if (store) {
            storeC = Store.YES;
        } else {
            storeC = Store.NO;
        }

        document_.add(new StringField(name, DateTools.timeToString(value, DateTools.Resolution.DAY), storeC));
    }

    public String toString() {
        return document_.toString();
    }
}