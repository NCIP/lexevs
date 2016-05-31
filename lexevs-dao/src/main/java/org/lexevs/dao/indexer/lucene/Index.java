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

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

/**
 * This is an abstracted view of an lucene index.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class Index {
    private final Logger logger = Logger.getLogger("Indexer.Index");
    public final static String UNIQUE_DOCUMENT_IDENTIFIER_FIELD = "UNIQUE_DOCUMENT_IDENTIFIER_FIELD";

    private final File location_;
    private LuceneIndexWriterInterface indexWriter_ = null;
    private LuceneIndexReader indexReader_ = null;
    private Analyzer analyzer_;

    /**
     * Opens an index using a provided analyzer.
     * 
     * @param location
     *            Location on disk to index to.
     * @param analyzer
     *            The analyzer to use while indexing
     */
    public Index(File location, Analyzer analyzer) {
        this.location_ = location;
        this.analyzer_ = analyzer;

        logger.info("Opening an index with a provided analyzer on " + location.getAbsolutePath());
    }

    /**
     * This constructor will open an index using a StandardAnalyzer.
     * 
     * @param location
     *            Location on the disk to create the index
     * @param stopWords
     *            Optional list of stopwords (words not to index) to use in the
     *            StandardAnalyzer.
     */
    public Index(File location, CharArraySet stopWords) {
        this.location_ = location;
        this.analyzer_ = new StandardAnalyzer(stopWords);

        logger.info("Opening an index with a standard analyzer on " + location.getAbsolutePath());
    }

    /**
     * Open the index for adding new documents. Use for small updates. Use a
     * Batch writer for large updates.
     * 
     * @param clearContents
     *            True to erase current contents, false to append to them
     * @throws RuntimeException
     */
    public void openFSIndexWriter(boolean clearContents) throws RuntimeException {
        if (indexWriter_ == null) {
            logger.info("Opening a FS Index Writer on " + location_.getAbsolutePath());
            indexWriter_ = new LuceneFSIndexWriter(location_, clearContents, analyzer_);
        } else {
            throw new RuntimeException("Their is already an index writer open.");
        }
    }
    
    /**
     * Closes the currently opened indexWriter.
     * 
     * @throws RuntimeException
     */
    public void closeIndexWriter() throws RuntimeException {
        if (indexWriter_ != null) {
            logger.info("Closing the IndexWriter " + location_.getAbsolutePath());
            indexWriter_.close();
            indexWriter_ = null;
        } else {
            logger.info("The index writer was already closed: " + location_.getAbsolutePath());
        }
    }

    public void openIndexReader() throws RuntimeException {
        openIndexReader(false);
    }

    public void openIndexReader(boolean useInMemoryIndex) throws RuntimeException {
        if (indexReader_ == null) {
            // If the index does not exist, create it.
//            if (!IndexReader.indexExists(location_)) {
//                logger.info("Index does not yet exist, creating a new (blank) index");
        	//This doesn't work this way anymore.  
                try {
                    openFSIndexWriter(true);
                    closeIndexWriter();
                } catch (RuntimeException e) {
                    // should be impossible
                }

//            }
            logger.info("Opening a IndexReader on " + location_.getAbsolutePath());
            indexReader_ = new LuceneIndexReader(location_, useInMemoryIndex);
        } else if (indexReader_.isInMemory() != useInMemoryIndex) {
            logger.info("Reopening index reader to match up with inMemory request status. ");
            indexReader_.close();
            indexReader_ = null;
            openIndexReader(useInMemoryIndex);
        }
    }

    public LuceneIndexReader getIndexReader() throws RuntimeException {
        return getIndexReader(false);
    }

    public LuceneIndexReader getIndexReader(boolean useInMemoryIndex) throws RuntimeException {
        if (indexReader_ == null) {
            openIndexReader(useInMemoryIndex);
        }
        if (indexReader_.isInMemory() != useInMemoryIndex) {
            indexReader_.close();
            indexReader_ = null;
            openIndexReader(useInMemoryIndex);
        }
        return indexReader_;
    }

    public void closeIndexReader() throws RuntimeException {
        if (indexReader_ != null) {
            logger.info("Closing the IndexReader " + location_.getAbsolutePath());
            indexReader_.close();
            indexReader_ = null;
        } else {
            logger.info("The index reader was already closed: " + location_.getAbsolutePath());
        }
    }

    /**
     * Adds a document to the currently open indexWriter.
     * 
     * @param document
     *            The document to add to the index.
     * @throws RuntimeException
     */
    public void addDocument(Document document) throws RuntimeException {
        boolean iOpened = false;
        if (indexWriter_ == null) {
            iOpened = true;
            try {
                this.openFSIndexWriter(false);
            } catch (RuntimeException e) {
                // can't happen
            }
        }
        indexWriter_.addDocument(document);

        if (iOpened) {
            this.closeIndexWriter();
        }
    }

    /**
     * Adds a document to the currently open indexWriter.
     * 
     * @param document
     *            The document to add to the index.
     * @param analyzer
     *            The analyzer to use
     * @throws RuntimeException
     */
    public void addDocument(Document document, Analyzer analyzer) throws RuntimeException {
        boolean iOpened = false;
        if (indexWriter_ == null) {
            iOpened = true;
            try {
                this.openFSIndexWriter(false);
            } catch (RuntimeException e) {
                // can't happen
            }
        }
        
        //Analyzer is added during init.  
///        indexWriter_.addDocument(document, analyzer);
        indexWriter_.addDocument(document);

        if (iOpened) {
            this.closeIndexWriter();
        }
    }

    public void removeDocument(String uniqueDocumentIdentifier) throws RuntimeException {
        boolean iOpened = false;
        if (indexReader_ == null) {
            this.openIndexReader();
            iOpened = true;
        }
        if (indexWriter_ != null) {
            if (iOpened) {
                this.closeIndexReader();
            }
            throw new RuntimeException(
                    "You cannot delete a document while a IndexWriter is open.  Close the IndexWriter, and then try again.");
        }

       indexReader_.delete(uniqueDocumentIdentifier);

        if (iOpened) {
            this.closeIndexReader();
        }
        //return docsRemoved;
    }

    public void removeDocument(String field, String fieldValue) throws RuntimeException {
        boolean iOpened = false;
        if (indexReader_ == null) {
            this.openIndexReader();
            iOpened = true;

        }
        if (indexWriter_ != null) {
            if (iOpened) {
                this.closeIndexReader();
            }
            throw new RuntimeException(
                    "You cannot delete a document while a IndexWriter is open.  Close the IndexWriter, and then try again.");
        }

        indexReader_.delete(field, fieldValue);

        if (iOpened) {
            this.closeIndexReader();
        }
      
    }

    /**
     * How many documents to write out per temporary index. Used for
     * performance/controlling open file handles. Only used on writers.
     * 
     * @param i
     *            - How many docs to add to the index before opening a new
     *            temporary index.
     */
    public void setDocsPerTempIndex(int i) throws RuntimeException {
        if (indexWriter_ != null) {
            indexWriter_.setDocsPerTempIndex(i);
        } else {
            throw new RuntimeException("This method can only be called if a index writer is open.");
        }

    }

    /**
     * Lucene will truncate fields longer than this. Only used on writers
     * 
     * @param i
     *            The max length that a field can be.
     */
    public void setMaxFieldLength(int i) throws RuntimeException {
        if (indexWriter_ != null) {
            indexWriter_.setMaxFieldLength(i);
        } else {
            throw new RuntimeException("This method can only be called if a index writer is open.");
        }
    }

    /**
     * See the lucene documentation. Probably unneeded. Only used on writers.
     * 
     * @param i
     */
    public void setMaxBufferedDocs(int i) throws RuntimeException {
        if (indexWriter_ != null) {
            indexWriter_.setMaxBufferedDocs(i);
        } else {
            throw new RuntimeException("This method can only be called if a index writer is open.");
        }
    }

    /**
     * See the lucene documentation. Probably unneeded. Only used on writers.
     * 
     * @param i
     */
    public void setMaxMergeDocs(int i) throws RuntimeException {
        if (indexWriter_ != null) {
            indexWriter_.setMaxMergeDocs(i);
        } else {
            throw new RuntimeException("This method can only be called if a index writer is open.");
        }
    }

    /**
     * How many documents to add in memory before writing to the index. Has
     * large affects on performance. Only used on writers.
     * 
     * @param i
     *            How many docs to add before writing.
     */
    public void setMergeFactor(int i) throws RuntimeException {
        if (indexWriter_ != null) {
            indexWriter_.setMergeFactor(i);
        } else {
            throw new RuntimeException("This method can only be called if a index writer is open.");
        }
    }

    /**
     * Whether or not to use the new compound file format. Reduces the number of
     * open files, but also reduces the indexing performance.
     * 
     * @param bool
     * @throws RuntimeException
     */
    public void setUseCompoundFile(boolean bool) throws RuntimeException {
        if (indexWriter_ != null) {
            indexWriter_.setUseCompoundFile(bool);
        } else {
            throw new RuntimeException("This method can only be called if a index writer is open.");
        }
    }

    /**
     * @return The folder that contains this index.
     */
    public File getLocation() {
        return this.location_;
    }

    /**
     * Change the analyzer of an index. You MUST call this method if you
     * constructed the index with an analyzer of your own. You always must use
     * the same Analyzer.
     * 
     * @param analyzer
     */
    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer_ = analyzer;
    }
}