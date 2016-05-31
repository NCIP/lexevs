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
package org.lexevs.dao.index.connection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;

import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Utility.logging.LgLoggerIF;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.lexevs.dao.indexer.api.IndexerService;
import org.lexevs.dao.indexer.api.SearchServiceInterface;
import org.lexevs.dao.indexer.lucene.LuceneIndexReader;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.exceptions.InternalException;
import org.lexevs.exceptions.UnexpectedInternalError;
import org.lexevs.logging.LoggerFactory;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.model.LocalCodingScheme;

/**
 * This classes manages the interactions with a single lucene index directory.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @author <A HREF="mailto:erdmann.jesse@mayo.edu">Jesse Erdmann</A>
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class IndexInterface {
    
    /** The service_. */
    private IndexerService service_;
    
    private ConcurrentMetaData  metadataService_;

    /** The index searchers_. */
    private Hashtable<String, SearchServiceInterface> indexSearchers_;
    
    /** The index readers_. */
    private Hashtable<String, LuceneIndexReader> indexReaders_;
    
    /** The code system to index map_. */
    private Hashtable<String, String> codeSystemToIndexMap_;
    
    /** The boundry doc id set map. */
    private HashMap<String, DocIdSet> boundryDocIdSetMap;

    /**
     * Gets the logger.
     * 
     * @return the logger
     */
    protected LgLoggerIF getLogger() {
        return LoggerFactory.getLogger();
    }
    
    /**
     * Instantiates a new index interface.
     * 
     * @param service the service
     */
    public IndexInterface(IndexerService service) {
    	this.service_ = service;
    	try {
			init();
		} catch (Exception e) {
			throw new RuntimeException("There was an unexpected error while initializing the index service. Exception: " + e);
		} 
    }

    /**
     * Instantiates a new index interface.
     * 
     * @param location the location
     */
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
    /**
     * Inits the.
     * 
     * @throws LBInvocationException the LB invocation exception
     * @throws UnexpectedInternalError the unexpected internal error
     */
    private void init() throws LBInvocationException, UnexpectedInternalError {
        indexSearchers_ = new Hashtable<String, SearchServiceInterface>();
        indexReaders_ = new Hashtable<String, LuceneIndexReader>();
        boundryDocIdSetMap = new HashMap<String, DocIdSet>();
       
        initCodingSchemes();
    }

    /**
     * Inits the coding schemes.
     * 
     * @throws LBInvocationException the LB invocation exception
     */
    public void initCodingSchemes() throws LBInvocationException {
        Hashtable<String, String> temp = new Hashtable<String, String>();

        try {
            service_.refreshAvailableIndexes();
        } catch (RuntimeException e) {
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
            for (int i = 0; i < codingSchemeVersionPairs.length; i++) {
            	temp.put(codingSchemeVersionPairs[i], service_.getMetaData().getIndexMetaDataValue(codingSchemeVersionPairs[i]));
            }
            codeSystemToIndexMap_ = temp;
        } catch (RuntimeException e) {
            throw new RuntimeException("There was a problem reading the index metadata.", e);
        }
    }

    /**
     * Gets the code system keys.
     * 
     * @return the code system keys
     */
    public ArrayList<String> getCodeSystemKeys() {
        ArrayList<String> keys = new ArrayList<String>();
        Enumeration<String> e = codeSystemToIndexMap_.keys();
        while (e.hasMoreElements()) {
            keys.add(e.nextElement());
        }
        return keys;
    }

    /**
     * Reopen meta data index reader.
     */
    public void reopenMetaDataIndexReader() {
        // clear out the current reader, it will be reopened when necessary.
        String indexName = SystemVariables.getMetaDataIndexName();
        indexReaders_.remove(indexName);
        indexSearchers_.remove(indexName);
    }

    /**
     * Reopen index.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @throws LBInvocationException the LB invocation exception
     */
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
        } catch (Exception e1) {
            String id = getLogger().error("Problem reopening index", e1);
            throw new LBInvocationException("Unexpected problem reopening index", id);
        }
        
        boundryDocIdSetMap.remove(indexName);

    }

    /**
     * Delete index.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @throws InternalException the internal exception
     */
    public void deleteIndex(String internalCodeSystemName, String internalVersionString) throws InternalException {
        try {
            LocalCodingScheme lcs = new LocalCodingScheme();
            lcs.codingSchemeName = internalCodeSystemName;
            lcs.version = internalVersionString;

            // remove it from the stored hash tables.

            String indexName = codeSystemToIndexMap_.get(lcs.getKey());
            codeSystemToIndexMap_.remove(lcs.getKey());

            indexSearchers_.remove(indexName);

            LuceneIndexReader lir = indexReaders_.get(indexName);
            if (lir != null) {
                lir.close();
            }

            indexReaders_.remove(indexName);

            // clean up the index metadata file.

            //service_.getMetaData().removeAllIndexMetaDataValue(indexName);
            service_.getMetaData().removeIndexMetaDataValue(lcs.getKey());

            // delete the index
            service_.deleteIndex(indexName);
        } catch (Exception e) {
            throw new InternalException("Problem trying to delete an item from the index interface", e);
        }
    }

    /**
     * Map code system to index name.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @return the string
     */
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

    /**
     * Gets the base indexer service.
     * 
     * @return the base indexer service
     */
    public IndexerService getBaseIndexerService() {
        return service_;
    }

    /**
     * Gets the index location.
     * 
     * @param internalCodeSystemName the internal code system name
     * @param internalVersionString the internal version string
     * 
     * @return the index location
     */
    public String getIndexLocation(String internalCodeSystemName, String internalVersionString) {
        try {
            String indexName = mapCodeSystemToIndexName(internalCodeSystemName, internalVersionString);
            return new File(new File(service_.getRootLocation()), indexName).getCanonicalPath();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}