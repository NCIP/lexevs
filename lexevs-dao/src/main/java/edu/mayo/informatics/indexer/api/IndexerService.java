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
package edu.mayo.informatics.indexer.api;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;

import edu.mayo.informatics.indexer.api.exceptions.DocumentMissingUniqueDocumentIdentifierException;
import edu.mayo.informatics.indexer.api.exceptions.IndexNotFoundException;
import edu.mayo.informatics.indexer.api.exceptions.IndexWriterAlreadyOpenException;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;
import edu.mayo.informatics.indexer.api.exceptions.OperatorErrorException;
import edu.mayo.informatics.indexer.lucene.Index;
import edu.mayo.informatics.indexer.lucene.LuceneIndexReader;
import edu.mayo.informatics.indexer.lucene.LuceneIndexSearcher;
import edu.mayo.informatics.indexer.lucene.LuceneMultiIndexSearcher;
import edu.mayo.informatics.indexer.utility.MetaData;
import edu.mayo.informatics.indexer.utility.Utility;

/**
 * This class will sit on top of multiple indexes, and manage them for you.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust </A>
 */
public class IndexerService {
    private File rootLocation_; // The root directory of the indexes
    private Hashtable indexes_; // will hold all of the current indexes
    private MetaData metadata_;

    /**
     * Create an indexer service on a directory. A Indexer Service can contain
     * multiple indexes.
     * 
     * @param rootLocation
     *            The directory where all of your indexes are located
     * @param configureLog4j
     *            Whether or not to configure a log4j appender.
     * @throws InternalErrorException
     */
    public IndexerService(String rootLocation, boolean configureLog4j) throws InternalErrorException {
        initServices(rootLocation, configureLog4j);
    }

    /**
     * Create an indexer service on a directory. A Indexer Service can contain
     * multiple indexes.
     * 
     * @param rootLocation
     *            The directory where all of your indexes are located
     * @throws InternalErrorException
     */
    public IndexerService(String rootLocation) throws InternalErrorException {
        initServices(rootLocation, false);
    }

    private void initServices(String rootLocation, boolean configureLog4j) throws InternalErrorException {
        if (configureLog4j) {
            org.apache.log4j.BasicConfigurator.configure();
        }

        indexes_ = new Hashtable();
        File root = new File(rootLocation);
        this.rootLocation_ = root;
        if (root.exists()) {
            loadIndexes();
        } else {
            root.mkdir();
        }

        initMetaData(root);

    }

    public void refreshAvailableIndexes() throws InternalErrorException {
        File[] files = rootLocation_.listFiles();
        HashSet fileNames = new HashSet();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                fileNames.add(files[i].getName());

                // add new ones
                if (!indexes_.contains(files[i].getName())) {
                    Index temp = new Index(files[i], new String[] {});
                    indexes_.put(files[i].getName(), temp);
                }
            }
        }

        // remove ones that no longer exist
        Enumeration indexes = indexes_.keys();
        while (indexes.hasMoreElements()) {
            String indexName = (String) indexes.nextElement();
            if (!fileNames.contains(indexName)) {
                indexes_.remove(indexName);
            }
        }
        metadata_.rereadFile(true);
    }

    /**
     * Create a new index, using the default analyzer, and your supplied
     * stopwords.
     * 
     * @param indexName
     *            Name of index - will match folder name created to hold it
     * @param stopwords
     *            Words not to index
     */
    public void createIndex(String indexName, String[] stopwords) {
        File newIndexLocation = new File(rootLocation_, indexName);
        newIndexLocation.mkdir();
        Index newIndex = new Index(newIndexLocation, stopwords);
        indexes_.put(indexName, newIndex);
    }

    /**
     * Create a new index, using a default analyzer, and no stopwords.
     * 
     * @param indexName
     *            Name of index - will match folder name created to hold it
     */
    public void createIndex(String indexName) {
        File newIndexLocation = new File(rootLocation_, indexName);
        newIndexLocation.mkdir();
        Index newIndex = new Index(newIndexLocation, new String[] {});
        indexes_.put(indexName, newIndex);
    }

    /**
     * Create a new index, using a user specified analyzer. When you do searches
     * on an index created with an analyzer of your choosing, you MUST supply
     * the same analyzer to the searcher. You will get invalid results from your
     * searches if you don't.
     * 
     * @param indexName
     *            Name of index - will match folder name created to hold it
     * @param analyzer
     *            The analyzer to use while creating the index. See the Lucene
     *            Documentation.
     */
    public void createIndex(String indexName, Analyzer analyzer) {
        File newIndexLocation = new File(rootLocation_, indexName);
        newIndexLocation.mkdir();
        Index newIndex = new Index(newIndexLocation, analyzer);
        indexes_.put(indexName, newIndex);
    }

    /**
     * Delete an index from this indexerService.
     * 
     * @param indexName
     * @throws IndexNotFoundException
     */
    public void deleteIndex(String indexName) throws IndexNotFoundException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }

        try {
            currentIndex.closeIndexWriter();
        } catch (InternalIndexerErrorException e) {
        }
        try {
            File temp = currentIndex.getLocation();
            Utility.deleteRecursive(temp);
            indexes_.remove(indexName);
            currentIndex = null;
        } catch (Exception e) {
            throw new InternalIndexerErrorException(
                    "Could not remove the index.. Is another process accessing the files? " + e);
        }
    }

    /**
     * Opens a new batchIndexWriter. Use this for large updates.
     * 
     * @param indexName
     * @param clearContents
     *            true erases current content, false appends.
     * @param useRAMIndexer
     *            true for a ram indexer, false for a file system indexer
     * @throws IndexNotFoundException
     * @throws IndexWriterAlreadyOpenException
     * @throws InternalIndexerErrorException
     */
