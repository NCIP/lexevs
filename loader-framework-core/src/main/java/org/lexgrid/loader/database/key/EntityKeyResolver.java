
package org.lexgrid.loader.database.key;


public interface EntityKeyResolver {

	public String resolveKey(String codingSchemeUri, String codingSchemeVersion, String entityCode, String entityCodeNamespace) throws KeyNotFoundException;

}