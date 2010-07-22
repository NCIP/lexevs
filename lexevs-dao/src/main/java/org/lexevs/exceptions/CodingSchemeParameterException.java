package org.lexevs.exceptions;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

public class CodingSchemeParameterException extends LBParameterException {

	private static final long serialVersionUID = -742865817743674751L;

	public CodingSchemeParameterException(AbsoluteCodingSchemeVersionReference ref, String message) {
		super("Coding Scheme URI: " + ref.getCodingSchemeURN() + " Version: " + ref.getCodingSchemeVersion() + " - ");
	}
}
