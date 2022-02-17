
package org.lexevs.dao.database.service.event.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;

/**
 * The Class CodingSchemeInsertEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class PostCodingSchemeInsertEvent {

	/** The coding scheme. */
	private CodingScheme codingScheme;

	
	/**
	 * Instantiates a new coding scheme insert event.
	 * 
	 * @param codingScheme the coding scheme
	 */
	public PostCodingSchemeInsertEvent(
			CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}

	/**
	 * Gets the coding scheme.
	 * 
	 * @return the coding scheme
	 */
	public CodingScheme getCodingScheme() {
		return codingScheme;
	}


	/**
	 * Sets the coding scheme.
	 * 
	 * @param codingScheme the new coding scheme
	 */
	public void setCodingScheme(CodingScheme codingScheme) {
		this.codingScheme = codingScheme;
	}
}