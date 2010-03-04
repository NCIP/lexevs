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
package org.lexevs.dao.index.connection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryFilter;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.lexevs.exceptions.InternalException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LgLoggerIF;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.model.LocalCodingScheme;

import edu.mayo.informatics.indexer.api.IndexerService;
import edu.mayo.informatics.indexer.api.SearchServiceInterface;
import edu.mayo.informatics.indexer.api.exceptions.IndexNotFoundException;
import edu.mayo.informatics.indexer.api.exceptions.InternalErrorException;
import edu.mayo.informatics.indexer.api.exceptions.InternalIndexerErrorException;
import edu.mayo.informatics.indexer.lucene.IDFNeutralSimilarity;
import edu.mayo.informatics.indexer.lucene.LuceneIndexReader;

/**
 * This classes manages the interactions with a single lucene index directory.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexInterface {
    private IndexerService service_;

    private Hashtable<String, SearchServiceInterface> indexSearchers_;
    private Hashtable<String, LuceneIndexReader> indexReaders_;
    private Hashtable<String, String> codeSystemToIndexMap_;
    private HashMap<String, DocIdSet> boundryDocIdSetMap;

    private Filter codeBoundryFilter_;

    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    public IndexInterface(IndexerService service) {
    	this.service_ = service;
    	try {
			init();
		} catch (Exception e) {
			throw new RuntimeException("There was an unexpected error while initializing the index service. Exception: " + e);
		} 
    }

    public IndexInterface(String location) {
        try {
            service_ = new IndexerService(location, false);
            init();
        } catch (Exception e) {
            throw new RuntimeException("There was an unexpected error while initializing the index service. Exception: " + e);
        }
    }

    /*
     * Initialize all of the lucene index reading/searching parts.
     */
    private void init() throws LBInvocationException, UnexpectedInternalError {
        indexSearchers_ = new Hashtable<String, SearchServiceInterface>();
        indexReaders_ = new Hashtable<String, LuceneIndexReader>();
        boundryDocIdSetMap = new HashMap<String, DocIdSet>();

        // See the top of the class for a description of what the codeBoundry
        // stuff is used for.
        BooleanQuery includesFilterQuery = new BooleanQuery();
        includesFilterQuery.add(new BooleanClause(new TermQuery(new Term("codeBoundry", "T")), Occur.MUST));
        codeBoundryFilter_ = new QueryFilter(includesFilterQuery);
       
        initCodingSchemes();
    }

    public void initCodingSchemes() throws LBInvocationException {
        Hashtable<String, String> temp = new Hashtable<String, String>();

        try {
            service_.refreshAvailableIndexes();
        } catch (InternalErrorException e) {
            String logId = getLogger()
                    .error(
                            "There was an unexpected error while rereading the index metadata at "
                                    + service_.getRootLocation(), e);
            throw new LBInvocationException("There was an unexpected internal error", logId);
        }

        try {
            String[] codingSchemeVersionPairs = service_.getMetaData().getIndexMetaDataKeys();

            // The index metadata file has notes that at the top level map
            // coding system / version combination
            // strings to the index that contains them. I want to find all of
            // the unique index locations.
            //HashSet<String> uniqueIndexLocations = new HashSet<String>();
            for (int i = 0; i < codingSchemeVersionPairs.length; i++) {
            	temp.put(codingSchemeVersionPairs[i], service_.getMetaData().getIndexMetaDataValue(codingSchemeVersionPairs[i]));
                
            	//Don't bother with this...
            	//uniqueIndexLocations.add(service_.getMetaData().getIndexMetaDataValue(codingSchemeVersionPairs[i]));
            }

            // TODO: This index metadata file is.... weird.
            // We don't need the code below if we are consistent about key creation.
            // It would be nice to do this differently -- Castor/XStream marshalling... etc...
            // This should simplify things and keep backward compatiblity.
            // TODO:
            
            // now, for each index location, read the coding scheme name and
            // version information, and
            // add that to the hashtable.
            /*
            Iterator<String> indexLocations = uniqueIndexLocations.iterator();
            while (indexLocations.hasNext()) {
                String currentLocation = indexLocations.next();

                LocalCodingScheme lcs = new LocalCodingScheme();
                lcs.codingSchemeName = service_.getMetaData().getIndexMetaDataValue(currentLocation, "codingScheme");
                lcs.version = service_.getMetaData().getIndexMetaDataValue(currentLocation, "version");

                temp.put(lcs.getKey(), currentLocation);
            } 
            */
            codeSystemToIndexMap_ = temp;
        } catch (InternalErrorException e) {
            throw new RuntimeException("There was a problem reading the index metadata.", e);
        }
    }

    public Filter getCodeBoundryFilter() {
        return codeBoundryFilter_;
    }

    public ArrayList<String> getCodeSystemKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        Enumeration<String> e = codeSystemToIndexMap_.keys();
        while (e.hasMoreElements()) {
            keys.add(e.nextElement());
        }
        return keys;
    }
    
    public DocIdSetIterator getBoundaryDocumentIterator(String internalCodeSystemName, String internalVersionString){
        String indexName = this.mapCodeSystemToIndexName(internalCodeSystemName, internalVersionString);
        
        if(!boundryDocIdSetMap.containsKey(indexName)){
            try {
                IndexReader reader = getIndexReader(internalCodeSystemName, internalVersionString).getBaseIndexReader();
                
                boundryDocIdSetMap.put(indexName, this.getCodeBoundryFilter().getDocIdSet(reader));

            } catch (Exception e) {
                throw new RuntimeException("There was a problem initializing the index.", e);
            } 
        }
        
        return boundryDocIdSetMap.get(indexName).iterator();
    }

    /*
     * Get a index reader for a given code system.
     */
    public LuceneIndexReader getIndexReader(String internalCodeSystemName, String internalVersionString) {
        String indexName = mapCodeSystemToIndexName(internalCodeSystemName, internalVersionString);

        LuceneIndexReader lir = (LuceneIndexReader) indexReaders_.get(indexName);

        if (lir != null) {
            return lir;
        }
        // if it did equal null, its not in the cache - open one up and put it
        // in the cache.
        try {
            lir = service_.getLuceneIndexReader(indexName);
            indexReaders_.put(indexName, lir);

            return lir;
        } catch (IndexNotFoundException e) {
            throw new RuntimeException("There was an error opening the index for " + internalCodeSystemName
                    + ".  I tried to open the index " + indexName + " from " + service_.getRootLocation(), e);
        } catch (InternalIndexerErrorException e) {
            throw new RuntimeException("There was an error opening the index for " + internalCodeSystemName
                    + ".  I tried to open the index " + indexName + " from " + service_.getRootLocation(), e);
        }
    }

    public void reopenMetaDataIndexReader() {
        // clear out the current reader, it will be reopened when necessary.
        String indexName = SystemVariables.getMetaDataIndexName();
        indexReaders_.remove(indexName);
        indexSearchers_.remove(indexName);
    }

    public LuceneIndexReader getMetaDataIndexReader() {
        String indexName = SystemVariables.getMetaDataIndexName();
        LuceneIndexReader lir = (LuceneIndexReader) indexReaders_.get(indexName);

        if (lir != null) {
            return lir;
        }
        // if it did equal null, its not in the cache - open one up and put it
        // in the cache.
        try {
            lir = service_.getLuceneIndexReader(indexName);
            indexReaders_.put(indexName, lir);

            return lir;
        } catch (IndexNotFoundException e) {
            throw new RuntimeException("There was an error opening the index for the MetaData Index", e);
        } catch (InternalIndexerErrorException e) {
            throw new RuntimeException("There was an error opening the index for the MetaData Index", e);
        }
    }

    public void reopenIndex(String internalCodeSystemName, String internalVersionString) throws LBInvocationException {
        // remove the index searcher, so a new one gets constructed.
        String indexName = mapCodeSystemToIndexName(internalCodeSystemName, internalVersionString);
        SearchServiceInterface ssi = indexSearchers_.remove(indexName);
        // tell the lucene index to reopen its internal readers.
        try {
            LuceneIndexReader lir = indexReaders_.get(indexName);
            if (lir != null) {
                lir.reopen();
            }
        } catch (InternalIndexerErrorException e1) {
            String id = getLogger().error("Problem reopening index", e1);
            throw new LBInvocationException("Unexpected problem reopening index", id);
        }

        try {
            if (ssi != null) {
                ssi.close();
            }
        } catch (InternalIndexerErrorException e) {
            // ignore these errors. These interfaces won't be used again anyway.
        }
    }

    /*
     * Get an index searcher for a given code system.
     */
    public SearchServiceInterface getSearcher(String internalCodeSystemName, String internalVersionString){
        String indexName = mapCodeSystemToIndexName(internalCodeSystemName, internalVersionString);

        SearchServiceInterface si = (SearchServiceInterface) indexSearchers_.get(indexName);

        if (si != null) {
            return si;
        }
        // if it did equal null, its not in the cache - open one up and put it
        // in the cache.
        try {
            si = service_.getIndexSearcher(indexName);
            si.setSimilarity(new IDFNeutralSimilarity());
            indexSearchers_.put(indexName, si);

            return si;
        } catch (IndexNotFoundException e) {
            throw new RuntimeException("There was an error opening the index searcher for "
                    + internalCodeSystemName, e);
        } catch (InternalIndexerErrorException e) {
            throw new RuntimeException("There was an error opening the index searcher for "
                    + internalCodeSystemName, e);
        }
    }

    public SearchServiceInterface getMetaDataSearcher() {
        String indexName = SystemVariables.getMetaDataIndexName();

        SearchServiceInterface si = (SearchServiceInterface) indexSearchers_.get(indexName);

        if (si != null) {
            return si;
        }
        // if it did equal null, its not in the cache - open one up and put it
        // in the cache.
        try {
            si = service_.getIndexSearcher(indexName);
            si.setSimilarity(new IDFNeutralSimilarity());
            indexSearchers_.put(indexName, si);

            return si;
        } catch (IndexNotFoundException e) {
            throw new RuntimeException("There was an error opening the metadata index searcher", e);
        } catch (InternalIndexerErrorException e) {
            throw new RuntimeException("There was an error opening the metadata index searcher.", e);
        }
    }

    public void deleteIndex(String internalCodeSystemName, String internalVersionString) throws InternalException {
        try {
            LocalCodingScheme lcs = new LocalCodingScheme();
            lcs.codingSchemeName = internalCodeSystemName;
            lcs.version = internalVersionString;

            // remove it from the stored hash tables.

            String indexName = codeSystemToIndexMap_.get(lcs.getKey());
            codeSystemToIndexMap_.remove(lcs.getKey());

            SearchServiceInterface ssi = indexSearchers_.get(indexName);
            if (ssi != null) {
                ssi.close();
            }

            indexSearchers_.remove(indexName);

            LuceneIndexReader lir = indexReaders_.get(indexName);
            if (lir != null) {
                lir.close();
            }

            indexReaders_.remove(indexName);

            // clean up the index metadata file.

            service_.getMetaData().removeAllIndexMetaDataValue(indexName);
            service_.getMetaData().removeIndexMetaDataValue(lcs.getKey());

            // delete the index
            service_.deleteIndex(indexName);
        } catch (Exception e) {
            throw new InternalException("Problem trying to delete an item from the index interface", e);
        }
    }

    private String mapCodeSystemToIndexName(String internalCodeSystemName, String internalVersionString){
        LocalCodingScheme lcs = new LocalCodingScheme();
        lcs.codingSchemeName = internalCodeSystemName;
        lcs.version = internalVersionString;

        String indexName = codeSystemToIndexMap_.get(lcs.getKey());

        if (indexName == null) {
            throw new RuntimeException("The index for the code system " + internalCodeSystemName
                    + " is not available.");
        }
        return indexName;
    }

    public String getMetaLocation() {
        try {
            return service_.getMetaData().getMetaLocation();
        } catch (IOException e) {
            throw new RuntimeException("Unexpected Error", e);
        }
    }

    public IndexerService getBaseIndexerService() {
        return service_;
    }

    public String getIndexLocation(String internalCodeSystemName, String internalVersionString) {
        try {
            String indexName = mapCodeSystemToIndexName(internalCodeSystemName, internalVersionString);
            return new File(new File(service_.getRootLocation()), indexName).getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        Enumeration<SearchServiceInterface> ssi = indexSearchers_.elements();
        while (ssi.hasMoreElements()) {
            try {
                ssi.nextElement().close();
            } catch (InternalIndexerErrorException e) {
                // do nothing
            }
        }

        Enumeration<LuceneIndexReader> lir = indexReaders_.elements();
        while (lir.hasMoreElements()) {
            try {
                lir.nextElement().close();
            } catch (InternalIndexerErrorException e) {
                // do nothing
            }
        }
    }
}