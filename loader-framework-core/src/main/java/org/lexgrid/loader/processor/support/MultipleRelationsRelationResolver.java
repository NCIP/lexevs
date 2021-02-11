
package org.lexgrid.loader.processor.support;

/**
 * The Interface MultipleRelationsRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface MultipleRelationsRelationResolver<T> extends RelationResolver<T> {
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelation(java.lang.Object)
	 */
	public String getRelation(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelationNamespace(java.lang.Object)
	 */
	public String getRelationNamespace(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getContainerName()
	 */
	public String getContainerName();
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSource(java.lang.Object)
	 */
	public String getSource(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceNamespace(java.lang.Object)
	 */
	public String getSourceNamespace(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTarget(java.lang.Object)
	 */
	public String getTarget(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTargetNamespace(java.lang.Object)
	 */
	public String getTargetNamespace(T item);
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceScheme(java.lang.Object)
	 */
	public String getSourceScheme(T item);
}