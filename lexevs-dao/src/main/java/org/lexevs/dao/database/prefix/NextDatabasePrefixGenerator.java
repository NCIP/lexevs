
package org.lexevs.dao.database.prefix;

/**
 * The Interface NextDatabasePrefixGenerator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface NextDatabasePrefixGenerator {

	/**
	 * Generate next database prefix.
	 * 
	 * @param currentIdentitfier the current identitfier
	 * 
	 * @return the string
	 */
	public String generateNextDatabasePrefix(String currentIdentitfier);
}