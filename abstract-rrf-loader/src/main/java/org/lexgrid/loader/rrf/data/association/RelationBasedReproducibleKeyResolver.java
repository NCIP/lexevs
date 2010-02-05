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
package org.lexgrid.loader.rrf.data.association;

import org.lexgrid.loader.data.association.AbstractReproducibleKeyResolver;
import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.processor.support.RelationResolver;

/**
 * The Class RelationBasedReproducibleKeyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RelationBasedReproducibleKeyResolver<I> extends AbstractReproducibleKeyResolver<I> {

	/** The relation resolver. */
	private RelationResolver<I> relationResolver;
	
	/** The entity code resolver. */
	private EntityCodeResolver<I> entityCodeResolver;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.association.AbstractReproducibleKeyResolver#resolveMultiAttributesKey(java.lang.Object)
	 */
	@Override
	public String resolveMultiAttributesKey(I key) {
		return super.generateKey(
		entityCodeResolver.getEntityCode(key),
		relationResolver.getContainerName(),
		relationResolver.getRelation(key),
		relationResolver.getSource(key),
		relationResolver.getSourceScheme(key),
		relationResolver.getTarget(key));	
	}
	
	/**
	 * Gets the relation resolver.
	 * 
	 * @return the relation resolver
	 */
	public RelationResolver<I> getRelationResolver() {
		return relationResolver;
	}
	
	/**
	 * Sets the relation resolver.
	 * 
	 * @param relationResolver the new relation resolver
	 */
	public void setRelationResolver(RelationResolver<I> relationResolver) {
		this.relationResolver = relationResolver;
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

}
