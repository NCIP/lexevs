
package org.lexevs.dao.indexer.lucene.query;

import java.io.Serializable;

import org.apache.lucene.sandbox.queries.regex.JavaUtilRegexCapabilities;

/**
 * The Class SerializableRegexCapabilities.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SerializableRegexCapabilities extends JavaUtilRegexCapabilities implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 228065119675070565L;

    /**
     * Instantiates a new serializable regex capabilities.
     */
    public SerializableRegexCapabilities(){
        super();
    }
}