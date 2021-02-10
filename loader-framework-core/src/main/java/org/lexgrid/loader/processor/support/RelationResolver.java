
package org.lexgrid.loader.processor.support;

/**
 * The Interface RelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface RelationResolver<T> {
	
	/**
	 * Gets the relation.
	 * 
	 * @param item the item
	 * 
	 * @return the relation
	 */
	public String getRelation(T item);
	
	/**
	 * Gets the relation namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the relation namespace
	 */
	public String getRelationNamespace(T item);
	
	/**
	 * Gets the container name.
	 * 
	 * @return the container name
	 */
	public String getContainerName();
	
	/**
	 * Gets the source.
	 * 
	 * @param item the item
	 * 
	 * @return the source
	 */
	public String getSource(T item);
	
	/**
	 * Gets the source namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the source namespace
	 */
	public String getSourceNamespace(T item);
	
	/**
	 * Gets the target.
	 * 
	 * @param item the item
	 * 
	 * @return the target
	 */
	public String getTarget(T item);
	
	/**
	 * Gets the target namespace.
	 * 
	 * @param item the item
	 * 
	 * @return the target namespace
	 */
	public String getTargetNamespace(T item);
	
	/**
	 * Gets the source scheme.
	 * 
	 * @param item the item
	 * 
	 * @return the source scheme
	 */
	public String getSourceScheme(T item);
}