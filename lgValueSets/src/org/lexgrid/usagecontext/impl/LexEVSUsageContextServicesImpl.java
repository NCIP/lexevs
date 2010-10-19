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
package org.lexgrid.usagecontext.impl;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.ServiceUtility;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.lexevs.dao.database.service.DatabaseServiceManager;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.system.service.SystemResourceService;
import org.lexgrid.usagecontext.LexEVSUsageContextServices;
import org.lexgrid.usagecontext.util.UsageContextConstants;

/**
 * Implementation of LexEVSUsageContextServices.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSUsageContextServicesImpl implements LexEVSUsageContextServices {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient LexBIGService lbsvc_;
	
	private static LexEVSUsageContextServices ucServ_;
	private DatabaseServiceManager databaseServiceManager = LexEvsServiceLocator.getInstance().getDatabaseServiceManager();
	
	public static LexEVSUsageContextServices defaultInstance(){
		if (ucServ_ == null)
			ucServ_ = new LexEVSUsageContextServicesImpl();
		
		return ucServ_;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextCodedNodeSet(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodedNodeSet getUsageContextCodedNodeSet(
			String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexBIGService().getNodeSet(codingSchemeNameOrURI, versionOrTag, 
				Constructors.createLocalNameList(UsageContextConstants.USAGE_CONTEXT_ENTITY_TYPE));
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextCodingScheme(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public CodingScheme getUsageContextCodingScheme(String codingSchemeNameOrURI, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		return getLexBIGService().resolveCodingScheme(codingSchemeNameOrURI, versionOrTag);
	}
	
	@Override
	public CodingSchemeSummary getUsageContextCodingSchemeSummary(String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag) throws LBException {
		String version = ServiceUtility.getVersion(codingSchemeNameOrURI, versionOrTag);
		
		return databaseServiceManager.getCodingSchemeService().
        	getCodingSchemeSummaryByUriAndVersion(getCodeSystemURI(codingSchemeNameOrURI, version), version);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextEntitisWithName(java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption, java.lang.String, java.lang.String)
	 */
	@Override
	public List<Entity> getUsageContextEntitisWithName(String usageContextName,
			String codingSchemeNameOrURI, CodingSchemeVersionOrTag versionOrTag,
			SearchDesignationOption option, String matchAlgorithm,
			String language) throws LBException {
		List<Entity> entityList = new ArrayList<Entity>();		
		CodedNodeSet cns = getUsageContextCodedNodeSet(codingSchemeNameOrURI, versionOrTag);
		
		if (cns != null)
		{
			cns.restrictToMatchingDesignations(usageContextName, option, matchAlgorithm, language);
			
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
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#getUsageContextEntity(java.lang.String, java.lang.String, java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public Entity getUsageContextEntity(String usageContextId, String namespace, String codingSchemeNameOrURI, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		Entity cdEntity = null;
		CodedNodeSet cns = this.getUsageContextCodedNodeSet(codingSchemeNameOrURI, versionOrTag);
		if (cns != null)
		{
			cns.restrictToCodes(Constructors.createConceptReferenceList(usageContextId));
			ResolvedConceptReferencesIterator rcrItr = cns.resolve(null, null, null, null, true);
			if (rcrItr != null && rcrItr.hasNext())
			{
				ResolvedConceptReference rcr = rcrItr.next();
				cdEntity = rcr.getEntity();
			}
		}
		return cdEntity;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#listAllUsageContextEntities(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<Entity> listAllUsageContextEntities(String codingSchemeNameOrURI, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		List<Entity> entityList = new ArrayList<Entity>();
		CodedNodeSet cns = getUsageContextCodedNodeSet(codingSchemeNameOrURI, versionOrTag);
		
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

	/*
	 * (non-Javadoc)
	 * @see org.lexgrid.usagecontext.LexEVSUsageContextServices#listAllUsageContextIds(java.lang.String, org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag)
	 */
	@Override
	public List<String> listAllUsageContextIds(String codingSchemeNameOrURI, 
			CodingSchemeVersionOrTag versionOrTag) throws LBException {
		List<String> idsList = new ArrayList<String>();
		List<Entity> entityList = listAllUsageContextEntities(codingSchemeNameOrURI, versionOrTag);
		for (Entity entity : entityList)
		{
			idsList.add(entity.getEntityCode());
		}
		return idsList;
	}

	private LexBIGService getLexBIGService(){
		if (lbsvc_ == null)
			lbsvc_ = LexBIGServiceImpl.defaultInstance();
		
		return lbsvc_;
	}
	
	private String getCodeSystemURI(String codeSystemNameOrUri, String version) throws LBParameterException{
		SystemResourceService systemResourceService = LexEvsServiceLocator.getInstance().getSystemResourceService();
		
		return systemResourceService.getUriForUserCodingSchemeName(codeSystemNameOrUri, version);
	}
}