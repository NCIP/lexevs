
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