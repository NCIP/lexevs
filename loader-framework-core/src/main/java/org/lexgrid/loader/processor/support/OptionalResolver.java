
package org.lexgrid.loader.processor.support;

/**
 * The Interface OptionalResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface OptionalResolver<T> {

	public boolean toProcess(T item);
}