package org.lexevs.dao.database.prefix;


public interface PrefixResolver {

	public String resolveDefaultPrefix();

	public String resolvePrefixForCodingScheme(String codingSchemeUri, String version);
	
	public String resolvePrefixForCodingScheme(String codingSchemeId);
	
	public String getNextCodingSchemePrefix();

	public String resolveHistoryPrefix();
}
