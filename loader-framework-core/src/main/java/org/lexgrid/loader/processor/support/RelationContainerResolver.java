
package org.lexgrid.loader.processor.support;

/**
 * The Interface RelationContainerResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RelationContainerResolver<I> {

	/**
	 * Gets the relation container.
	 * 
	 * @param item the item
	 * 
	 * @return the relation container
	 */
	public String getRelationContainer(I item);
}