
package org.lexevs.dao.index.access;

import java.util.List;

import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;

/**
 * The Class AbstractBaseIndexDao.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseIndexDao implements LexEvsIndexFormatVersionAwareDao{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.index.access.LexEvsIndexFormatVersionAwareDao#supportsLexEvsIndexFormatVersion(org.lexevs.dao.index.version.LexEvsIndexFormatVersion)
	 */
	public boolean supportsLexEvsIndexFormatVersion(LexEvsIndexFormatVersion version) {
		for(LexEvsIndexFormatVersion supportedVersion : doGetSupportedLexEvsIndexFormatVersions()){
			if(version.isEqualVersion(supportedVersion)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Do get supported lex evs index format versions.
	 * 
	 * @return the list< lex evs index format version>
	 */
	public abstract List<LexEvsIndexFormatVersion> doGetSupportedLexEvsIndexFormatVersions();
}