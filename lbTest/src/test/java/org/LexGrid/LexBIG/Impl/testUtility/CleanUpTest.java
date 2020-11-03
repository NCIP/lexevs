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
package org.LexGrid.LexBIG.Impl.testUtility;

import junit.framework.TestCase;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.History.HistoryService;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexBIGServiceManagerImpl;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.function.query.TestPostLoadManifest;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.admin.Util;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * This test removes the terminologies loaded by the JUnit tests.
 * 
 * @author <A HREF="mailto:armbrust.daniel@mayo.edu">Dan Armbrust</A>
 * @version subversion $Revision: $ checked in on $Date: $
 */
public class CleanUpTest extends TestCase {

    public void testRemoveAutombiles() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.1", "1.0");
        
        AbsoluteCodingSchemeVersionReference b = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
        		"urn:oid:11.11.0.1.1-extension", "1.0-extension");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.deactivateCodingSchemeVersion(b, null);
        lbsm.unRegisterCodingSchemeAsSupplement(a, b);

        lbsm.removeCodingSchemeVersion(a);
        lbsm.removeCodingSchemeVersion(b);
    }

    public void testRemoveGermanMadeParts() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:11.11.0.2", "2.0");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveHistory() throws LBException {
    	LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceManagerImpl lbsm = (LexBIGServiceManagerImpl) ServiceHolder.instance().getLexBIGService()
                .getServiceManager(null);
        lbsm.removeHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
        
        try {
        	lbsi.getHistoryService("http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#");
		} catch (Exception e) {
			//expected
			return;
		}
		fail("Returned removed History Service.");
    }
    
    public void testRemoveMetaHistory() throws LBException {
        LexBIGService lbsi = ServiceHolder.instance().getLexBIGService();
        LexBIGServiceManager lbmn = lbsi.getServiceManager(null);

        lbmn.removeHistoryService(HistoryService.metaURN);
        try {
			lbsi.getHistoryService(HistoryService.metaURN);
		} catch (Exception e) {
			//expected
			return;
		}
		fail("Returned removed History Service.");
    }

    public void testRemoveBoostScheme() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:77.77.77.77", "1.0");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
    public void testRemoveObo() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:lsid:bioontology.org:cell", "UNASSIGNED");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveMedDRA() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
        		LexBIGServiceTestCase.MEDDRA_URN, LexBIGServiceTestCase.MEDDRA_VERSION);

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
    }
    
	public  void testRemoveOwl2Snippet() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_VOCABULARY_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_VOCABULARY_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}
	
	public  void testRemoveOwl2SnippetIndividuals() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}
	
	public  void testRemoveOwl2SnippetPrimitivess() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_PRIMITIVE_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}
	
	public  void testRemoveOwl2SnippetIndividualsUnannotated() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_UNANNOTATED_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}
	
	public  void testRemoveOwl2SnippetPrimitivessUnannotated() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_PRIMITIVE_UNANNOTATED_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}

	public  void testRemoveOwl2SnippetSpecialCases() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_INDIVIDUAL_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}
	public  void testRemoveOwl2SnippetSpecialCasesNamespace() throws LBException {
		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService()
				.getServiceManager(null);

		AbsoluteCodingSchemeVersionReference a = ConvenienceMethods
				.createAbsoluteCodingSchemeVersionReference(
						LexBIGServiceTestCase.OWL2_SNIPPET_INDIVIDUAL_URN,
						LexBIGServiceTestCase.OWL2_SNIPPET_SPECIAL_CASE_NAMESPACE_VERSION);

		lbsm.deactivateCodingSchemeVersion(a, null);
		lbsm.removeCodingSchemeVersion(a);

	}
    public void testRemoveOwl() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "05.09.bvt");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
    }
    
    public void testRemoveOwlThesaurus() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://www.co-ode.org/ontologies/pizza/2005/05/16/pizza.owl#", "version 1.2");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveOwlLoaderPreferences() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "CameraRegisteredName", "CameraV1");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveGenericOwl() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://www.co-ode.org/ontologies/amino-acid/2006/05/18/amino-acid.owl#",
                "version 1.2, copyright The University of Mancheste");

        try {
            lbsm.deactivateCodingSchemeVersion(a, null);
        } catch (Exception e) {
            // It means the Coding Scheme manifest was loaded. so to match up
            // try with manifest entries.
            a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference("AminoAcidRegisteredNameFromManifest",
                    "2005/10/11");

            lbsm.deactivateCodingSchemeVersion(a, null);
        }

        lbsm.removeCodingSchemeVersion(a);
    }

    public void testRemoveGenericOwlWithInstanceData() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://www.OntoReason.com/Ontologies/OvarianMass_SNOMED_ValueSets.owl",
                "UNASSIGNED");
        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
    }
    
    
    public void testRemoveOwlLoaderCompProps() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#", "05.09.comp.prop.bvt");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    
    public void testRemoveOwlLoaderNpoTest() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "http://purl.bioontology.org/ontology/npo", "TestForMultiNamespace");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }
    public void testRemoveMeta() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:2.16.840.1.113883.3.26.1.2", "200510.bvt");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveMeta2() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:2.16.840.1.113883.3.26.1.2", "200902_For_Test");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveUMLS() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:2.16.840.1.113883.6.110", "1993.bvt");

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);

    }

    public void testRemoveMetaData1() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "test.1", "1.0");

        lbsm.removeCodingSchemeVersionMetaData(a);

    }

    public void testRemoveMetaData2() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "test.2", "2.0");

        lbsm.removeCodingSchemeVersionMetaData(a);

    }

    public void testRemoveHL7MIFVocabulary() throws LBException {
        LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
        		LexBIGServiceTestCase.HL7_MIF_VOCABULARY_URN, LexBIGServiceTestCase.HL7_MIF_VOCABULARY_VERSION);

        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
    }

    
    public void testRemoveManifiestPostLoad() throws Exception {
    	try{
    		LexBIGServiceManager lbsm = ServiceHolder.instance().getLexBIGService().getServiceManager(null);
    		AbsoluteCodingSchemeVersionReference verRef = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
    				TestPostLoadManifest.NEW_URN, TestPostLoadManifest.NEW_VERSION);

    		lbsm.deactivateCodingSchemeVersion(verRef, null);
    		lbsm.removeCodingSchemeVersion(verRef);
    	}catch(Throwable e){
    		//don't fail -- some OS's/Test runners will remove this before it gets here
    	}

    }

	public void testRemoveDefaultMappingsScheme() throws LBException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN("http://default.mapping.container");
		scheme.setCodingSchemeVersion("1.0");
		lbsm.deactivateCodingSchemeVersion(scheme, null);
		lbsm.removeCodingSchemeVersion(scheme);

	}

	public void testRemoveFullMappingsScheme() throws LBException,
			LBInvocationException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN("Tested_URI");
		scheme.setCodingSchemeVersion("0.0");
		lbsm.deactivateCodingSchemeVersion(scheme, null);
		lbsm.removeCodingSchemeVersion(scheme);
	}
	
	public void testRemoveAuthoringShell() throws LBException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN("http://authoring.test.shell");
		scheme.setCodingSchemeVersion("1.1");
		lbsm.deactivateCodingSchemeVersion(scheme, null);
		lbsm.removeCodingSchemeVersion(scheme);
	}
	
	public void testRemoveMappingScheme() throws LBException {
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN("urn:oid:mapping:sample");
		scheme.setCodingSchemeVersion("1.0");
		lbsm.deactivateCodingSchemeVersion(scheme, null);
		lbsm.removeCodingSchemeVersion(scheme);
	}
	
	public void testRemoveMrMap2Mapping() throws LBException, LBInvocationException{
        LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);

        AbsoluteCodingSchemeVersionReference a = ConvenienceMethods.createAbsoluteCodingSchemeVersionReference(
                "urn:oid:CL413321.MDR.CST", "200909");
        
        lbsm.deactivateCodingSchemeVersion(a, null);
        lbsm.removeCodingSchemeVersion(a);
	}
	
	public void testRemoveValueSetArtifacts() throws LBException{
		LexBIGServiceManager lbsm = LexBIGServiceImpl.defaultInstance()
				.getServiceManager(null);
		AbsoluteCodingSchemeVersionReference scheme = new AbsoluteCodingSchemeVersionReference();
		scheme.setCodingSchemeURN("SRITEST:AUTO:AllDomesticButGM");
		scheme.setCodingSchemeVersion("12.03test");
		lbsm.deactivateCodingSchemeVersion(scheme, null);
		lbsm.removeCodingSchemeVersion(scheme);
		
		final LexEVSValueSetDefinitionServices vss = new LexEVSValueSetDefinitionServicesImpl();
		List<String> uris = vss.listValueSetDefinitionURIs();
		for (String urn : uris) {
			try {
				vss.removeValueSetDefinition(URI.create(urn));
			} catch (LBException e) {
				Util.displayAndLogError(e);
				e.printStackTrace();
			}
			Util.displayAndLogMessage("ValueSetDefinition removed: " + urn);
		}
		
		
	}
}