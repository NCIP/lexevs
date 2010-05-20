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
package org.lexgrid.conceptdomain.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.EntityDescription;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.commonTypes.Text;
import org.LexGrid.concepts.Entity;
import org.LexGrid.concepts.Presentation;
import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.apache.commons.lang.StringUtils;
import org.lexevs.dao.database.service.codingscheme.CodingSchemeService;
import org.lexevs.dao.database.service.entity.EntityService;
import org.lexevs.dao.index.service.entity.EntityIndexService;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexgrid.conceptdomain.LexEVSConceptDomainServices;
import org.lexgrid.conceptdomain.util.CodingSchemeBuilder;
import org.lexgrid.conceptdomain.util.ConceptDomainConstants;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 *Implements LexEVS Concept Domain API.
 *
 *@author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSConceptDomainServicesImpl implements LexEVSConceptDomainServices {
	
	private CodingSchemeService csServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getCodingSchemeService();
	private EntityService entityServ_ = LexEvsServiceLocator.getInstance().getDatabaseServiceManager().getEntityService();
	private EntityIndexService eIdxServ_ = LexEvsServiceLocator.getInstance().getIndexServiceManager().getEntityIndexService();
	
	private LexBIGService lbsvc_;
	private LexEVSValueSetDefinitionServices vsd_;
	
	private static LexEVSConceptDomainServices cdServ_;
	
	public static LexEVSConceptDomainServices defaultInstance(){
		if (cdServ_ == null)
			cdServ_ = new LexEVSConceptDomainServicesImpl();
		
		return cdServ_;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainCodingScheme()
	 */
	@Override
	public CodingScheme getConceptDomainCodingScheme() throws LBException {	
		return getLexBIGService().resolveCodingScheme(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTag(null, 
						ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION));
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainCodedNodeSet()
	 */
	@Override
	public CodedNodeSet getConceptDomainCodedNodeSet() throws LBException {
		return getLexBIGService().getNodeSet(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTag(null, 
						ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION), 
						Constructors.createLocalNameList(ConceptDomainConstants.CONCEPT_DOMAIN_ENTITY_TYPE));
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#listAllConceptDomainEntities()
	 */
	@Override
	public List<Entity> listAllConceptDomainEntities() throws LBException {
		List<Entity> entityList = new ArrayList<Entity>();
		CodedNodeSet cns = getConceptDomainCodedNodeSet();
		
		if (cns != null)
		{
			ResolvedConceptReferencesIterator rcrIter = cns.resolve(null, null, null);
			while (rcrIter.hasNext())
			{
				ResolvedConceptReference rcr = rcrIter.next();
				entityList.add(rcr.getEntity());
			}
		}
		return entityList;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#listAllConceptDomainIds()
	 */
	@Override
	public List<String> listAllConceptDomainIds() throws LBException {
		List<String> idsList = new ArrayList<String>();
		List<Entity> entityList = listAllConceptDomainEntities();
		for (Entity entity : entityList)
		{
			idsList.add(entity.getEntityCode());
		}
		return idsList;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getValueSetDefinitionURIsForConceptDomain(java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURIsForConceptDomain(
			String conceptDomainId) throws LBException {
		return getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomain(conceptDomainId);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#insertConceptDomain(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.LexGrid.commonTypes.Properties)
	 */
	@Override
	public void insertConceptDomain(String conceptDomainId,
			String conceptDomainName, String description, String status,
			Properties properties) throws LBException {
		// create an entity object for concept domain
		Entity entity = new Entity();
		entity.setEntityCode(conceptDomainId);
		entity.setEntityCodeNamespace(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_FORMAL_NAME);
		EntityDescription ed = new EntityDescription();
		ed.setContent(description);
		entity.setEntityDescription(ed);
		entity.setStatus(status);
		entity.addEntityType(ConceptDomainConstants.CONCEPT_DOMAIN_ENTITY_TYPE);
		
		if (StringUtils.isNotEmpty(conceptDomainName))
		{
			Presentation pres = new Presentation();
			pres.setPropertyName(SQLTableConstants.TBLCOLVAL_TEXTUALPRESENTATION);
			Text text = new Text();
			text.setContent(conceptDomainName);
			pres.setValue(text);
			pres.setIsPreferred(true);
			
			entity.addPresentation(pres);
		}
		
		if (properties != null)
			entity.addAnyProperties(properties.getPropertyAsReference());
		
		insertConceptDomain(entity);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#insertConceptDomain(org.LexGrid.concepts.Entity)
	 */
	@Override
	public void insertConceptDomain(Entity conceptDomain) throws LBException {
		if (conceptDomain == null)
			return;
		
		if (!validateType(conceptDomain))
		{
			throw new LBException("Invalid entity type found in entity object. Only valid entity type for conecot domain entity is 'conceptDomain'");
		}
		
		CodingScheme cs = null;
		
		try {
				cs = getLexBIGService().resolveCodingScheme(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTag(null, 
						ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION));
		} catch (LBParameterException e){ // if concept domain coding scheme does not exists in the system, create it.
			if (e.getMessage().indexOf("No URI found") != -1)
			{
				CodingSchemeBuilder csBuilder = new CodingSchemeBuilder();
				cs = csBuilder.build(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI,
					ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_FORMAL_NAME,
					ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION,
					ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_FORMAL_NAME,
					null, 
					null, 
					null, 
					null);
				
				// insert concept domain coding scheme into system
				csServ_.insertCodingScheme(cs, null);
				
				AbsoluteCodingSchemeVersionReference acsvr = Constructors.createAbsoluteCodingSchemeVersionReference(
						ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI, 
						ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION);
				
				// activate concept domain coding scheme
				getLexBIGService().getServiceManager(null).activateCodingSchemeVersion(acsvr);
				
				// create empty Lucene entry for concept domain coding scheme
				eIdxServ_.createIndex(acsvr);				
			}
			else
			{
				throw new LBException("Problem inserting concept domain", e);
			}
		}
		
		// insert concept domain
		entityServ_.insertEntity(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI, 
				ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION, conceptDomain);
		
		// create lucene index for newly create concept domain
		eIdxServ_.addEntityToIndex(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI,
				ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION, conceptDomain);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainEntity(java.lang.String)
	 */
	@Override
	public Entity getConceptDomainEntity(String conceptDomainId) throws LBException {
		return entityServ_.getEntity(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI, 
				ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION,
				conceptDomainId, 
				ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_FORMAL_NAME);
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#getConceptDomainEntitiesWithName(java.lang.String)
	 */
	@Override
	public List<Entity> getConceptDomainEntitisWithName(String conceptDomainName) throws LBException {		
		List<Entity> entityList = new ArrayList<Entity>();		
		CodedNodeSet cns = getConceptDomainCodedNodeSet();
		
		if (cns != null)
		{
			cns.restrictToMatchingDesignations(conceptDomainName, null, MatchAlgorithms.LuceneQuery.name(), null);
			
			if (cns != null)
			{
				ResolvedConceptReferencesIterator rcrIter = cns.resolve(null, null, null);
				while (rcrIter.hasNext())
				{
					ResolvedConceptReference rcr = rcrIter.next();
					entityList.add(rcr.getEntity());
				}
			}
		}
		
		return entityList;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#isEntityInConceptDomain(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList)
	 */
	public List<String> isEntityInConceptDomain(String conceptDomainId, String entityCode, AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionList) 
		throws LBException {
		if (StringUtils.isEmpty(conceptDomainId) || StringUtils.isEmpty(entityCode))
			return null;
		
		List<String> vsdURIs = new ArrayList<String>();
		
		List<String> allVSD = getValueSetDefinitionURIsForConceptDomain(conceptDomainId);
		
		for (String vsdURI : allVSD)
		{
			try {
				AbsoluteCodingSchemeVersionReference csvr = getValueSetDefinitionService().isEntityInValueSet(entityCode, 
						new URI(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_FORMAL_NAME), 
						new URI(vsdURI), codingSchemeVersionList, null);
				
				if (csvr != null)
					vsdURIs.add(vsdURI);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}		
		
		if (vsdURIs.size() == 0)
			vsdURIs = null;
		
		return vsdURIs;
	}
	
	/* (non-Javadoc)
	 * @see org.lexgrid.conceptdomain.LexEVSConceptDomainServices#removeConceptDomain(java.lang.String)
	 */
	@Override
	public void removeConceptDomain(String conceptDomainId) throws LBException {
		if (StringUtils.isEmpty(conceptDomainId))
			return;
		
		Entity entity = getConceptDomainEntity(conceptDomainId);
		
		if (entity != null)
		{
			// remove from database
			entityServ_.removeEntity(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI,
				ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION,
				entity);
			// remove from lucene index
			eIdxServ_.deleteEntityFromIndex(ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_URI,
				ConceptDomainConstants.CONCEPT_DOMAIN_CODING_SCHEME_VERSION,
				entity);
		}
		else
		{
			throw new LBException("No concept domain entity found with id : " + conceptDomainId);
		}
	}
	
	private boolean validateType(Entity conceptDomain){
		if (conceptDomain.getEntityTypeCount() == 0)
			return false;
		
		for (String type : conceptDomain.getEntityTypeAsReference())
		{
			if (type.equalsIgnoreCase(ConceptDomainConstants.CONCEPT_DOMAIN_ENTITY_TYPE))
				return true;
		}
		
		return false;
	}
	
	private LexBIGService getLexBIGService(){
		if (lbsvc_ == null)
			lbsvc_ = LexBIGServiceImpl.defaultInstance();
		
		return lbsvc_;
	}
	
	private LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
		if (vsd_ == null)
			vsd_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		
		return vsd_;
	}
}
