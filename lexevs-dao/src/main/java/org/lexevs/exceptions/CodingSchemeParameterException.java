
package org.lexevs.exceptions;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.dao.database.utility.DaoUtility;

/**
 * The Class CodingSchemeParameterException.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeParameterException extends LBParameterException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -742865817743674751L;

	/**
	 * Instantiates a new coding scheme parameter exception.
	 * 
	 * @param ref the ref
	 * @param message the message
	 */
	public CodingSchemeParameterException(AbsoluteCodingSchemeVersionReference ref, String message) {
		super("Coding Scheme URI: " + ref.getCodingSchemeURN() + " Version: " + ref.getCodingSchemeVersion() + " - " + message);
	}
	
	/**
	 * Instantiates a new coding scheme parameter exception.
	 * 
	 * @param codingScheme the coding scheme
	 * @param message the message
	 */
	public CodingSchemeParameterException(CodingScheme codingScheme, String message) {
		this(DaoUtility.createAbsoluteCodingSchemeVersionReference(codingScheme.getCodingSchemeURI(), codingScheme.getRepresentsVersion()), message);
	}
}