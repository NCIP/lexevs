
package org.LexGrid.LexBIG.Extensions.Load.options;

import java.net.URI;
import java.util.List;

/**
 * The Interface URIOption.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface URIOption extends Option<URI> {

	/**
	 * Gets the allowed file extensions.
	 * 
	 * @return the allowed file extensions
	 */
	public List<String> getAllowedFileExtensions();
	
	/**
	 * Adds the allowed file extensions.
	 * 
	 * @param allowedFileExtensions the allowed file extensions
	 */
	public void addAllowedFileExtensions(String allowedFileExtensions);

	/**
	 * Checks if is folder.
	 * 
	 * @return true, if is folder
	 */
	public boolean isFolder();
	
}