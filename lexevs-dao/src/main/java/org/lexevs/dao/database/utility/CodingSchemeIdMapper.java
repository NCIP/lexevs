
package org.lexevs.dao.database.utility;

/**
 * The Interface CodingSchemeIdMapper.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodingSchemeIdMapper {

	/**
	 * Map coding scheme uri and version to uuid.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * 
	 * @return the string
	 */
	public String mapCodingSchemeUriAndVersionToUUID(String codingSchemeUri,
			String codingSchemeVersion);

}