
package org.lexevs.dao.database.operation.transitivity;

public interface TransitivityBuilder {
	
	public enum TransitivityTableState {COMPUTED, NOT_COMPUTED, NO_TRANSITIVE_ASSOCIATIONS_DEFINED}
	
	public void computeTransitivityTable(String codingSchemeUri, String codingSchemeVersion);

	public TransitivityTableState isTransitiveTableComputed(String codingSchemeUri,
			String codingSchemeVersion);

	public void reComputeTransitivityTable(String codingSchemeUri,
			String codingSchemeVersion);
	
	
}