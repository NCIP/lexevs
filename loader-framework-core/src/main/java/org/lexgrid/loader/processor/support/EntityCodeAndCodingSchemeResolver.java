
package org.lexgrid.loader.processor.support;

import org.lexgrid.loader.wrappers.CodeCodingSchemePair;

/**
 * The Interface EntityCodeAndCodingSchemeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface EntityCodeAndCodingSchemeResolver<T> {

	/**
	 * Gets the entity code and coding scheme.
	 * 
	 * @param item the item
	 * 
	 * @return the entity code and coding scheme
	 */
	public CodeCodingSchemePair getEntityCodeAndCodingScheme(T item);
}