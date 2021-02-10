
package org.lexgrid.loader.processor.support;

/**
 * The Class AbstractBasicEntityResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBasicEntityResolver<T> implements EntityResolver<T>{

	/** The entity code resolver. */
	EntityCodeResolver<T> entityCodeResolver;
	
	/** The entity namespace resolver. */
	EntityNamespaceResolver<T> entityNamespaceResolver;
	
	/** The entity description resolver. */
	EntityDescriptionResolver<T> entityDescriptionResolver;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(T item) {
		return entityCodeResolver.getEntityCode(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getEntityCodeNamespace(java.lang.Object)
	 */
	public String getEntityCodeNamespace(T item){
		return entityNamespaceResolver.getEntityNamespace(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getEntityDescription(java.lang.Object)
	 */
	public String getEntityDescription(T item) {
		return entityDescriptionResolver.getEntityDescription(item);
	}

	/**
	 * Gets the entity code resolver.
	 * 
	 * @return the entity code resolver
	 */
	public EntityCodeResolver<T> getEntityCodeResolver() {
		return entityCodeResolver;
	}
	
	/**
	 * Sets the entity code resolver.
	 * 
	 * @param entityCodeResolver the new entity code resolver
	 */
	public void setEntityCodeResolver(EntityCodeResolver<T> entityCodeResolver) {
		this.entityCodeResolver = entityCodeResolver;
	}
	
	/**
	 * Gets the entity description resolver.
	 * 
	 * @return the entity description resolver
	 */
	public EntityDescriptionResolver<T> getEntityDescriptionResolver() {
		return entityDescriptionResolver;
	}
	
	/**
	 * Sets the entity description resolver.
	 * 
	 * @param entityDescriptionResolver the new entity description resolver
	 */
	public void setEntityDescriptionResolver(
			EntityDescriptionResolver<T> entityDescriptionResolver) {
		this.entityDescriptionResolver = entityDescriptionResolver;
	}

	/**
	 * Gets the entity namespace resolver.
	 * 
	 * @return the entity namespace resolver
	 */
	public EntityNamespaceResolver<T> getEntityNamespaceResolver() {
		return entityNamespaceResolver;
	}

	/**
	 * Sets the entity namespace resolver.
	 * 
	 * @param entityNamespaceResolver the new entity namespace resolver
	 */
	public void setEntityNamespaceResolver(
			EntityNamespaceResolver<T> entityNamespaceResolver) {
		this.entityNamespaceResolver = entityNamespaceResolver;
	}
}