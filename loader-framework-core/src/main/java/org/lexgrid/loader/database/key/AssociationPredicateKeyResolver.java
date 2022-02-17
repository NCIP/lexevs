
package org.lexgrid.loader.database.key;


public interface AssociationPredicateKeyResolver {

	public String resolveKey(
			String codingSchemeName, 
			String codingSchemeVersion, 
			String relationContainerName,
			String associationName);

}