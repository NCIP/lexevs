
package org.lexevs.dao.database.key;

/**
 * The Interface KeyProvider.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface KeyProvider<I,O> {

	/**
	 * Gets the key.
	 * 
	 * @param record the record
	 * 
	 * @return the key
	 */
	O getKey(I record);
}