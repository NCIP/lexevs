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
package org.lexgrid.loader.rrf.processor;

import org.LexGrid.relations.AssociationSource;
import org.lexgrid.loader.database.key.AssociationPredicateKeyResolver;
import org.lexgrid.loader.processor.CodingSchemeIdAwareProcessor;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.wrappers.ParentIdHolder;
import org.springframework.batch.item.ItemProcessor;

/**
 * The Class RelAndRelaEntityAssnsToEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class RrfEntityAssnsToEntityProcessor extends CodingSchemeIdAwareProcessor implements ItemProcessor<Mrrel, ParentIdHolder<AssociationSource>> {
	
	private AssociationPredicateKeyResolver associationPredicateKeyResolver;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public ParentIdHolder<AssociationSource> process(Mrrel item) throws Exception {
		AssociationSource relation = buildEntityAssociationSource(item);
	
		return new ParentIdHolder<AssociationSource>(
				this.getCodingSchemeIdSetter(),
				this.associationPredicateKeyResolver.resolveKey(
							this.getCodingSchemeIdSetter().getCodingSchemeId(), getRelation(item)), 
							relation);
	}


	protected abstract AssociationSource buildEntityAssociationSource(Mrrel item) throws Exception;
	
	protected String getRelation(Mrrel item){
		return item.getRel();
	}
}
