
package org.lexgrid.loader.database.key;


public interface AssociationInstanceKeyResolver {

	public String resolveKey(String codingSchemeId, String associationInstanceId);

}