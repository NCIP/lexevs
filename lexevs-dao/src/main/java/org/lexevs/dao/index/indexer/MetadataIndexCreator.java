
package org.lexevs.dao.index.indexer;

import java.net.URI;

/**
 * The Interface IndexCreator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MetadataIndexCreator {

	 public void indexMetadata(
	    		String codingSchemeRegisteredName, 
	    		String codingSchemeVersion, 
	    		URI metaDataLocation,  
	    		boolean appendToExistingMetaData) throws Exception;
}