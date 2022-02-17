
package org.lexevs.dao.index.lucenesupport;

import java.io.File;

import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;

/**
 * The Interface LuceneDirectoryCreator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LuceneDirectoryCreator {
	
	/**
	 * Gets the directory.
	 * 
	 * @param indexName the index name
	 * @param baseDirectory the base directory
	 * 
	 * @return the directory
	 */
	public NamedDirectory getDirectory(String indexName, File baseDirectory);

}