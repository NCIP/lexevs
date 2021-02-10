
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