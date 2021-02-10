
package org.lexgrid.loader.data.association;

/**
 * The Interface AssociationInstanceIdResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface AssociationInstanceIdResolver<T> {
	
	/**
	 * Resolve multi attributes key.
	 * 
	 * @param key the key
	 * 
	 * @return the string
	 */
	public String resolveAssociationInstanceId(T key);

}