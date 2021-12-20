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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final Logger logger = LogManager.getLogger("Indexer.Index");
    public final static String UNIQUE_DOCUMENT_IDENTIFIER_FIELD = "UNIQUE_DOCUMENT_IDENTIFIER_FIELD";

    private final File location_ ;

    /**
     * 
     * @param location
     *            Location on the disk to create the index
     * @param stopWords
     *            Optional list of stopwords (words not to index) to use in the
     *            StandardAnalyzer.
     */
    public Index(File location) {
        this.location_ = location;
    }

    /**
     * @return The folder that contains this index.
     */
    public File getLocation() {
        return this.location_;
    }

}