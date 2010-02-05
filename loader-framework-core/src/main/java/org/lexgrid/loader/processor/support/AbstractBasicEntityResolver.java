/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
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
