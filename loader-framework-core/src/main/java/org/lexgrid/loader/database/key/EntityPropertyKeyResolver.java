
package org.lexgrid.loader.database.key;


public interface EntityPropertyKeyResolver {

	public String resolveKey(String codingSchemeUri, String codingSchemeVersion, String entityCode, String entityCodeNamespace, String propertyId);

}