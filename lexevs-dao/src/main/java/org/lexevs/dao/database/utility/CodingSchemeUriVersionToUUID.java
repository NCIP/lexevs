
package org.lexevs.dao.database.utility;

import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;

/**
 * The Class CodingSchemeUriVersionToUUID.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeUriVersionToUUID implements CodingSchemeIdMapper {
	
	/** The coding scheme dao. */
	private CodingSchemeDao codingSchemeDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.utility.CodingSchemeIdMapper#mapCodingSchemeUriAndVersionToUUID(java.lang.String, java.lang.String)
	 */
	public String mapCodingSchemeUriAndVersionToUUID(String codingSchemeUri, String codingSchemeVersion){
		return codingSchemeDao.getCodingSchemeUIdByUriAndVersion(codingSchemeUri, codingSchemeVersion);
	}

	/**
	 * Gets the coding scheme dao.
	 * 
	 * @return the coding scheme dao
	 */
	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}

	/**
	 * Sets the coding scheme dao.
	 * 
	 * @param codingSchemeDao the new coding scheme dao
	 */
	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}
}