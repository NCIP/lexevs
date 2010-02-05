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

import org.LexGrid.persistence.model.Association;
import org.LexGrid.persistence.model.AssociationId;
import org.lexgrid.loader.dao.template.SupportedAttributeTemplate;
import org.lexgrid.loader.processor.CodingSchemeNameAwareProcessor;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.dao.RrfPostProcessingDao;
import org.lexgrid.loader.rrf.model.Mrdoc;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.util.Assert;

/**
 * The Class MrdocAssociationProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdocAssociationProcessor extends CodingSchemeNameAwareProcessor implements ItemProcessor<List<Mrdoc>,List<Association>> {

	/** The rrf post processing dao. */
	private RrfPostProcessingDao rrfPostProcessingDao;
	
	/** The supported attribute template. */
	private SupportedAttributeTemplate supportedAttributeTemplate;
	
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	public List<Association> process(List<Mrdoc> items) throws Exception {
		List<Association> returnList = new ArrayList<Association>();
	
		String relationName = getRelationName(items);
		String reverseName = getReverseName(items);
		String expandedName = getExpandedName(items);
		
		boolean isRelationLoaded = rrfPostProcessingDao.doesRelationExistInEntityAssnToEntity(relationName);
		if(isRelationLoaded){
			List<String> containerNames = rrfPostProcessingDao.getRelationContainers(relationName);
			for(String containerName : containerNames){
			supportedAttributeTemplate.addSupportedAssociation(getCodingSchemeNameSetter().getCodingSchemeName(), 
					relationName, 
					relationName, 
					expandedName);
			returnList.add(buildAssociation(containerName, relationName, reverseName, expandedName));
			}
		} 
		return returnList;
	}
	
	/**
	 * Builds the association.
	 * 
	 * @param containerName the container name
	 * @param relationName the relation name
	 * @param reverseName the reverse name
	 * @param expandedName the expanded name
	 * 
	 * @return the association
	 */
	protected Association buildAssociation(String containerName, String relationName, String reverseName, String expandedName){
		Association assoc = new Association();
		AssociationId assocId = new AssociationId();
		assoc.setAssociationName(relationName);
		assoc.setForwardName(relationName);
		assoc.setInverseId(reverseName);
		assoc.setReverseName(reverseName);
		assoc.setIsTransitive(isTransitive(relationName));
		assoc.setIsNavigable(true);
		assoc.setEntityDescription(expandedName);
		assocId.setContainerName(containerName);
		assocId.setEntityCode(relationName);
		assocId.setCodingSchemeName(getCodingSchemeNameSetter().getCodingSchemeName());
		assocId.setEntityCodeNamespace(getCodingSchemeNameSetter().getCodingSchemeName());
		assoc.setId(assocId);	
		return assoc;
	}
	
	/**
	 * Checks if is transitive.
	 * 
	 * @param relationName the relation name
	 * 
	 * @return true, if is transitive
	 */
	protected boolean isTransitive(String relationName){
		return RrfLoaderConstants.TRANSITIVE_ASSOCIATIONS.contains(relationName);
	}
	
	/**
	 * Gets the expanded name.
	 * 
	 * @param items the items
	 * 
	 * @return the expanded name
	 */
	private String getExpandedName(List<Mrdoc> items){
		for(Mrdoc mrdoc : items){
			if(mrdoc.getType().equals(RrfLoaderConstants.RELATION_EXPANDED_FORM)){
				return mrdoc.getExpl();
			}
		}
		return null;
	}
	
	/**
	 * Gets the reverse name.
	 * 
	 * @param items the items
	 * 
	 * @return the reverse name
	 */
	private String getReverseName(List<Mrdoc> items){
		for(Mrdoc mrdoc : items){
			if(mrdoc.getType().endsWith(RrfLoaderConstants.RELATION_INVERSE)){
				return mrdoc.getExpl();
			}
		}
		return null;
	}
	
	/**
	 * Gets the relation name.
	 * 
	 * @param items the items
	 * 
	 * @return the relation name
	 */
	private String getRelationName(List<Mrdoc> items){
		Assert.notEmpty(items);
		return items.get(0).getValue();
	}

	/**
	 * Gets the rrf post processing dao.
	 * 
	 * @return the rrf post processing dao
	 */
	public RrfPostProcessingDao getRrfPostProcessingDao() {
		return rrfPostProcessingDao;
	}

	/**
	 * Sets the rrf post processing dao.
	 * 
	 * @param rrfPostProcessingDao the new rrf post processing dao
	 */
	public void setRrfPostProcessingDao(RrfPostProcessingDao rrfPostProcessingDao) {
		this.rrfPostProcessingDao = rrfPostProcessingDao;
	}

	/**
	 * Gets the supported attribute template.
	 * 
	 * @return the supported attribute template
	 */
	public SupportedAttributeTemplate getSupportedAttributeTemplate() {
		return supportedAttributeTemplate;
	}

	/**
	 * Sets the supported attribute template.
	 * 
	 * @param supportedAttributeTemplate the new supported attribute template
	 */
	public void setSupportedAttributeTemplate(
			SupportedAttributeTemplate supportedAttributeTemplate) {
		this.supportedAttributeTemplate = supportedAttributeTemplate;
	}
}
