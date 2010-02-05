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

import java.util.ArrayList;
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
 * The Class GroupRrfEntityAssnsToEntityProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class GroupRrfEntityAssnsToEntityProcessor extends CodingSchemeNameAwareProcessor implements InitializingBean, ItemProcessor<List<Mrrel>, List<EntityAssnsToEntity>> {
	
	private List<EntityAssnToEQualsProcessor<Mrrel>> qualifierProcessors;
	
	private LexEvsDao lexEvsDao;
	
	private int maxCacheSize = 10;
	
	public List<EntityAssnsToEntity> process(List<Mrrel> items) throws Exception {
		
		List<EntityAssnsToEntity> processedAssociations = new ArrayList<EntityAssnsToEntity>();
		
		for(Mrrel mrrel : items){
			EntityAssnsToEntity assoc = buildEntityAssnsToEntity(mrrel);
			if(!this.listContainsEntityAssnsToEntity(assoc, processedAssociations)){
				processedAssociations.add(assoc);
				this.processQualifiers(assoc, mrrel);
			}
		}
		return processedAssociations;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	protected void processQualifiers(EntityAssnsToEntity relation, Mrrel item) throws Exception {
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
	}
	
	protected boolean listContainsEntityAssnsToEntity(EntityAssnsToEntity assoc, List<EntityAssnsToEntity> processedAssociations){
		for(EntityAssnsToEntity processedAssoc : processedAssociations){
			if(processedAssoc.getCodingSchemeName().equals(assoc.getCodingSchemeName()) &&
					processedAssoc.getContainerName().equals(assoc.getContainerName()) &&
					processedAssoc.getEntityCode().equals(assoc.getEntityCode()) &&
					processedAssoc.getEntityCodeNamespace().equals(assoc.getEntityCodeNamespace()) &&
					processedAssoc.getSourceEntityCode().equals(assoc.getSourceEntityCode()) &&
					processedAssoc.getSourceEntityCodeNamespace().equals(assoc.getSourceEntityCodeNamespace()) &&
					processedAssoc.getTargetEntityCode().equals(assoc.getTargetEntityCode()) &&
					processedAssoc.getTargetEntityCodeNamespace().equals(assoc.getTargetEntityCodeNamespace())){
				return true;
			}
		}
		return false;
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

	public int getMaxCacheSize() {
		return maxCacheSize;
	}

	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}	
}
