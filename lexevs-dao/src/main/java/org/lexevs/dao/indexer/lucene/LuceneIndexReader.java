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
package org.lexevs.dao.indexer.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.lexevs.dao.index.indexer.LuceneLoaderCode;


/**
 * The wrapper for the Index Reader.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class LuceneIndexReader {
	//At least some of this class doesn't seem to be surfaced. We may be able to remove.  Just the same I pulled in
	//Some classes to do some of the work IndexReader can't do anymore.  See IndexWriter use here.
    private final File location_;

    private IndexReader indexReader_;
    private IndexWriter indexWriter_;
    private boolean useInMemoryIndex_ = false;

    private final Logger logger = Logger.getLogger("Indexer.Index");

    public LuceneIndexReader(File location) throws RuntimeException, IOException {
        this.location_ = location;
        useInMemoryIndex_ = false;
        openIndex();
    }

    public LuceneIndexReader(File location, boolean useInMemoryIndex) throws RuntimeException {
        this.location_ = location;
        useInMemoryIndex_ = useInMemoryIndex;
        openIndex();
    }

    public boolean isInMemory() {
        return useInMemoryIndex_;
    }

    private void openIndex() throws RuntimeException {
        try {
            if (useInMemoryIndex_) {
                indexReader_ = DirectoryReader.open(new RAMDirectory());
            } else {
            	Path path = Paths.get(location_.toURI());
            	Directory directory = new MMapDirectory(path);
                indexReader_ = DirectoryReader.open(directory);
            }
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error opening the index reader. " + e.getMessage());
        }
    }

    public void delete(String uniqueDocumentId) throws RuntimeException {
    	//TODO figure out what this is intended to do.  If we are deleting an entire entity -
    	//Then we should be deleting a set of documents.  Even in the old version.
        try {
            indexWriter_.deleteDocuments(new Term(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD, uniqueDocumentId));
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error removing the document. " + e.getMessage());
        }
    }

    public IndexReader getBaseIndexReader() {
        return indexReader_;
    }

    public void  delete(String field, String uniqueDocumentId) throws RuntimeException {
        try {
           indexWriter_.deleteDocuments(new Term(field, uniqueDocumentId));
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error removing the document. " + e.getMessage());
        }
    }

    public void close() throws RuntimeException {
        logger.info("Closing the index reader");
        try {
            indexReader_.close();
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error closing the index writer. " + e.getMessage());
        }

    }

    public Collection searchableFields() {
    	//TODO remove this method if we can.  Doesn't look like it gets surfaced anywhere.
        Collection temp = null;
//        temp = indexReader_.getFieldNames(IndexReader.FieldOption.ALL);
//        temp.remove(""); // for some reason, there is an empty string field in
        // there...

        return temp;
    }

    public int maxDoc() {
        return indexReader_.maxDoc();
    }

    public Document document(int docIndex) throws RuntimeException {
        try {
            return indexReader_.document(docIndex);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error closing the index writer. " + e.getMessage());
        }
    }

    public boolean upToDate() {
    	//Doesn't look like we use this either TODO drop method.
//        try {
//            if (IndexReader.getCurrentVersion(this.location_) != this.indexReaderLastModifiedDate)
//                return false;
//            else
//                return true;
//        } catch (IOException e) {
//            logger.error("Error reading index date" + e);
//            return false;
//        }
    	return false;
    }

    public void reopen() throws RuntimeException {
        this.close();
        this.openIndex();
    }

}