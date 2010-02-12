package org.lexgrid.loader.database.key;


public interface EntityKeyResolver {

	public String resolveKey(String codingSchemeId, String entityCode, String entityCodeNamespace);

}
