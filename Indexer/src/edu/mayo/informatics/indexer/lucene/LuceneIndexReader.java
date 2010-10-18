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
package edu.mayo.informatics.indexer.lucene;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.RAMDirectory;

import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;

/**
 * The wrapper for the Index Reader.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class LuceneIndexReader {
    private final File location_;

    private IndexReader indexReader_;
    private long indexReaderLastModifiedDate;
    private boolean useInMemoryIndex_ = false;

    private final Logger logger = Logger.getLogger("Indexer.Index");

    public LuceneIndexReader(File location) throws InternalIndexerErrorException {
        this.location_ = location;
        useInMemoryIndex_ = false;
        openIndex();
    }

    public LuceneIndexReader(File location, boolean useInMemoryIndex) throws InternalIndexerErrorException {
        this.location_ = location;
        useInMemoryIndex_ = useInMemoryIndex;
        openIndex();
    }

    public boolean isInMemory() {
        return useInMemoryIndex_;
    }

    private void openIndex() throws InternalIndexerErrorException {
        try {
            if (useInMemoryIndex_) {
                indexReader_ = IndexReader.open(new RAMDirectory(location_));
            } else {
                indexReader_ = IndexReader.open(location_);
            }
            this.indexReaderLastModifiedDate = IndexReader.getCurrentVersion(location_);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error opening the index reader. " + e.getMessage());
        }
    }

    public int delete(String uniqueDocumentId) throws InternalIndexerErrorException {
        try {
            return indexReader_.deleteDocuments(new Term(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD, uniqueDocumentId));
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error removing the document. " + e.getMessage());
        }
    }

    public IndexReader getBaseIndexReader() {
        return indexReader_;
    }

    public int delete(String field, String uniqueDocumentId) throws InternalIndexerErrorException {
        try {
            return indexReader_.deleteDocuments(new Term(field, uniqueDocumentId));
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error removing the document. " + e.getMessage());
        }
    }

    public void close() throws InternalIndexerErrorException {
        logger.info("Closing the index reader");
        try {
            indexReader_.close();
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error closing the index writer. " + e.getMessage());
        }

    }

    public Collection searchableFields() {
        Collection temp;
        temp = indexReader_.getFieldNames(IndexReader.FieldOption.ALL);
        temp.remove(""); // for some reason, there is an empty string field in
        // there...

        return temp;
    }

    public int maxDoc() {
        return indexReader_.maxDoc();
    }

    public Document document(int docIndex) throws InternalIndexerErrorException {
        try {
            return indexReader_.document(docIndex);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error closing the index writer. " + e.getMessage());
        }
    }

    public boolean upToDate() {
        try {
            if (IndexReader.getCurrentVersion(this.location_) != this.indexReaderLastModifiedDate)
                return false;
            else
                return true;
        } catch (IOException e) {
            logger.error("Error reading index date" + e);
            return false;
        }
    }

    public void reopen() throws InternalIndexerErrorException {
        this.close();
        this.openIndex();
    }

}