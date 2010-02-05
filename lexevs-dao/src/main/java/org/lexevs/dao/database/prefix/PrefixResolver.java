package org.lexevs.dao.database.prefix;


public interface PrefixResolver {

	public String resolvePrefix();

	public String resolvePrefixForCodingScheme(String codingSchemeName, String version);
	
	public String resolvePrefixForCodingScheme(String codingSchemeId);
	
	public String getNextCodingSchemePrefix();
}
