
package org.lexgrid.loader.processor.support;

import org.lexgrid.loader.data.property.IndividualIdSetter;

/**
 * The Class AbstractBasicPropertyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBasicPropertyResolver<I> implements PropertyResolver<I> {
	
	/** The entity code resolver. */
	private EntityCodeResolver<I> entityCodeResolver;
	
	/** The entity code resolver. */
	private EntityNamespaceResolver<I> entityNamespaceResolver;
	
	/** The individual id setter. */
	private IndividualIdSetter<I> individualIdSetter = 
		new org.lexgrid.loader.data.property.RandomGuidIndividualIdSetter<I>();

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(I item) {
		return entityCodeResolver.getEntityCode(item);
	}
	
	public String getEntityCodeNamespace(I item) {
		return entityNamespaceResolver.getEntityNamespace(item);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getId(java.lang.Object)
	 */
	public String getId(I item) {
		return individualIdSetter.addId(item);
	}
	
	/**
	 * Gets the entity code resolver.
	 * 
	 * @return the entity code resolver
	 */
	public EntityCodeResolver<I> getEntityCodeResolver() {
		return entityCodeResolver;
	}
	
	/**
	 * Sets the entity code resolver.
	 * 
	 * @param entityCodeResolver the new entity code resolver
	 */
	public void setEntityCodeResolver(EntityCodeResolver<I> entityCodeResolver) {
		this.entityCodeResolver = entityCodeResolver;
	}
	
	/**
	 * Gets the individual id setter.
	 * 
	 * @return the individual id setter
	 */
	public IndividualIdSetter<I> getIndividualIdSetter() {
		return individualIdSetter;
	}
	
	/**
	 * Sets the individual id setter.
	 * 
	 * @param individualIdSetter the new individual id setter
	 */
	public void setIndividualIdSetter(IndividualIdSetter<I> individualIdSetter) {
		this.individualIdSetter = individualIdSetter;
	}

	public void setEntityNamespaceResolver(EntityNamespaceResolver<I> entityNamespaceResolver) {
		this.entityNamespaceResolver = entityNamespaceResolver;
	}

	public EntityNamespaceResolver<I> getEntityNamespaceResolver() {
		return entityNamespaceResolver;
	}
}