
package org.lexevs.dao.index.access;

import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

/**
 * The Interface LexEvsIndexFormatVersionAwareDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsIndexFormatVersionAwareDao {

	/**
	 * Supports lex evs index format version.
	 * 
	 * @param version the version
	 * 
	 * @return true, if successful
	 */
	public boolean supportsLexEvsIndexFormatVersion(LexEvsIndexFormatVersion version);

}