
package org.lexevs.dao.database.prefix;

/**
 * The Interface PrefixResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface PrefixResolver {

	public static String historyPrefix = "h_";
	/**
	 * Resolve default prefix.
	 * 
	 * @return the string
	 */
	public String resolveDefaultPrefix();

	/**
	 * Resolve prefix for coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 * 
	 * @return the string
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeUri, String version);
	
	/**
	 * Resolve prefix for coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the string
	 */
	public String resolvePrefixForCodingScheme(String codingSchemeId);
	
	/**
	 * Resolve prefix for history coding scheme.
	 * 
	 * @param codingSchemeId the coding scheme id
	 * 
	 * @return the string
	 */
	public String resolvePrefixForHistoryCodingScheme(String codingSchemeId);
	
	/**
	 * Gets the next coding scheme prefix.
	 * 
	 * @return the next coding scheme prefix
	 */
	public String getNextCodingSchemePrefix();

	/**
	 * Resolve history prefix.
	 * 
	 * @return the string
	 */
	public String resolveHistoryPrefix();
}