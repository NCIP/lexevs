package org.lexgrid.loader.database.key;


public interface EntityPropertyKeyResolver {

	public String resolveKey(String codingSchemeId, String entityCode, String entityCodeNamespace, String propertyId);

}
