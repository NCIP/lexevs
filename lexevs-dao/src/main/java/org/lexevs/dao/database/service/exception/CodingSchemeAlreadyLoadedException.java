package org.lexevs.dao.database.service.exception;

import org.LexGrid.LexBIG.Exceptions.LBException;

public class CodingSchemeAlreadyLoadedException extends LBException{

	private static final long serialVersionUID = -1857840295359784201L;

	public CodingSchemeAlreadyLoadedException(String uri, String version) {
		super("Coding Scheme URI: " + uri + " Version: " + version + " is already loaded in the system.");
	}
}
