
package org.lexgrid.loader.database.key;

/**
 * The Class KeyNotFoundException.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class KeyNotFoundException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8751575597071563864L;
	
	/**
	 * Instantiates a new key not found exception.
	 * 
	 * @param entityCode the entity code
	 * @param entityCodeNamespace the entity code namespace
	 */
	public KeyNotFoundException(String entityCode, String entityCodeNamespace){
		super("Could not map Entity Code: " + entityCode + " - Namespace: " + entityCodeNamespace + " to an EntityUid.");
	}
}