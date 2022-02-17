
package org.lexevs.dao.database.service.event.codingscheme;

import org.LexGrid.codingSchemes.CodingScheme;

/**
 * The Class CodingSchemeInsertErrorEvent.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeInsertErrorEvent<T extends Exception> {

	/** The original coding scheme. */
	private CodingScheme codingScheme;
	
	/** The exception. */
	private T exception;

	/**
	 * Instantiates a new coding scheme insert error event.
	 * 
	 * @param codingScheme the coding scheme
	 * @param exception the exception
	 */
	public CodingSchemeInsertErrorEvent(CodingScheme codingScheme,
			T exception) {
		super();
		this.codingScheme = codingScheme;
		this.exception = exception;
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

	/**
	 * Gets the exception.
	 * 
	 * @return the exception
	 */
	public T getException() {
		return exception;
	}

	/**
	 * Sets the exception.
	 * 
	 * @param exception the new exception
	 */
	public void setException(T exception) {
		this.exception = exception;
	}
}