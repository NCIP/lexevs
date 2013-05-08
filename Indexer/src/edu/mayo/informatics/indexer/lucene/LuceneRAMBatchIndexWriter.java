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
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;
import edu.mayo.informatics.indexer.utility.Utility;

/**
 * Indexes documents into a RAMDirectory, and then flushes the RAMDirectory to
 * disk when docsPerTempIndex_ is reached. Gives small speed gains over a
 * standard FSDirectory.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class LuceneRAMBatchIndexWriter implements LuceneIndexWriterInterface {
    private int docsPerTempIndex_ = 10000;
    private int maxFieldLength_ = Integer.MAX_VALUE; // Lucene's default will
    // silently truncate docs.
    // Unsafe. Rather have it
    // run out of memory.
    private int maxMergeDocs_ = -1; // Leave at lucene default
    private int mergeFactor_ = 90; // tested best
    private int maxBufferedDocs_ = 100;
    private boolean useCompoundFile_ = false; // lucene defaults to true - bad
    // for performance.
    private final File location_;

    private IndexWriter masterIndexWriter_; // The index to store everything in
    // when they are all closed.
    private IndexWriter activeIndexWriter_; // The index we are currently
    // writing to.

    private RAMDirectory ramDirectory_; // The RAMDirectory to write new docs
    // to.
    private ArrayList tempIndexes_; // Place to write out the RAMDirectories

    private int docsInCurrentRamIndex = 0;
    private int currentIndexId = 0; // to create unique file names for the temp
    // directories.

    private Analyzer analyzer_;

    private final Logger logger = Logger.getLogger("Indexer.Index");

    public LuceneRAMBatchIndexWriter(File location, boolean clearContents, Analyzer analyzer)
            throws InternalIndexerErrorException {
        this.location_ = location;
        this.analyzer_ = analyzer;
        tempIndexes_ = new ArrayList();
        openBatchWriter(clearContents);
        logger.info("Created a LuceneRAMBatchIndexWriter");
    }

    private void openBatchWriter(boolean clearContents) throws InternalIndexerErrorException {
        try {
            Utility.removeSubFolders(this.location_);
            masterIndexWriter_ = new IndexWriter(location_, analyzer_, clearContents);
            openNewRamIndexer();
            logger.info("opened the batch writer");
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error opening the index writer. " + e.getMessage());
        }
    }

    private void openNewRamIndexer() throws IOException {
        logger.info("Opening a RAM directory");
        ramDirectory_ = new RAMDirectory();
        this.docsInCurrentRamIndex = 0;

        activeIndexWriter_ = new IndexWriter(ramDirectory_, analyzer_, true);
        updateLuceneVars();
    }

    public void close() throws InternalIndexerErrorException {

        try {
            logger.info("Closing the index writer");
            storeActiveIndex();
            logger.info("Merging all temporary Indexes");
            masterIndexWriter_.addIndexes((Directory[]) this.tempIndexes_.toArray(new Directory[tempIndexes_.size()]));
            masterIndexWriter_.close();
            logger.info("Removing temporary Indexes");
            Utility.removeSubFolders(this.location_);
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error closing the index writer. " + e.getMessage());
        }

    }

    private void storeActiveIndex() throws IOException {
        logger.info("Storing memory index to disk");
        activeIndexWriter_.close();
        ramDirectory_.close();

        File tempFile = new File(location_, "temp" + this.currentIndexId++);
        tempFile.mkdir();
        FSDirectory tempFSDirectory = FSDirectory.getDirectory(tempFile, true);

        IndexWriter tempIndexWriter = new IndexWriter(tempFSDirectory, analyzer_, true);
        tempIndexWriter.addIndexes(new Directory[] { ramDirectory_ });
        tempIndexWriter.close();

        this.tempIndexes_.add(tempFSDirectory);

        activeIndexWriter_ = null;
        ramDirectory_ = null;
        tempIndexWriter = null;
        tempFile = null;
    }

    public void addDocument(Document document) throws InternalIndexerErrorException {
        try {
            if (this.docsInCurrentRamIndex >= this.docsPerTempIndex_) {
                storeActiveIndex();
                openNewRamIndexer();
            }
            activeIndexWriter_.addDocument(document);
            this.docsInCurrentRamIndex++;
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error adding the document. " + e.getMessage());
        }
    }

    public void addDocument(Document document, Analyzer analyzer) throws InternalIndexerErrorException {
        try {
            if (this.docsInCurrentRamIndex >= this.docsPerTempIndex_) {
                storeActiveIndex();
                openNewRamIndexer();
            }
            activeIndexWriter_.addDocument(document, analyzer);
            this.docsInCurrentRamIndex++;
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error adding the document. " + e.getMessage());
        }
    }

    /**
     * @param i
     */
    public void setDocsPerTempIndex(int i) {
        docsPerTempIndex_ = i;
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

    public void optimize() throws InternalIndexerErrorException {
        try {
            this.masterIndexWriter_.optimize();
        } catch (IOException e) {
            logger.error(e);
            throw new InternalIndexerErrorException("There was an error closing the index writer. " + e.getMessage());
        }
    }

    private void updateLuceneVars() {
        if (maxFieldLength_ > 1) {
            this.activeIndexWriter_.setMaxFieldLength(maxFieldLength_);
            this.masterIndexWriter_.setMaxFieldLength(maxFieldLength_);
        }
        if (maxMergeDocs_ > 1) {
            this.activeIndexWriter_.setMaxMergeDocs(maxMergeDocs_);
            this.masterIndexWriter_.setMaxMergeDocs(maxMergeDocs_);
        }
        if (mergeFactor_ > 1) {
            this.activeIndexWriter_.setMergeFactor(mergeFactor_);
            this.masterIndexWriter_.setMergeFactor(mergeFactor_);
        }
        if (maxBufferedDocs_ > 1) {
            this.activeIndexWriter_.setMaxBufferedDocs(mergeFactor_);
            this.masterIndexWriter_.setMaxBufferedDocs(maxBufferedDocs_);
        }
        this.activeIndexWriter_.setUseCompoundFile(useCompoundFile_);
        this.masterIndexWriter_.setUseCompoundFile(useCompoundFile_);
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}