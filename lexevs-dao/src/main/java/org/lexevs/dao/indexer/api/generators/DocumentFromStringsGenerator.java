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

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.lexevs.dao.indexer.utility.Utility;

/**
 * This class assists in building 'proper' documents for the index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 */
public class DocumentFromStringsGenerator {
    private Document document_;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

    public DocumentFromStringsGenerator() {

    }

    public void startNewDocument(String documentIdentifier) throws RuntimeException {
        if (documentIdentifier == null || documentIdentifier.length() < 1) {
            throw new RuntimeException("Document Identifier.is required");
        }
        document_ = null;
        document_ = new Document();
        document_.add(new Field(org.lexevs.dao.indexer.lucene.Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD,
                documentIdentifier, Store.COMPRESS, Index.UN_TOKENIZED));
    }

    public Document getDocument() throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }
        return document_;
    }

    public void addTextField(String name, String value, boolean store, boolean index, boolean tokenize)
            throws RuntimeException {
        addTextField(name, value, store, false, index, tokenize);
    }

    public void addTextField(String name, String value, boolean store, boolean compress, boolean index, boolean tokenize)
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

        Field.Store storeC;
        Field.Index indexC;
        if (store && compress) {
            storeC = Store.COMPRESS;
        } else if (store) {
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

        document_.add(new Field(name, value, storeC, indexC));
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

        document_.add(new Field(name, Utility.padInt(value, '0', padToLength, true), storeC, indexC));

    }

    public void addDateField(String name, Date value, boolean store, boolean index) throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }

        if (value == null) {
            throw new RuntimeException("Value is required.");
        }

        if (name == null || name.length() < 1) {
            throw new RuntimeException("Name is required.");
        }

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

        document_.add(new Field(name, formatter.format(value), storeC, indexC));

    }

    public void addDateField(String name, long value, boolean store, boolean index) throws RuntimeException {
        if (document_ == null) {
            throw new RuntimeException("You must start a document first (call startNewDocument())");
        }

        if (name == null || name.length() < 1) {
            throw new RuntimeException("Name is required.");
        }

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

        document_.add(new Field(name, formatter.format(new Date(value)), storeC, indexC));
    }

    public String toString() {
        return document_.toString();
    }
}