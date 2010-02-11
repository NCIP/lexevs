package org.lexevs.dao.database.utility;

import org.lexevs.dao.database.access.codingscheme.CodingSchemeDao;

public class CodingSchemeUriVersionToUUID implements CodingSchemeIdMapper {
	
	private CodingSchemeDao codingSchemeDao;

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.utility.CodingSchemeIdMapper#mapCodingSchemeUriAndVersionToUUID(java.lang.String, java.lang.String)
	 */
	public String mapCodingSchemeUriAndVersionToUUID(String codingSchemeUri, String codingSchemeVersion){
		return codingSchemeDao.getCodingSchemeId(codingSchemeUri, codingSchemeVersion);
	}

	public CodingSchemeDao getCodingSchemeDao() {
		return codingSchemeDao;
	}

	public void setCodingSchemeDao(CodingSchemeDao codingSchemeDao) {
		this.codingSchemeDao = codingSchemeDao;
	}
}
