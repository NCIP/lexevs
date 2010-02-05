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

import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.data.association.MultiAttribKeyResolver;
import org.lexgrid.loader.processor.support.RelationResolver;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class EntityAssnsToEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityAssnsToEntityProcessor<I> extends CodingSchemeNameAwareProcessor implements ItemProcessor<I,EntityAssnsToEntity> {
	
	/** The relation resolver. */
	private RelationResolver<I> relationResolver;
	
	/** The key resolver. */
	private MultiAttribKeyResolver<I> keyResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityAssnsToEntity process(I item) throws Exception {
		String rel = relationResolver.getRelation(item);
		if(StringUtils.isEmpty(rel)){
			return null;
		}
		
		EntityAssnsToEntity relAssoc = new EntityAssnsToEntity();
		relAssoc.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		relAssoc.setEntityCodeNamespace(relationResolver.getRelationNamespace(item));
		relAssoc.setContainerName(relationResolver.getContainerName());
		relAssoc.setEntityCode(relationResolver.getRelation(item));
		relAssoc.setSourceEntityCode(relationResolver.getSource(item));
		relAssoc.setSourceEntityCodeNamespace(relationResolver.getSourceNamespace(item));
		relAssoc.setTargetEntityCode(relationResolver.getTarget(item));
		relAssoc.setTargetEntityCodeNamespace(relationResolver.getTargetNamespace(item));
		
		relAssoc.setIsActive(true);
		relAssoc.setIsDefining(true);
		relAssoc.setIsInferred(false);
		relAssoc.setMultiAttributesKey(keyResolver.resolveMultiAttributesKey(item));
		
		return relAssoc;	
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
	 * Gets the key resolver.
	 * 
	 * @return the key resolver
	 */
	public MultiAttribKeyResolver<I> getKeyResolver() {
		return keyResolver;
	}

	/**
	 * Sets the key resolver.
	 * 
	 * @param keyResolver the new key resolver
	 */
	public void setKeyResolver(MultiAttribKeyResolver<I> keyResolver) {
		this.keyResolver = keyResolver;
	}	
}
