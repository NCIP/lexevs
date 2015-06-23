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

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

/**
 * Indexing class to be used for small updates, or real time indexing.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class LuceneFSIndexWriter implements LuceneIndexWriterInterface {
    private int maxFieldLength_ = Integer.MAX_VALUE; // Lucene's default will
    // silently truncate docs.
    // Unsafe. Rather have it
    // run out of memory.
    private int maxMergeDocs_ = -1; // Leave at lucene default
    private int mergeFactor_ = 5; // Good for making documents available sooner.
    private int maxBufferedDocs_ = -1; // lucene default
    private boolean useCompoundFile_ = false; // lucene defaults to true - bad
    // for performance.
    private final File location_;
    private Analyzer analyzer_;

    private IndexWriter masterIndexWriter_; // The index to store everything in
    // when they are all closed.

    private final Logger logger = Logger.getLogger("Indexer.Index");

    public LuceneFSIndexWriter(File location, boolean clearContents, Analyzer analyzer)
            throws RuntimeException {
        this.location_ = location;
        this.analyzer_ = analyzer;
        openIndex(clearContents);
    }

    private void openIndex(boolean clearContents) throws RuntimeException {
        try {
            masterIndexWriter_ = new IndexWriter(location_, analyzer_, clearContents);
            this.updateLuceneVars();
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error opening the index writer. " + e.getMessage());
        }
    }

    /**
     * @param i
     */
    public void setDocsPerTempIndex(int i) {
        throw new UnsupportedOperationException();
    }

    /**
     * @param i
     */
    public void setMaxFieldLength(int i) {
        maxFieldLength_ = i;
        updateLuceneVars();
    }

    /**
     * @param i
     */
    public void setMaxMergeDocs(int i) {
        maxMergeDocs_ = i;
        updateLuceneVars();
    }

    public void setMaxBufferedDocs(int i) {
        maxBufferedDocs_ = i;
        updateLuceneVars();
    }

    /**
     * @param i
     */
    public void setMergeFactor(int i) {
        mergeFactor_ = i;
        updateLuceneVars();
    }

    public void setUseCompoundFile(boolean bool) {
        useCompoundFile_ = bool;
        updateLuceneVars();
    }

    private void updateLuceneVars() {
        if (maxFieldLength_ > 1) {
            this.masterIndexWriter_.setMaxFieldLength(maxFieldLength_);
        }
        if (maxMergeDocs_ > 1) {
            this.masterIndexWriter_.setMaxMergeDocs(maxMergeDocs_);
        }
        if (mergeFactor_ > 1) {
            this.masterIndexWriter_.setMergeFactor(mergeFactor_);
        }
        if (maxBufferedDocs_ > 1) {
            this.masterIndexWriter_.setMaxBufferedDocs(maxBufferedDocs_);
        }
        this.masterIndexWriter_.setUseCompoundFile(useCompoundFile_);
    }

    public void addDocument(Document document) throws RuntimeException {
        try {
            this.masterIndexWriter_.addDocument(document);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error adding the document. " + e.getMessage());
        }
    }

    public void addDocument(Document document, Analyzer analyzer) throws RuntimeException {
        try {
            this.masterIndexWriter_.addDocument(document, analyzer);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error adding the document. " + e.getMessage());
        }
    }

    public void optimize() throws RuntimeException {
        try {
            this.masterIndexWriter_.optimize();
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error closing the index writer. " + e.getMessage());
        }
    }

    public void close() throws RuntimeException {
        logger.info("Closing the index writer");
        try {
            masterIndexWriter_.close();
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException("There was an error closing the index writer. " + e.getMessage());
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}