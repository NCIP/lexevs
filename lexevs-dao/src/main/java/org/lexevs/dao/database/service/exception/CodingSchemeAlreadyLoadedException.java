
package org.lexevs.dao.database.service.exception;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * The Class CodingSchemeAlreadyLoadedException.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeAlreadyLoadedException extends LBException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -1857840295359784201L;

	/**
	 * Instantiates a new coding scheme already loaded exception.
	 * 
	 * @param uri the uri
	 * @param version the version
	 */
	public CodingSchemeAlreadyLoadedException(String uri, String version) {
		super("Coding Scheme URI: " + uri + " Version: " + version + " is already loaded in the system.");
	}
}