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

import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.EntityAssnsToEntity;
import org.LexGrid.persistence.model.EntityAssnsToEquals;
import org.lexgrid.loader.data.association.NoopKeyResolver;
import org.lexgrid.loader.processor.CodingSchemeNameAwareProcessor;
import org.lexgrid.loader.processor.EntityAssnToEQualsProcessor;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * The Class RelAndRelaEntityAssnsToEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class RrfEntityAssnsToEntityProcessor extends CodingSchemeNameAwareProcessor implements InitializingBean, ItemProcessor<Mrrel, EntityAssnsToEntity> {
	
	private List<EntityAssnToEQualsProcessor<Mrrel>> qualifierProcessors;
	
	private LexEvsDao lexEvsDao;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public EntityAssnsToEntity process(Mrrel item) throws Exception {
		EntityAssnsToEntity relation = buildEntityAssnsToEntity(item);
		
		if(qualifierProcessors != null){
			for(EntityAssnToEQualsProcessor<Mrrel> processor : qualifierProcessors){
				EntityAssnsToEquals qual = processor.process(item);
				
				//This is allowed to be null -- don't try to insert it then.
				if(qual != null) {
					qual.getId().setMultiAttributesKey(relation.getMultiAttributesKey());
					lexEvsDao.insert(qual);
				}
			}
		}
		
		return relation;
	}
		
	public void afterPropertiesSet() throws Exception {
		if(qualifierProcessors != null) {
			for(EntityAssnToEQualsProcessor<?> qualProcessor : qualifierProcessors) {
				Assert.isNull(qualProcessor.getKeyResolver(), 
						"Do not individually set the Qualifier Processor Key Resolvers. " +
				"The RrfEntityAssnsToEntity Processor will set the MultiAttribute Keys.");
				qualProcessor.setKeyResolver(new NoopKeyResolver());
			}
		}
	}

	protected abstract EntityAssnsToEntity buildEntityAssnsToEntity(Mrrel item) throws Exception;
	
	protected String getRelation(Mrrel item){
		return item.getRel();
	}

	public List<EntityAssnToEQualsProcessor<Mrrel>> getQualifierProcessors() {
		return qualifierProcessors;
	}

	public void setQualifierProcessors(
			List<EntityAssnToEQualsProcessor<Mrrel>> qualifierProcessors) {
		this.qualifierProcessors = qualifierProcessors;
	}

	public LexEvsDao getLexEvsDao() {
		return lexEvsDao;
	}

	public void setLexEvsDao(LexEvsDao lexEvsDao) {
		this.lexEvsDao = lexEvsDao;
	}	
}
