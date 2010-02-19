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
package org.lexgrid.loader.processor;

import org.LexGrid.relations.AssociationSource;
import org.LexGrid.relations.AssociationTarget;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.database.key.AssociationPredicateKeyResolver;
import org.lexgrid.loader.processor.support.RelationResolver;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityAssnsToEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityProcessor<I> extends CodingSchemeIdAwareProcessor implements ItemProcessor<I,ParentIdHolder<AssociationSource>> {
	
	/** The relation resolver. */
	private RelationResolver<I> relationResolver;
	
	/** The key resolver. */
	private AssociationInstanceIdResolver<I> associationInstanceIdResolver;
	
	private AssociationPredicateKeyResolver associationPredicateKeyResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationSource> process(I item) throws Exception {
		String rel = relationResolver.getRelation(item);
		if(StringUtils.isEmpty(rel)){
			return null;
		}
			
		AssociationSource source = new AssociationSource();
		AssociationTarget target = new AssociationTarget();
		
		source.setSourceEntityCode(relationResolver.getSource(item));
		source.setSourceEntityCodeNamespace(relationResolver.getSourceNamespace(item));
		
		target.setTargetEntityCode(relationResolver.getTarget(item));
		target.setTargetEntityCodeNamespace(relationResolver.getTargetNamespace(item));
		target.setAssociationInstanceId(associationInstanceIdResolver.resolveAssociationInstanceId(item));		
		
		target.setIsActive(true);
		target.setIsDefining(true);
		
		source.addTarget(target);

		String associationPredicateKey = associationPredicateKeyResolver.resolveKey(
				this.getCodingSchemeIdSetter().getCodingSchemeName(), 
				relationResolver.getRelation(item));
		
		return new ParentIdHolder<AssociationSource>(
				this.getCodingSchemeIdSetter(),
				associationPredicateKey, 
				source);	
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

	public AssociationInstanceIdResolver<I> getAssociationInstanceIdResolver() {
		return associationInstanceIdResolver;
	}

	public void setAssociationInstanceIdResolver(
			AssociationInstanceIdResolver<I> associationInstanceIdResolver) {
		this.associationInstanceIdResolver = associationInstanceIdResolver;
	}

	public AssociationPredicateKeyResolver getAssociationPredicateKeyResolver() {
		return associationPredicateKeyResolver;
	}

	public void setAssociationPredicateKeyResolver(
			AssociationPredicateKeyResolver associationPredicateKeyResolver) {
		this.associationPredicateKeyResolver = associationPredicateKeyResolver;
	}
}
