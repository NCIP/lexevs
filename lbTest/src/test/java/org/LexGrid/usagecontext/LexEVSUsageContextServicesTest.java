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
package org.LexGrid.usagecontext;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.junit.Test;
import org.lexgrid.usagecontext.LexEVSUsageContextServices;
import org.lexgrid.usagecontext.impl.LexEVSUsageContextServicesImpl;
import org.lexgrid.usagecontext.util.UsageContextConstants;

/**
 * @author m004181
 *
 */
public class LexEVSUsageContextServicesTest extends TestCase {

	private LexEVSUsageContextServices ucServ_;
	private LexBIGService lbServ_;
	
	@Test
	public void testGetUsageContextCodingScheme() throws LBException {
		CodingScheme cs = getUsageContextService().getUsageContextCodingScheme(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, Constructors.createCodingSchemeVersionOrTag(null, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));
		assertTrue(cs.getCodingSchemeURI().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI));
		assertTrue(cs.getCodingSchemeName().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_FORMAL_NAME));
		assertTrue(cs.getRepresentsVersion().equals(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));		
	}

	@Test
	public void testGetUsageContextEntities() throws LBException {
		List<Entity> entities = getUsageContextService().listAllUsageContextEntities(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, Constructors.createCodingSchemeVersionOrTag(null, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));
		
		assertTrue(entities.size() == 2);
		for (Entity entity : entities)
		{
			assertTrue(entity.getEntityCode().equals("uc001") || entity.getEntityCode().equals("Autos"));
		}
	}

	@Test
	public void testGetUsageContextIds() throws LBException {
		
		List<String> cdIds = getUsageContextService().listAllUsageContextIds(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI, Constructors.createCodingSchemeVersionOrTag(null, 
				UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION));
		for (String id : cdIds)
		{
			assertTrue(id.equals("uc001") || id.equals("Autos"));
		}
	}

	@Test
	public void testRemoveUsageContextCodingSceheme() throws LBException{		
		LexBIGServiceManager lbsm = getLexBIGService().getServiceManager(null);
		AbsoluteCodingSchemeVersionReference acsvr = new AbsoluteCodingSchemeVersionReference();
		acsvr.setCodingSchemeURN(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_URI);
		acsvr.setCodingSchemeVersion(UsageContextConstants.USAGE_CONTEXT_DEFAULT_CODING_SCHEME_VERSION);
		lbsm.deactivateCodingSchemeVersion(acsvr, new Date());
		lbsm.removeCodingSchemeVersion(acsvr);
	}
	
	private LexEVSUsageContextServices getUsageContextService(){
		if (ucServ_ == null)
			ucServ_ = LexEVSUsageContextServicesImpl.defaultInstance();
		
		return ucServ_;
	}
	
	private LexBIGService getLexBIGService(){
		if (lbServ_ == null)
			lbServ_ = LexBIGServiceImpl.defaultInstance();
		
		return lbServ_;
	}

}