//    public void openBatchWriter(String indexName, boolean clearContents, boolean useRAMIndexer)
//            throws IndexNotFoundException, IndexWriterAlreadyOpenException, InternalIndexerErrorException {
//        Index currentIndex = (Index) indexes_.get(indexName);
//
//        if (currentIndex == null) {
//            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
//        }
//        if (useRAMIndexer) {
//            currentIndex.openBatchRAMIndexWriter(clearContents);
//        } else {
//            currentIndex.openBatchFSIndexWriter(clearContents);
//        }
//    }

    /**
     * Opens an index writer. Best for small updates. Use a Batch Writer for
     * large updates.
     * 
     * @param indexName
     * @param clearContents
     *            True erases the current index. False appends to it.
     * @throws IndexNotFoundException
     * @throws IndexWriterAlreadyOpenException
     * @throws InternalIndexerErrorException
     */
    public void openWriter(String indexName, boolean clearContents) throws IndexNotFoundException,
            IndexWriterAlreadyOpenException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.openFSIndexWriter(clearContents);
    }

    /**
     * Closes the currently open Index.
     * 
     * @param indexName
     * @throws IndexNotFoundException
     * @throws InternalIndexerErrorException
     */
    public void closeWriter(String indexName) throws IndexNotFoundException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.closeIndexWriter();
    }

    /**
     * Closes the currently open IndexReader.
     * 
     * @param indexName
     * @throws IndexNotFoundException
     * @throws InternalIndexerErrorException
     */
    public void closeBatchRemover(String indexName) throws IndexNotFoundException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.closeIndexReader();
    }

    /**
     * Opens a reader on the index. This is really only needed for batch
     * deletes.
     * 
     * @param indexName
     *            The index to open
     * @throws IndexNotFoundException
     * @throws InternalIndexerErrorException
     */
    public void openBatchRemover(String indexName) throws IndexNotFoundException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.openIndexReader();
    }

    /**
     * Add a document to the index. Note: If you construct your own document
     * (rather than using a provided document generator, you must add a field to
     * your document of Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD and a unique
     * value. If the values are not unique, you will not be able to easily
     * remove documents from the index. If you are going to be adding multiple
     * items, it will be much faster to call openWriter or openBatchWriter
     * before you do your additions, and call closeWriter after you do them.
     * 
     * @param indexName
     *            The index to add the document to.
     * @param document
     *            The document to add
     * @throws IndexNotFoundException
     * @throws InternalIndexerErrorException
     * @throws IndexWriterNotOpenException
     */
    public void addDocument(String indexName, Document document) throws IndexNotFoundException,
            InternalIndexerErrorException, DocumentMissingUniqueDocumentIdentifierException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        if (document.getField(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD) == null) {
            throw new DocumentMissingUniqueDocumentIdentifierException(
                    "You must provide a Unique Document Identifier Field.");
        }
        currentIndex.addDocument(document);

    }

    /**
     * Run the low level lucene optimize command on an index. This is usually
     * only necessary after a large amount of deletes from an index.
     * 
     * @param indexName
     * @throws IndexNotFoundException
     * @throws InternalIndexerErrorException
     */
    public void optimizeIndex(String indexName) throws IndexNotFoundException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }

        currentIndex.optimizeIndex();
    }

    /**
     * Add a document to the index. Note: If you construct your own document
     * (rather than using a provided document generator, you must add a field to
     * your document of Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD and a unique
     * value. If the values are not unique, you will not be able to easily
     * remove documents from the index. If you are going to be adding multiple
     * items, it will be much faster to call openWriter or openBatchWriter
     * before you do your additions, and call closeWriter after you do them.
     * 
     * @param indexName
     *            The index to add the document to.
     * @param document
     *            The document to add
     * @param analyzer
     *            The analyzer to use
     * @throws IndexNotFoundException
     * @throws InternalIndexerErrorException
     * @throws IndexWriterNotOpenException
     */
    public void addDocument(String indexName, Document document, Analyzer analyzer) throws IndexNotFoundException,
            InternalIndexerErrorException, DocumentMissingUniqueDocumentIdentifierException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        if (document.getField(Index.UNIQUE_DOCUMENT_IDENTIFIER_FIELD) == null) {
            throw new DocumentMissingUniqueDocumentIdentifierException(
                    "You must provide a Unique Document Identifier Field.");
        }
        currentIndex.addDocument(document, analyzer);

    }

    /**
     * Use this method to remove a document from an index. You may not have a
     * index writer open while you do this. If you are going to do multiple
     * deletions, you will get much better performance if you call
     * openBatchRemover before your deletions, and call closeBatchRemover after
     * your removals.
     * 
     * @param indexName
     * @param documentIdentifier
     *            The document identifier that was used to add the document.
     * @return The number of documents removed
     * @throws IndexNotFoundException
     * @throws IndexReaderNotOpenException
     * @throws InternalIndexerErrorException
     * @throws OperatorErrorException
     */
    public int removeDocument(String indexName, String documentIdentifier) throws IndexNotFoundException,
            InternalIndexerErrorException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        return currentIndex.removeDocument(documentIdentifier);
    }

    /**
     * This will remove all documents from an index where fieldValue = field.
     * You may not have a index writer open while you do this. If you are going
     * to do multiple deletions, you will get much better performance if you
     * call openBatchRemover before your deletions, and call closeBatchRemover
     * after your removals.
     * 
     * @param indexName
     *            The index to remove from.
     * @param field
     *            The field to look for.
     * @param fieldValue
     *            The value to match.
     * @return The number of documents removed.
     * @throws IndexNotFoundException
     * @throws IndexReaderNotOpenException
     * @throws InternalIndexerErrorException
     * @throws OperatorErrorException
     */
    public int removeDocument(String indexName, String field, String fieldValue) throws IndexNotFoundException,
            InternalIndexerErrorException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        return currentIndex.removeDocument(field, fieldValue);
    }

    /**
     * List all of the indexes in this indexservice.
     * 
     * @return All of the indexes that exist
     */
    public String[] listIndexes() {
        String[] results = new String[indexes_.size()];
        Enumeration enumer = indexes_.keys();
        int i = 0;
        while (enumer.hasMoreElements()) {
            results[i++] = (String) enumer.nextElement();
        }
        return results;

    }

    public SearchServiceInterface getIndexSearcher(String[] indexNames, boolean parallelSearch)
            throws InternalIndexerErrorException, IndexNotFoundException {
        return getIndexSearcher(indexNames, false, parallelSearch);
    }

    public SearchServiceInterface getIndexSearcher(String[] indexNames, boolean useInMemoryIndex, boolean parallelSearch)
            throws InternalIndexerErrorException, IndexNotFoundException {
        LuceneIndexReader[] temp = new LuceneIndexReader[indexNames.length];

        for (int i = 0; i < temp.length; i++) {
            Index currentIndex = (Index) indexes_.get(indexNames[i]);

            if (currentIndex == null) {
                throw new IndexNotFoundException("The index " + indexNames[i] + " does not exist.");
            }
            temp[i] = currentIndex.getIndexReader(useInMemoryIndex);
        }

        return new LuceneMultiIndexSearcher(temp, parallelSearch);
    }

    public LuceneIndexReader getLuceneIndexReader(String indexName) throws IndexNotFoundException,
            InternalIndexerErrorException {
        return getLuceneIndexReader(indexName, false);
    }

    public LuceneIndexReader getLuceneIndexReader(String indexName, boolean useInMemoryIndex)
            throws IndexNotFoundException, InternalIndexerErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        return currentIndex.getIndexReader(useInMemoryIndex);
    }

    public SearchServiceInterface getIndexSearcher(String indexName) throws InternalIndexerErrorException,
            IndexNotFoundException {
        return getIndexSearcher(indexName, false);
    }

    public SearchServiceInterface getIndexSearcher(String indexName, boolean useInMemoryIndex)
            throws InternalIndexerErrorException, IndexNotFoundException {
        return new LuceneIndexSearcher(getLuceneIndexReader(indexName, useInMemoryIndex));
    }

    private void loadIndexes() {
        File[] files = rootLocation_.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                Index temp = new Index(files[i], new String[] {});
                indexes_.put(files[i].getName(), temp);
            }
        }
    }

    private void initMetaData(File root) throws InternalErrorException {
        metadata_ = new MetaData(root);
    }

    public String getRootLocation() {
        return rootLocation_.getAbsolutePath();
    }

    public void forceUnlockIndex(String indexName) throws IndexNotFoundException, InternalIndexerErrorException {
        try {
            IndexReader reader = getLuceneIndexReader(indexName).getBaseIndexReader();
            IndexReader.unlock(reader.directory());
        } catch (IndexNotFoundException e) {
            throw e;
        } catch (InternalIndexerErrorException e) {
            throw e;
        } catch (IOException e) {
            throw new InternalIndexerErrorException("There was an error while trying to unlock the index " + e);
        }
    }

    public MetaData getMetaData() {
        return metadata_;
    }

    /**
     * How many documents to write out per temporary index. Used for
     * performance/controlling open file handles. Only used on writers.
     * 
     * @param indexName
     * @param docs
     * @throws IndexNotFoundException
     * @throws OperatorErrorException
     */
    public void setDocsPerTempIndex(String indexName, int docs) throws IndexNotFoundException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.setDocsPerTempIndex(docs);
    }

    /**
     * How many documents to buffer before merging. Only used on writers. See
     * Lucene documentation.
     * 
     * @param indexName
     * @param docs
     * @throws IndexNotFoundException
     * @throws OperatorErrorException
     */
    public void setMaxBufferedDocs(String indexName, int docs) throws IndexNotFoundException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.setMaxBufferedDocs(docs);
    }

    /**
     * Lucene will truncate fields longer than this. Only used on writers
     * 
     * @param indexName
     * @param size
     * @throws IndexNotFoundException
     * @throws OperatorErrorException
     */
    public void setMaxFieldLength(String indexName, int size) throws IndexNotFoundException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.setMaxFieldLength(size);
    }

    /**
     * See the lucene documentation. Probably unneeded. Only used on writers.
     * 
     * @param indexName
     * @param docs
     * @throws IndexNotFoundException
     * @throws OperatorErrorException
     */
    public void setMaxMergeDocs(String indexName, int docs) throws IndexNotFoundException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.setMaxMergeDocs(docs);
    }

    /**
     * How many documents to add in memory before writing to the index. Has
     * large affects on performance. Only used on writers.
     * 
     * @param indexName
     * @param mergeFactor
     * @throws IndexNotFoundException
     * @throws OperatorErrorException
     */
    public void setMergeFactor(String indexName, int mergeFactor) throws IndexNotFoundException, OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.setMergeFactor(mergeFactor);
    }

    /**
     * Whether or not to use the new compound file format. Reduces the number of
     * open files, but also reduces the indexing performance.
     * 
     * @param indexName
     * @param bool
     * @throws IndexNotFoundException
     * @throws OperatorErrorException
     */
    public void setUseCompoundFile(String indexName, boolean bool) throws IndexNotFoundException,
            OperatorErrorException {
        Index currentIndex = (Index) indexes_.get(indexName);

        if (currentIndex == null) {
            throw new IndexNotFoundException("The index " + indexName + " does not exist.");
        }
        currentIndex.setUseCompoundFile(bool);
    }
}