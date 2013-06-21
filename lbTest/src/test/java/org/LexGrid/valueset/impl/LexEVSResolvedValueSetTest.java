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
package org.LexGrid.valueset.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.types.ProcessState;
import org.LexGrid.LexBIG.Extensions.Load.ResolvedValueSetDefinitionLoader;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Property;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.lexgrid.resolvedvalueset.LexEVSResolvedValueSetService;
import org.lexgrid.resolvedvalueset.impl.LexEVSResolvedValueSetServiceImpl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.helper.VSDServiceHelper;

/**
 * JUnit for Resolved Value Set Service.
 * 
 * @author <A HREF="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</A>
 */
public class LexEVSResolvedValueSetTest extends TestCase {
	
	LexEVSResolvedValueSetService service;	
	
	private LexEVSValueSetDefinitionServices vds_;
	
	public  void setUp(){
		service= new LexEVSResolvedValueSetServiceImpl();
	}
	@Test
	public void testLoadValueSetDefinition() throws Exception {
				
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

		ResolvedValueSetDefinitionLoader loader = (ResolvedValueSetDefinitionLoader) lbsm.getLoader("ResolvedValueSetDefinitionLoader");
		loader.load(new URI("SRITEST:AUTO:AllDomesticButGM"), null, null, null);

		while (loader.getStatus().getEndTime() == null) {
			Thread.sleep(2000);
		}
		assertTrue(loader.getStatus().getState().equals(ProcessState.COMPLETED));
		assertFalse(loader.getStatus().getErrorsLogged().booleanValue());

		lbsm.activateCodingSchemeVersion(loader.getCodingSchemeReferences()[0]);

	}

	@Test
	public void testListAllResolvedValueSets() throws Exception {
		List<CodingScheme> list= service.listAllResolvedValueSets();		
        assertTrue(list.size() > 0 );
        CodingScheme scheme = list.get(0);
    	for (Property prop :scheme.getProperties().getPropertyAsReference()){
    		if(prop.getPropertyName().equals(LexEVSValueSetDefinitionServices.RESOLVED_AGAINST_CODING_SCHEME_VERSION)){
    			System.out.println("Version: " + prop.getPropertyQualifier(0).getValue().getContent());
    		}
    	}
        LexBIGService lbs = LexBIGServiceImpl.defaultInstance();
        CodedNodeSet set = lbs.getCodingSchemeConcepts(scheme.getCodingSchemeName(), Constructors.createCodingSchemeVersionOrTag(null,scheme.getRepresentsVersion()));
        ResolvedConceptReferencesIterator refs = set.resolve(null, null, null);
        while(refs.hasNext()){
        	ResolvedConceptReference ref = refs.next();
        	System.out.println("Namespace: " + ref.getEntity().getEntityCodeNamespace());
        	System.out.println("Code: " + ref.getCode());
        	System.out.println("Description: " + ref.getEntityDescription().getContent());

        }
	}
    @Test
    public void testGetResolvedValueSetsforConceptReference(){
    	ConceptReference ref = new ConceptReference();
    	ref.setCode("005");
    	ref.setCodeNamespace("Automobiles");
    	ref.setCodingSchemeName("Automobiles");
    	List<CodingScheme> schemes = service.getResolvedValueSetsForConceptReference(ref);
    	assertTrue(schemes.size() > 0);
        }
     
        
    @Test
    public void testGetCodingSchemeMetadataForResolvedValueSetURI() throws URISyntaxException{
    	URI uri = new URI("SRITEST:AUTO:AllDomesticButGM");
    	CodingScheme scheme = service.getCodingSchemeMetaDataForValueSetURI(uri);
    	assertTrue(scheme.getProperties().getProperty(1).getPropertyName().equals("resolvedAgainstCodingSchemeVersion"));
    	assertTrue(scheme.getProperties().getProperty(1).getPropertyQualifier(0).getValue().getContent().equals("1.0"));
    }
	

}