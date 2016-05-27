/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexevs.dao.index.indexer;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.PropertyType;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.apache.lucene.document.Document;
import org.lexevs.dao.database.ibatis.entity.model.IdableEntity;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexevs.dao.index.lucene.v2010.entity.LuceneEntityDao;
import org.lexevs.dao.index.version.LexEvsIndexFormatVersion;
import org.lexevs.system.constants.SystemVariables;
import org.lexevs.system.service.SystemResourceService;

/**
 * The Class LuceneLoaderCodeIndexer.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LuceneLoaderCodeIndexer extends LuceneLoaderCode implements EntityIndexer {

	/** The system resource service. */
	private SystemResourceService systemResourceService;
	
	/** The system variables. */
	private SystemVariables systemVariables;
		
	/** The current index version. */
	private String currentIndexVersion = "2010";
	
	private LuceneEntityDao luceneEntityDao;

	/**
	 * Instantiates a new lucene loader code indexer.
	 */
	public LuceneLoaderCodeIndexer(){
		super();
	}
	
	/**
	 * Index entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param entity the entity
	 */
	public List<Document> indexEntity(
			String codingSchemeUri, 
			String codingSchemeVersion,
			Entity entity) {
		List<Document> returnList = new ArrayList<Document>();
		
		try {
			String codingSchemeName = 
				  systemResourceService.getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, codingSchemeVersion);
			
			String entityUid = null;
			if(entity instanceof IdableEntity) {
				entityUid = ((IdableEntity)entity).getId();
			}
			
			Document parentDoc = this.createParentDocument(
					codingSchemeName, 
					codingSchemeUri, 
					codingSchemeVersion, 
					entity.getEntityCode(),
					entity.getEntityCodeNamespace(),
					entity.getEntityDescription(),
					entity.getIsActive(),
					entity.getIsAnonymous(),
					entity.getIsDefined(),
					entity.getEntityType(),
					entity.getStatus(),
					entityUid,
					isParent);
			
			// TODO ensure entityCodeLowerCase is created in the child doc
			// TODO format was in the original index not present here.
			
			//returnList.add(parentDoc);
			
			if(entity.getAllProperties().length == 0) {
				entity.addPresentation(
						getDefaultPresentation(entity));
			}
			for(Property prop : entity.getAllProperties()) {
				returnList.add(
						this.indexEntity(codingSchemeUri, codingSchemeVersion, entity, prop));
			}
			
			returnList.add(parentDoc);
			
			return returnList;
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	protected Presentation getDefaultPresentation(Entity entity) {
		Presentation defaultPresentation = new Presentation();
		defaultPresentation.setPropertyType(PropertyType.PRESENTATION.toString());
		defaultPresentation.setPropertyName(PropertyType.PRESENTATION.toString());
		defaultPresentation.setValue(
				DaoUtility.createText(
						DaoUtility.getEntityDescriptionText(entity.getEntityDescription())));
		
		return defaultPresentation;
	}

	/**
	 * Index entity.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param codingSchemeVersion the coding scheme version
	 * @param entity the entity
	 * @param prop the prop
	 * 
	 * @throws Exception the exception
	 */
	protected Document indexEntity(String codingSchemeUri, String codingSchemeVersion,
			Entity entity, Property prop) throws Exception {
		
		boolean isPreferred = false;
		String degreeOfFidelity = "";
		boolean matchIfNoContext = false;
		String repForm = "";
		if(prop instanceof Presentation) {
			Presentation pres = (Presentation)prop;
			
			if(pres.isIsPreferred() == null) {
				isPreferred = false;
			} else {
				isPreferred = pres.isIsPreferred();
			}
			
			degreeOfFidelity = pres.getDegreeOfFidelity();
			
			if(pres.isMatchIfNoContext() == null) {
				matchIfNoContext = false;
			} else {
				matchIfNoContext = pres.isMatchIfNoContext();
			}
			repForm = pres.getRepresentationalForm();
		}

		String entityUid = null;
		if(entity instanceof IdableEntity) {
			entityUid = ((IdableEntity)entity).getId();
		}
		
		return this.addEntity(
				systemResourceService.
					getInternalCodingSchemeNameForUserCodingSchemeName(codingSchemeUri, codingSchemeVersion), 
				codingSchemeUri, 
				codingSchemeVersion,
				entityUid,
				entity.getEntityCode(), 
				entity.getEntityCodeNamespace(), 
				entity.getEntityType(),
				DaoUtility.getEntityDescriptionText(entity.getEntityDescription()),
				prop.getPropertyType(), 
				prop.getPropertyName(), 
				prop.getValue().getContent(), 
				entity.getIsActive(), 
				entity.getIsAnonymous(),
				prop.getValue().getDataType(), 
				prop.getLanguage(),
				isPreferred,
				entity.getStatus(),
				prop.getPropertyId(), 
				degreeOfFidelity,
				matchIfNoContext,
				repForm,
				sourceToString(prop.getSource()), 
				prop.getUsageContext(), 
				propertyQualifiersToQualifiers(prop.getPropertyQualifier()));
	}
	
	/**
	 * Property qualifiers to qualifiers.
	 * 
	 * @param qualifiers the qualifiers
	 * 
	 * @return the qualifier[]
	 */
	private Qualifier[] propertyQualifiersToQualifiers(PropertyQualifier[] qualifiers) {
		Qualifier[] quals = new Qualifier[qualifiers.length];
		for(int i=0;i<qualifiers.length;i++) {
			quals[i] = new Qualifier(qualifiers[i].getPropertyQualifierName(), qualifiers[i].getValue().getContent());
		}
		return quals;
	}
	
	/**
	 * Source to string.
	 * 
	 * @param sources the sources
	 * 
	 * @return the string[]
	 */
	private String[] sourceToString(Source[] sources) {
		String[] stringSource = new String[sources.length];
		for(int i=0;i<sources.length;i++) {
			stringSource[i] = sources[i].getContent();
		}
		return stringSource;
	}

	/**
	 * Gets the system resource service.
	 * 
	 * @return the system resource service
	 */
	public SystemResourceService getSystemResourceService() {
		return systemResourceService;
	}

	/**
	 * Sets the system resource service.
	 * 
	 * @param systemResourceService the new system resource service
	 */
	public void setSystemResourceService(SystemResourceService systemResourceService) {
		this.systemResourceService = systemResourceService;
	}

	/**
	 * Gets the system variables.
	 * 
	 * @return the system variables
	 */
	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	/**
	 * Sets the system variables.
	 * 
	 * @param systemVariables the new system variables
	 */
	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public LuceneEntityDao getLuceneEntityDao() {
		return luceneEntityDao;
	}

	public void setLuceneEntityDao(LuceneEntityDao luceneEntityDao) {
		this.luceneEntityDao = luceneEntityDao;
	}

	/**
	 * Sets the current index version.
	 * 
	 * @param currentIndexVersion the new current index version
	 */
	public void setCurrentIndexVersion(String currentIndexVersion) {
		this.currentIndexVersion = currentIndexVersion;
	}

	/**
	 * Gets the current index version.
	 * 
	 * @return the current index version
	 */
	public String getCurrentIndexVersion() {
		return currentIndexVersion;
	}

	@Override
	public LexEvsIndexFormatVersion getIndexerFormatVersion() {
		return LexEvsIndexFormatVersion.parseStringToVersion(this.getCurrentIndexVersion());
	}
}