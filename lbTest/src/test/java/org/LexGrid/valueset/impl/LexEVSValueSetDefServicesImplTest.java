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

import static org.junit.Assert.assertNull;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.AnonymousOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.commonTypes.Source;
import org.LexGrid.naming.Mappings;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.naming.SupportedDataType;
import org.LexGrid.naming.SupportedPropertyQualifier;
import org.LexGrid.naming.SupportedSource;
import org.LexGrid.valueSets.DefinitionEntry;
import org.LexGrid.valueSets.EntityReference;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitionReference;
import org.LexGrid.valueSets.types.DefinitionOperator;
import org.apache.commons.lang.StringUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.dto.ResolvedValueSetCodedNodeSet;
import org.lexgrid.valuesets.dto.ResolvedValueSetDefinition;
import org.lexgrid.valuesets.helper.compiler.FileSystemCachingValueSetDefinitionCompilerDecorator;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * JUnit for Value Set Definition Services.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class LexEVSValueSetDefServicesImplTest extends TestCase {
	private LexEVSValueSetDefinitionServices vds_;
	
	@Test
	public void testGetAllValueSetsWithNoNames() throws LBException, URISyntaxException {
		List<String> uris = getValueSetDefinitionService().getAllValueSetDefinitionsWithNoName();
		ArrayList<String> blankURIS = new ArrayList<String>();
		
		for(String uri : uris)
		{
			blankURIS.add(uri);
		}
		
		assertTrue(blankURIS.contains("SRITEST:AUTO:AutomobilesNoName"));
		blankURIS.clear();
		uris.clear();
	}

	@Test
	public void testGetCodingSchemesInValueSetDef() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList csvrList = getValueSetDefinitionService().getCodingSchemesInValueSetDefinition(new URI("SRITEST:FA:HyphaInMycelium"));
		String urn = null;
		String version = null;
		
		for (int i = 0; i < csvrList.getAbsoluteCodingSchemeVersionReferenceCount(); i++){
			AbsoluteCodingSchemeVersionReference csvr = csvrList.getAbsoluteCodingSchemeVersionReference(i);
			urn = csvr.getCodingSchemeURN();
			version = csvr.getCodingSchemeVersion();
		}
		
        assertTrue(urn.equals("urn:lsid:bioontology.org:fungal_anatomy"));
        assertTrue(version == null || version.equals("UNASSIGNED"));
		
		csvrList = getValueSetDefinitionService().getCodingSchemesInValueSetDefinition(new URI("SRITEST:AUTO:Ford"));
		
		for (int i = 0; i < csvrList.getAbsoluteCodingSchemeVersionReferenceCount(); i++){
			AbsoluteCodingSchemeVersionReference csvr = csvrList.getAbsoluteCodingSchemeVersionReference(i);
			urn = csvr.getCodingSchemeURN();
			version = csvr.getCodingSchemeVersion();
		}
	}

	@Test
	public void testGetValueSetDefinition() throws LBException, URISyntaxException {
		ValueSetDefinition vdDef = getValueSetDefinitionService().getValueSetDefinition(new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		
		assertTrue(vdDef.getDefaultCodingScheme().equals("fungal_anatomy"));
		assertTrue(vdDef.getDefinitionEntry().length == 2);
		
		assertTrue(vdDef.getStatus().equals("ACTIVE"));
		assertTrue(vdDef.getIsActive());
		
		// value domain definition source
		List<Source> srcs = vdDef.getSourceAsReference();
		assertTrue(srcs.size() == 2);
		for(Source src : srcs)
		{
			String val = src.getContent();
			if (val.equals("OBO"))
			{
				assertTrue(src.getRole().equals("role1"));
				assertTrue(src.getSubRef().equals("subRef1"));
			}
			else if (val.equals("Fungal"))
			{
				assertTrue(src.getRole().equals("role2"));
				assertTrue(src.getSubRef().equals("subRef2"));
			}
		}
		srcs.clear();
		
		// value domain definition context		
		assertTrue(vdDef.getRepresentsRealmOrContextAsReference().size() == 3);		
		Iterator<String> cItr = vdDef.getRepresentsRealmOrContextAsReference().iterator();		
		while(cItr.hasNext())
		{
			String context = cItr.next();
			assertTrue(context.equals("OBO") || context.equals("Fungal") || context.equals("Anatomy"));
		}
		
		
		// value domain definition entry state
//		EntryState vdES = vdDef.getEntryState();
		
//		assertTrue(vdES.getContainingRevision().equals("R001"));
//		assertTrue(vdES.getPrevRevision().equals("R00A"));
//		assertTrue(vdES.getChangeType().name().equals(ChangeType.NEW.value()));
		
		Mappings maps = vdDef.getMappings();
		List<SupportedAssociation> supAssns = maps.getSupportedAssociationAsReference();
		
        assertTrue(supAssns.size() == 3);
        int matchCount = supAssns.size();
        for (SupportedAssociation supAssn : supAssns)
        {
            // Note: there should be two matches here - one for the local and one for the global
            // TODO Maybe we should eliminate duplicates?
            if(supAssn.getUri().equals("urn:lsid:bioontology.org:fungal_anatomy:is_a")) {
                matchCount--;
                assertTrue(supAssn.getLocalId().equals("is_a"));
            } else if(supAssn.getUri().equals("urn:oid:1.3.6.1.4.1.2114.108.1.8.1")) {
                matchCount--;
                assertTrue(supAssn.getLocalId().equals("hasSubtype"));
            } else if(supAssn.getUri().equals("urn:oid:11.11.0.1")) {
                matchCount--;
                assertTrue(supAssn.getLocalId().equals("uses"));
            }
        }
        assertTrue(matchCount == 0);
        supAssns.clear();
        
        List<SupportedDataType> supDTs = maps.getSupportedDataTypeAsReference();
        assertTrue(supDTs.size() == 3);
        matchCount = supDTs.size();
        for (SupportedDataType supDT : supDTs)
        {
            if(supDT.getLocalId().equals("testhtml")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getContent().equals("test/html"));
            } else if(supDT.getLocalId().equals("textplain")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getContent().equals("text/plain"));
            } if(supDT.getLocalId().equals("text")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getContent().equals("text"));
            }
        }
        assertTrue(matchCount == 0);
        supDTs.clear();
		
		List<SupportedPropertyQualifier> supPQs = maps.getSupportedPropertyQualifierAsReference();
		
		for(SupportedPropertyQualifier supPQ : supPQs)
		{
			assertTrue(supPQ.getLocalId().equals("PropQual A Namuuuuu") || supPQ.getLocalId().equals("PropQual B Namuuuuu"));
		}
		supPQs.clear();
		
        List<SupportedSource> supSrcs = maps.getSupportedSourceAsReference();
        assertTrue(supSrcs.size() == 4);
        for(SupportedSource supSrc : supSrcs)
        {
            assertTrue(supSrc.getLocalId().equals("OBO") ||
                       supSrc.getLocalId().equals("Fungal") || 
                       supSrc.getLocalId().equals("lexgrid.org") || 
                       supSrc.getLocalId().equals("_111101") );
        }
        supSrcs.clear();
		
		Property prop = (Property) vdDef.getProperties().getPropertyAsReference().get(0);
		
		assertTrue(prop.getValue().getContent().equals("microbial structure ontology AND HyphaInMycelium"));
		
		// property source
		assertTrue(prop.getSourceAsReference().size() == 2);
		Iterator<Source> srcItr = prop.getSourceAsReference().iterator();
		while(srcItr.hasNext()){
			Source source = srcItr.next();
			
			String srcValue = source.getContent();
			String role = source.getRole();
			String subRef = source.getSubRef();
			
			if (srcValue.equals("OBO"))
			{
				assertTrue(role.equals("PropRole1"));
				assertTrue(subRef.equals("PropSubRef1"));
			}
			else 
			{
				assertTrue(role.equals("PropRole2"));
				assertTrue(subRef.equals("PropSubRef2"));
			}
		}
		
		// property usage context
		assertTrue(prop.getUsageContextAsReference().size() == 2);
		cItr = prop.getUsageContextAsReference().iterator();
		while(cItr.hasNext())
		{
			String context = cItr.next();
			assertTrue(context.equals("PropUsageContext OBO") || context.equals("PropUsageContext Fungal"));
		}
		
		
		// property qualifier
		assertTrue(prop.getPropertyQualifierAsReference().size() == 2);
		Iterator<PropertyQualifier> pItr = prop.getPropertyQualifierAsReference().iterator();
		while(pItr.hasNext())
		{
			PropertyQualifier pQual = pItr.next();
			String value = pQual.getValue().getContent();
//			String dataType = pQual.getValue().getDataType();
			String type = pQual.getPropertyQualifierType();
			String name = pQual.getPropertyQualifierName();
			
			assertTrue(value.equals("PropQualValue MicrobialStructureOntology") || value.equals("PropQualValue HyphaInMycelium"));
			assertTrue(name.equals("PropQual A Namuuuuu") || name.equals("PropQual B Namuuuuu"));
			assertTrue(type.equals("pQual Type A") || type.equals("pQual Type B"));
		}
		
//		EntryState propES = prop.getEntryState();
		
		// entry state for this property
//		assertTrue(propES.getContainingRevision().equals("PR001"));
//		assertTrue(propES.getPrevRevision().equals("PR00A"));
//		assertTrue(propES.getChangeType().name().equals(ChangeType.NEW.value()));
	}
	
	@Test
	public void testGetValueSetDefinitionFromOWL2() throws LBException, URISyntaxException {
		ValueSetDefinition vdDef = getValueSetDefinitionService().
				getValueSetDefinition(new URI("OWL2LEXEVS:VerySickCancerPatient"), null);
		
		assertTrue(vdDef.getDefaultCodingScheme().equals("owl2lexevs"));
		assertTrue(vdDef.getStatus().equals("ACTIVE"));
		assertTrue(vdDef.getIsActive());
	}
	
	@Test
	public void testGetValueSetDefinitionFromAssertedValueSet() throws LBException, URISyntaxException {
		ValueSetDefinition vdDef = getValueSetDefinitionService().
				getValueSetDefinition(new URI("http://evs.nci.nih.gov/valueset/FDA/C54453"), null);
		
		assertTrue(vdDef.getDefaultCodingScheme().equals("owl2lexevs"));
		assertTrue(vdDef.getStatus().equals("ACTIVE"));
		assertTrue(vdDef.getIsActive());	
		assertNotNull(vdDef.getEntityDescription().getContent().equals("Terminology used for representation of "
				+ "the information on pharmaceutical product color in the framework of the Structured Product "
				+ "Labeling documents."));
	}
	
	@Test
	public void isBadValueSetDefinitionCreated() throws LBException, URISyntaxException{
		ValueSetDefinition def = getValueSetDefinitionService()
				.getValueSetDefinition(new URI("http://evs.nci.nih.gov/valueset/FDA/C117743"), null);
		assertNull(def);
	}
	
	@Test
	public void testResolveValueSetDefinitionFromOWL2NoAnonymousDefault() throws LBException, URISyntaxException {
		
		// Don't pass in an AnonymousOption. It will default to NON_ANONYMOUS_ONLY
		ResolvedValueSetCodedNodeSet resolvedVSDCNS = getValueSetDefinitionService().
				getCodedNodeSetForValueSetDefinition(new URI("OWL2LEXEVS:VerySickCancerPatient"), null, null, null);
		
		CodedNodeSet finalCNS = null;
		
		 if (resolvedVSDCNS != null) {
			 finalCNS = resolvedVSDCNS.getCodedNodeSet(); 
			 			 
			 ResolvedConceptReferencesIterator iterator = finalCNS.resolve(null, null, null, null, true);
			 
		     while (iterator.hasNext()) {
		    	 ResolvedConceptReference conRef = iterator.next();
		         assertFalse(conRef.getEntity().isIsAnonymous()); 
		     }
		 }	
	}
	
	@Test
	public void testResolveValueSetDefinitionFromOWL2NoAnonymousExplicit() throws LBException, URISyntaxException {
		
		// Explicitly pass in an AnonymousOption.NON_ANONYMOUS_ONLY. 
		ResolvedValueSetCodedNodeSet resolvedVSDCNS = getValueSetDefinitionService().
				getCodedNodeSetForValueSetDefinition(new URI("OWL2LEXEVS:VerySickCancerPatient"), null, null, null, 
						AnonymousOption.NON_ANONYMOUS_ONLY);
		
		CodedNodeSet finalCNS = null;
		
		 if (resolvedVSDCNS != null) {
			 finalCNS = resolvedVSDCNS.getCodedNodeSet(); 
			 			 
			 ResolvedConceptReferencesIterator iterator = finalCNS.resolve(null, null, null, null, true);
			 
		     while (iterator.hasNext()) {
	            ResolvedConceptReference conRef = iterator.next();
	            assertFalse(conRef.getEntity().isIsAnonymous()); 
		     }
		 }	
	}
	
	@Test
	public void testResolveValueSetDefinitionFromOWL2OnlyAnonymous() throws LBException, URISyntaxException {
		
		// pass in option ANONYMOUS_ONLY
		ResolvedValueSetCodedNodeSet resolvedVSDCNS = getValueSetDefinitionService().
				getCodedNodeSetForValueSetDefinition(new URI("OWL2LEXEVS:VerySickCancerPatient"), null, null, null, 
						AnonymousOption.ANONYMOUS_ONLY);
		
		CodedNodeSet finalCNS = null;
		
		 if (resolvedVSDCNS != null) {
			 finalCNS = resolvedVSDCNS.getCodedNodeSet(); 
			 
			 ResolvedConceptReferencesIterator iterator = finalCNS.resolve(null, null, null, null, true);
			 
		     while (iterator.hasNext()) {
	            ResolvedConceptReference conRef = iterator.next();
	            assertTrue(conRef.getEntity().isIsAnonymous()); 
		     }
		 }	
	}
	
	@Test
	public void testResolveValueSetDefinitionFromOWL2AnonymousAll() throws LBException, URISyntaxException {
		
		// pass in option AnonymousOption ALL
		ResolvedValueSetCodedNodeSet resolvedVSDCNS = getValueSetDefinitionService().
				getCodedNodeSetForValueSetDefinition(new URI("OWL2LEXEVS:VerySickCancerPatient"), null, null, null, 
						AnonymousOption.ALL);
		
		CodedNodeSet finalCNS = null;
		
		 if (resolvedVSDCNS != null) {
			 finalCNS = resolvedVSDCNS.getCodedNodeSet(); 
			 
			 ResolvedConceptReferencesIterator iterator = finalCNS.resolve(null, null, null, null, true);
			 boolean foundAnonymous = false;
			 boolean foundNonAnonymous = false;
			 
		     while (iterator.hasNext()) {
	            ResolvedConceptReference conRef = iterator.next();
	            if (conRef.getEntity().isIsAnonymous()) { 
	            	foundAnonymous = true;
	            }
	            else { 
	            	foundNonAnonymous = true;
	            }
	            assertTrue(foundAnonymous); 
	            assertTrue(foundNonAnonymous);
		     }
		 }	
	}
	
	@Test
	public void testGetValueSetEntitiesForTermFromOWL2() throws LBException, URISyntaxException {
		
		ResolvedValueSetCodedNodeSet vdcns = getValueSetDefinitionService().
				getValueSetDefinitionEntitiesForTerm("Very Sick Cancer Patient", 
						MatchAlgorithms.exactMatch.name(), new URI("OWL2LEXEVS:VerySickCancerPatient"), null, null);
		
		CodedNodeSet cns = null;
		@SuppressWarnings("unused")
		AbsoluteCodingSchemeVersionReferenceList csvrList = null;
		ResolvedConceptReferenceList rcrList = null;
		
		if (vdcns != null)
		{
			cns = vdcns.getCodedNodeSet();
			csvrList = vdcns.getCodingSchemeVersionRefList();
			
			rcrList = cns.resolveToList(null, null, null, null, false, 1024);
			
			assertTrue(rcrList.getResolvedConceptReferenceCount() == 1);
			
			for (int i = 0; i < rcrList.getResolvedConceptReferenceCount(); i++)
			{
				ResolvedConceptReference rcr =  rcrList.getResolvedConceptReference(i);
				assertTrue(rcr.getCode().equalsIgnoreCase("VerySickCancerPatient"));
			}
		}
	}
	
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testResolveValueSetDefinitionNoVersion() throws LBException, URISyntaxException {

		CodedNodeSet finalCNS = null;
		AbsoluteCodingSchemeVersionReferenceList csVersionList =  null;
		
		String valueSetDefintionRevisionId = null;
		String csVersionTag = null;
		
		ResolvedValueSetCodedNodeSet rvs_cns = getValueSetDefinitionService().
				getCodedNodeSetForValueSetDefinition(new URI("SRITEST:AUTO:PropertyRefTest1"), 
						valueSetDefintionRevisionId, csVersionList, csVersionTag);
			
		 if (rvs_cns != null) {
			 finalCNS = rvs_cns.getCodedNodeSet(); 
			 
			 ResolvedConceptReferencesIterator iterator	= null;			     
			 CodedNodeSet.SearchDesignationOption option = null;
			 String language = null;
			 String matchText = "GM";
			 String matchAlgorithm = MatchAlgorithms.exactMatch.name();
			 
			 finalCNS = finalCNS.restrictToAnonymous(CodedNodeSet.AnonymousOption.NON_ANONYMOUS_ONLY);
			 finalCNS = finalCNS.restrictToMatchingDesignations(matchText, option, matchAlgorithm, language);
             SortOptionList sortOptions = null;
             LocalNameList filterOptions = null;
             LocalNameList propertyNames = null;
             CodedNodeSet.PropertyType[] propertyTypes = null;
             boolean resolveObjects = false;
             
             iterator = finalCNS.resolve(sortOptions, filterOptions, propertyNames, propertyTypes, resolveObjects);
             while (iterator.hasNext()) {
            	 ResolvedConceptReference conRef = iterator.next();
            	 System.out.println("Coding Scheme Name: " + conRef.getCodingSchemeName());
		    	 System.out.println("Coding Scheme Reference Version: " + conRef.getCodingSchemeVersion());
		    	 System.out.println("Name Space: " + conRef.getCodeNamespace());
		    	 assertEquals("1.1", conRef.getCodingSchemeVersion());
             }
		 }	
	}
	
	@Test
	public void testGetValueSetEntitiesForTerm() throws LBException, URISyntaxException {
		
		ResolvedValueSetCodedNodeSet vdcns = getValueSetDefinitionService().getValueSetDefinitionEntitiesForTerm("General Motors", MatchAlgorithms.exactMatch.name(), new URI("SRITEST:AUTO:AllDomesticANDGM"), null, null);
		CodedNodeSet cns = null;
		@SuppressWarnings("unused")
		AbsoluteCodingSchemeVersionReferenceList csvrList = null;
		ResolvedConceptReferenceList rcrList = null;
		
		if (vdcns != null)
		{
			cns = vdcns.getCodedNodeSet();
			csvrList = vdcns.getCodingSchemeVersionRefList();
			
			rcrList = cns.resolveToList(null, null, null, null, false, 1024);
			
			assertTrue(rcrList.getResolvedConceptReferenceCount() == 1);
			
			for (int i = 0; i < rcrList.getResolvedConceptReferenceCount(); i++)
			{
				ResolvedConceptReference rcr =  rcrList.getResolvedConceptReference(i);
				assertTrue(rcr.getCode().equalsIgnoreCase("GM"));
			}
		}
		
		vdcns = getValueSetDefinitionService().getValueSetDefinitionEntitiesForTerm("General Motors", MatchAlgorithms.exactMatch.name(), new URI("SRITEST:AUTO:AllDomesticANDGM1"), null, null);
        cns = null;
        csvrList = null;
        rcrList = null;
        
        if (vdcns != null)
        {
            cns = vdcns.getCodedNodeSet();
            csvrList = vdcns.getCodingSchemeVersionRefList();
            
            rcrList = cns.resolveToList(null, null, null, null, false, 1024);
            
            assertTrue(rcrList.getResolvedConceptReferenceCount() == 1);
            
            for (int i = 0; i < rcrList.getResolvedConceptReferenceCount(); i++)
            {
                ResolvedConceptReference rcr =  rcrList.getResolvedConceptReference(i);
                assertTrue(rcr.getCode().equalsIgnoreCase("GM"));
            }
        }
        

		
		vdcns = getValueSetDefinitionService().getValueSetDefinitionEntitiesForTerm("General Motors", MatchAlgorithms.exactMatch.name(), new URI("SRITEST:AUTO:AllDomesticButGM"), null, null);
		cns = null;
		csvrList = null;
		rcrList = null;
		
		if (vdcns != null)
		{
			cns = vdcns.getCodedNodeSet();
			csvrList = vdcns.getCodingSchemeVersionRefList();
			
			rcrList = cns.resolveToList(null, null, null, null, false, 1024);
			
			if (rcrList != null)
			{
				assertTrue(rcrList.getResolvedConceptReferenceCount() == 0);
			}
		}
		
		vdcns = getValueSetDefinitionService().getValueSetDefinitionEntitiesForTerm("hypha", MatchAlgorithms.LuceneQuery.name(), new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		
		if (vdcns != null)
		{
			cns = vdcns.getCodedNodeSet();
			csvrList = vdcns.getCodingSchemeVersionRefList();
			
			rcrList = cns.resolveToList(null, null, null, null, false, 1024);
			
			assertTrue(rcrList.getResolvedConceptReferenceCount() == 4);
			
			for (int i = 0; i < rcrList.getResolvedConceptReferenceCount(); i++)
			{
				ResolvedConceptReference rcr =  rcrList.getResolvedConceptReference(i);
				assertTrue(rcr.getCode().equalsIgnoreCase("FAO:0001014")
						|| rcr.getCode().equalsIgnoreCase("FAO:0001013")
						|| rcr.getCode().equalsIgnoreCase("FAO:0001004")
						|| rcr.getCode().equalsIgnoreCase("FAO:0001003"));				
			}
		}		
	}
	
	
	

	@Test
	public void testIsEntityInVSResolution() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReference csvr = getValueSetDefinitionService().isEntityInValueSet("Chevy", new URI("SRITEST:AUTO:EveryThing"), null, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("Jaguar", new URI("SRITEST:AUTO:GM"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("GM", new URI("SRITEST:AUTO:AllDomesticButGM"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("Ford", new URI("SRITEST:AUTO:AllDomesticButGM"), null, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("Ford", new URI("SRITEST:AUTO:AllDomesticANDGM1"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("Chevy", new URI("SRITEST:AUTO:AllDomesticANDGM1"),null,  null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001013", new URI("SRITEST:FA:MicrobialStructureOntologyLeafOnly"), null, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001004", new URI("SRITEST:FA:MicrobialStructureOntologyLeafOnly"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0000001", new URI("SRITEST:FA:MicrobialStructureOntologyLeafOnly"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001003", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0000001", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001004", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001013", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001001", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		assertTrue(csvr == null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("FAO:0001010", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		assertTrue(csvr == null);
	}

	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testIsEntityInValueSetResolution() throws LBException, URISyntaxException {
	    AbsoluteCodingSchemeVersionReferenceList incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"));	    
		AbsoluteCodingSchemeVersionReference csvr = getValueSetDefinitionService().isEntityInValueSet("Focus", new URI("Automobiles"), new URI("SRITEST:AUTO:AllDomesticButGM"), null, incsvrl, null);
		assertTrue(csvr == null);
		
		incsvrl.setAbsoluteCodingSchemeVersionReference(0, Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.1"));
        csvr = getValueSetDefinitionService().isEntityInValueSet("Focus", new URI("Automobiles"), new URI("SRITEST:AUTO:AllDomesticButGM"), null, incsvrl, null);
		assertTrue(csvr != null);
		
		csvr = getValueSetDefinitionService().isEntityInValueSet("Focus", new URI("Automobiles"), new URI("SRITEST:AUTO:AllDomesticButGM"), null, null, "devel");
        assertTrue(csvr == null);
        
        csvr = getValueSetDefinitionService().isEntityInValueSet("Focus", new URI("Automobiles"), new URI("SRITEST:AUTO:AllDomesticButGM"), null, null, LBConstants.KnownTags.PRODUCTION.toString());
        assertTrue(csvr != null);
	}

	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testListValueSetsWithEntityCode() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"));	    
		
	    List<String> uris = getValueSetDefinitionService().listValueSetsWithEntityCode("Focus", new URI("Automobiles"), incsvrl, null);
	    
	    assertTrue(uris.size() == 0);
	    
	    uris = getValueSetDefinitionService().listValueSetsWithEntityCode("Ford", new URI("Automobiles"), incsvrl, null);
	    
	    assertTrue(uris.size() == 5);
	    for (String uri : uris)
	    {
	    	assertTrue(uri.equals("SRITEST:AUTO:AllDomesticButGM") ||
	    			uri.equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName") ||
	    			uri.equals("SRITEST:AUTO:DomesticAutoMakers") ||
	    			uri.equals("SRITEST:AUTO:EveryThing") ||
	    			uri.equals("SRITEST:AUTO:Ford"));
	    }
	    
	    incsvrl.setAbsoluteCodingSchemeVersionReference(0, Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.1"));
	    uris = getValueSetDefinitionService().listValueSetsWithEntityCode("Focus", new URI("Automobiles"), incsvrl, null);
	    
	    assertTrue(uris.size() == 7);
	    for (String uri : uris)
	    {
	    	assertTrue(uri.equals("SRITEST:AUTO:AllDomesticButGM") ||
	    			uri.equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName") ||
	    			uri.equals("SRITEST:AUTO:DomasticLeafOnly") ||
	    			uri.equals("SRITEST:AUTO:DomesticAutoMakers") ||
	    			uri.equals("SRITEST:AUTO:EveryThing") ||
	    			uri.equals("SRITEST:AUTO:Ford") ||
	    			uri.equals("SRITEST:AUTO:PropRefGeneralOrFocus"));
	    }
	    
	    uris = getValueSetDefinitionService().listValueSetsWithEntityCode("Ford", new URI("Automobiles"), incsvrl, null);
	    
	    assertTrue(uris.size() == 5);
	    for (String uri : uris)
	    {
	    	assertTrue(uri.equals("SRITEST:AUTO:AllDomesticButGM") ||
	    			uri.equals("SRITEST:AUTO:AllDomesticButGMWithlt250charName") ||
	    			uri.equals("SRITEST:AUTO:DomesticAutoMakers") ||
	    			uri.equals("SRITEST:AUTO:EveryThing") ||
	    			uri.equals("SRITEST:AUTO:Ford"));
	    }
	}
	
	@Test
	public void testListValueSetsWithEntityCodeForOWL2() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("http://ncicb.nci.nih.gov/xml/owl/EVS/owl2lexevs.owl", "0.1.1"));	    
		
	    List<String> uris = getValueSetDefinitionService().
	    		listValueSetsWithEntityCode("Person", new URI("OWL2LEXEVS:VerySickCancerPatient"), incsvrl, null);
	    assertTrue(uris.size() == 0);
	}
	
	@Test
	public void testIsSubSet() throws LBException, URISyntaxException {

		assertFalse(getValueSetDefinitionService().isSubSet(new URI("SRITEST:AUTO:DomesticAutoMakers"), new URI("SRITEST:AUTO:GM"), null, null));
		
		assertTrue(getValueSetDefinitionService().isSubSet(new URI("SRITEST:AUTO:GM"), new URI("SRITEST:AUTO:DomesticAutoMakers"), null, null));
		
		assertFalse(getValueSetDefinitionService().isSubSet(new URI("SRITEST:AUTO:Ford"), new URI("SRITEST:AUTO:AllDomesticANDGM"), null, null));
	}

	@Test
	public void testListValueSetDefinitions() throws LBException {
		List<String> uris = getValueSetDefinitionService().listValueSetDefinitions("Domestic Auto Makers");
		String uri = uris.get(0).toString();
		
		assertTrue(uri.equals("SRITEST:AUTO:DomesticAutoMakers"));
		
		uris = getValueSetDefinitionService().listValueSetDefinitions(null);
		assertTrue(uris.size() > 0);
	}
	
	@Test
	@Category(RemoveFromDistributedTests.class)
	public void testCompilerDecoratorCacheSize(){
		assertEquals(FileSystemCachingValueSetDefinitionCompilerDecorator.MAX_IN_CACHE, 1000);
	}
	
	@Test
	public void testGetValueSetURIsForSupportedTagAndValue(){
		List<String> uris = getValueSetDefinitionService().getValueSetDefinitionURIsForSupportedTagAndValue("ConceptDomain", "Autos", null);
		
		assertTrue("vsdURIs.size() : " + uris.size(), uris.size() >= 3);
	
		for (String uri : uris)
		{
			assertTrue(uri.equalsIgnoreCase("SRITEST:AUTO:Automobiles") || 
					uri.equalsIgnoreCase("SRITEST:AUTO:DomesticAutoMakers") || 
					uri.equalsIgnoreCase("SRITEST:AUTO:EveryThing")|| 
			uri.equalsIgnoreCase("XTEST:One.Node.ValueSet"));
		}
		
		uris = getValueSetDefinitionService().getValueSetDefinitionURIsForSupportedTagAndValue("ConceptDomain", "Autos1231231", null);
		assertTrue(uris.size() == 0);
	}
	
	
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testResolveValueSetDef() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.1"));
		ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:AUTO:Ford"), null, csvList, null, null);
		
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getValueSetDefinitionURI().equals(new URI("SRITEST:AUTO:Ford")));
		assertTrue(rvdDef.getValueSetDefinitionName().equals("Ford"));
		
		Set<String> codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		
		assertTrue(codes.size() == 5);
		assertTrue(codes.contains("F150"));
		assertTrue(codes.contains("Windsor"));
		assertTrue(codes.contains("Jaguar"));
		assertTrue(codes.contains("Focus"));
		assertTrue(codes.contains("Ford"));
		
		codes.clear();
		
		csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.0"));
		rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:AUTO:Ford"), null, csvList, null, null);
		
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getCodingSchemeVersionRefList().getAbsoluteCodingSchemeVersionReference()[0].getCodingSchemeURN().equals("urn:oid:11.11.0.1"));
		assertTrue(rvdDef.getCodingSchemeVersionRefList().getAbsoluteCodingSchemeVersionReference()[0].getCodingSchemeVersion().equals("1.0"));
		assertTrue(rvdDef.getValueSetDefinitionURI().equals(new URI("SRITEST:AUTO:Ford")));
		assertTrue(rvdDef.getValueSetDefinitionName().equals("Ford"));
		
		codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		assertTrue(codes.size() == 2);
		assertTrue(codes.contains("Jaguar"));
		assertTrue(codes.contains("Ford"));
		
		//***************------------------*****************************
		rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:FA:hyphaLeafOnly"), null, null, null, null);
        
        codes = new HashSet<String>();
        while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
        {
            ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
            codes.add(rcr.getCode());
        }
        assertTrue("Size: " + codes.size(), codes.size() == 3);
        assertTrue(codes.contains("FAO:0001018"));
        assertTrue(codes.contains("FAO:0001014"));
        assertTrue(codes.contains("FAO:0001013"));
        codes.clear();
        
		rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:AUTO:DomasticLeafOnly"), null, null, LBConstants.KnownTags.PRODUCTION.name(), null);
		
		codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		assertTrue(codes.size() == 6);
		assertFalse(codes.contains("73"));        // We never pull retired codes (!)
		assertTrue(codes.contains("Chevy"));
		assertTrue(codes.contains("F150"));
		assertTrue(codes.contains("Focus"));
		assertTrue(codes.contains("Jaguar"));
		assertTrue(codes.contains("Windsor"));
		assertTrue(codes.contains("GMC"));
		codes.clear();
		
		// no versionTag specified.  This will attempt to retrieve the PRODUCTION version.
        rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:AUTO:AllDomesticANDGM1"), null, null, null, null);
        
        codes = new HashSet<String>();
        while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
        {
            ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
            codes.add(rcr.getCode());
        }
        assertTrue("Size: " + codes.size(), codes.size() == 3);
        assertTrue(codes.contains("GM"));
        assertTrue(codes.contains("Chevy"));
        assertTrue(codes.contains("GMC"));
        codes.clear();
	}
	
	/**
	 * GForge #24037 : 1. Include only specified entityCode if the transitiveClosure is 'false' and no association is stated
	 * in referenceAssociation.
	 * @throws LBException
	 * @throws URISyntaxException
	 */
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testResolveNodeOnlyValueSetDef() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.1"));
		ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:AUTO:GM_NODE_ONLY"), null, csvList, null, null);
		
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getValueSetDefinitionURI().equals(new URI("SRITEST:AUTO:GM_NODE_ONLY")));
		assertTrue(rvdDef.getValueSetDefinitionName().equals("GM Node Only"));
		
		Set<String> codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		
		// should return only GM concept
		assertTrue(codes.size() == 1);
		assertTrue(codes.contains("GM"));
		
		codes.clear();
	}
	
	/**
	 * GForge #24037 : 2. Include specified entityCode and it's immediate parent/children (depending on stated association) if transitiveClosure
	 * is 'false' and an association is stated in referenceAssociation.
	 * @throws LBException
	 * @throws URISyntaxException
	 */
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testResolveNodeAndImmediateChildrenOnlyValueSetDef() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.1"));
		ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(new URI("SRITEST:AUTO:GM_AND_IMMI_NODE"), null, csvList, null, null);
		
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getValueSetDefinitionURI().equals(new URI("SRITEST:AUTO:GM_AND_IMMI_NODE")));
		assertTrue(rvdDef.getValueSetDefinitionName().equals("GM and immediate nodes Only"));
		
		Set<String> codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		
		// should return only GM and immediate children concepts
		assertTrue(codes.size() == 3);
		assertTrue(codes.contains("GM"));
		assertTrue(codes.contains("GMC"));
		assertTrue(codes.contains("Chevy"));
		codes.clear();
	}

	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testIsDomain() throws LBException {
		assertTrue(getValueSetDefinitionService().isValueSetDefinition("VD005", "Automobiles", Constructors.createCodingSchemeVersionOrTag(null, "1.1")));
	}
	
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testPropertyReference() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
		AbsoluteCodingSchemeVersionReference autoVersion_10 = Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.0");
		AbsoluteCodingSchemeVersionReference autoVersion_11 = Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.1");
		
		// version 1.0 does not contain Focus, so only General Motors should be returned
		acsvrList.addAbsoluteCodingSchemeVersionReference(autoVersion_10);
		ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().
				resolveValueSetDefinition(new URI("SRITEST:AUTO:PropRefGeneralOrFocus"), null, acsvrList, null, null);
		
		assertTrue(rvdDef != null);
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getValueSetDefinitionURI().equals(new URI("SRITEST:AUTO:PropRefGeneralOrFocus")));
		assertTrue(rvdDef.getValueSetDefinitionName().equals("Property Ref test General OR Focus"));
		
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			assertTrue(rcr.getCode().equals("GM"));
		}
		
		acsvrList = new AbsoluteCodingSchemeVersionReferenceList();
		// version 1.1 does contain both General and Focus terms, so both General Motors and Focus should be returned
		acsvrList.addAbsoluteCodingSchemeVersionReference(autoVersion_11);
		rvdDef = getValueSetDefinitionService().
				resolveValueSetDefinition(new URI("SRITEST:AUTO:PropRefGeneralOrFocus"), null, acsvrList, null, null);
		
		assertTrue(rvdDef != null);
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getValueSetDefinitionURI().equals(new URI("SRITEST:AUTO:PropRefGeneralOrFocus")));
		assertTrue(rvdDef.getValueSetDefinitionName().equals("Property Ref test General OR Focus"));
		
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			assertTrue(rcr.getCode().equals("GM") || rcr.getCode().equals("Focus"));
		}		
	}
	
//	public void testExportValueSetDefinition(){
//		try {
//			getValueSetDefinitionService().exportValueSetDefinition(new URI("SRITEST:AUTO:PropRefGeneralOrFocus"), "c:\\testExportVSD.xml", true, true);
//		} catch (LBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	@Test
	public void testGetVSDURIForConceptDomain(){
		List<String> vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomain("Autos", null);
		assertTrue("vsdURIs.size() : " + vsdURIs.size(), vsdURIs.size() >= 3);
		
		for (String uri : vsdURIs)
		{
			assertTrue(uri.equalsIgnoreCase("SRITEST:AUTO:Automobiles") 
					|| uri.equalsIgnoreCase("SRITEST:AUTO:DomesticAutoMakers")
					|| uri.equalsIgnoreCase("SRITEST:AUTO:EveryThing")|| 
					uri.equalsIgnoreCase("XTEST:One.Node.ValueSet"));			
		}
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomain("something", null);
		assertTrue(vsdURIs.size() == 0);
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomain(null, null);
		assertTrue(vsdURIs.size() == 0);
	}
	
	public void testGetVSDURIForUsageContext(){
		List<String> usageContexts = new ArrayList<String>();
		usageContexts.add("GM");
		usageContexts.add("Automobiles");
		usageContexts.add("Ford");
		List<String> vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithUsageContext(usageContexts, null);
		assertTrue(vsdURIs.size() == 4);
		
		for (String uri : vsdURIs)
		{
			assertTrue(uri.equalsIgnoreCase("SRITEST:AUTO:DomesticAutoMakers") 
					|| uri.equalsIgnoreCase("SRITEST:AUTO:EveryThing")
					|| uri.equalsIgnoreCase("SRITEST:AUTO:PropertyRefTest1")|| 
					uri.equalsIgnoreCase("XTEST:One.Node.ValueSet"));			
		}
		
		usageContexts = new ArrayList<String>();
		usageContexts.add("Ford");
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithUsageContext(usageContexts, null);
		assertTrue(vsdURIs.size() == 3);
		
		for (String uri : vsdURIs)
		{
			assertTrue(uri.equalsIgnoreCase("SRITEST:AUTO:EveryThing")
					|| uri.equalsIgnoreCase("SRITEST:AUTO:PropertyRefTest1")|| 
					uri.equalsIgnoreCase("XTEST:One.Node.ValueSet"));			
		}
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithUsageContext(null, null);
		assertTrue(vsdURIs.size() == 0);
	}
	
	@Test
	public void testGetVSDURIForConceptDomainAndUsageContext(){
		List<String> usageContexts = new ArrayList<String>();
		usageContexts.add("GM");
		usageContexts.add("Automobiles");
		usageContexts.add("Ford");
		List<String> vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomainAndUsageContext("Autos", usageContexts, null);
		
		assertTrue(vsdURIs.size() == 3);
		
		for (String uri : vsdURIs)
		{
			assertTrue(uri.equalsIgnoreCase("SRITEST:AUTO:DomesticAutoMakers") 
					|| uri.equalsIgnoreCase("SRITEST:AUTO:EveryThing")|| 
					uri.equalsIgnoreCase("XTEST:One.Node.ValueSet"));;			
		}
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomainAndUsageContext("Autos", null, null);
		assertTrue(vsdURIs.size() == 0);
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomainAndUsageContext(null, usageContexts, null);
		assertTrue(vsdURIs.size() == 0);
		
		usageContexts = new ArrayList<String>();
		usageContexts.add("Ford");
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomainAndUsageContext("Autos", usageContexts, null);
		assertTrue(vsdURIs.size() == 2);
		
		for (String uri : vsdURIs)
		{
			assertTrue(uri.equalsIgnoreCase("SRITEST:AUTO:EveryThing") || uri.equalsIgnoreCase("XTEST:One.Node.ValueSet"));			
		}
		
		vsdURIs = getValueSetDefinitionService().getValueSetDefinitionURIsWithConceptDomainAndUsageContext("Autos-Nomatch", usageContexts, null);
		assertTrue(vsdURIs.size() == 0);
	}
	
	/**
	 * This test case is to test the functionality of resolving VSD references. 
	 * We will create a VSD SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD that contains 2 definition Entries:
	 * 		1. VSDReference - SRITEST:AUTO:GM_AND_IMMI_NODE
	 * 		2. EntityReference - Ford concept only
	 * Using this VSD, we will test two scenarios:
	 * 1. We will resolve the VSD 'SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD' which references external VSD SRITEST:AUTO:GM_AND_IMMI_NODE. 
	 * 		This will resolve the SRITEST:AUTO:GM_AND_IMMI_NODE that is available in the service.
	 * 		This should return 3 concepts from SRITEST:AUTO:GM_AND_IMMI_NODE VSD and a FORD concept using Automobiles 1.1
	 * 2. We will create referenced VSD SRITEST:AUTO:GM_AND_IMMI_NODE to return only GM concept 
	 * 	  and pass this VSD as a parameter to resolve SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD.
	 * 		This resolve method should use the passed VSD, not the one that is available in the service.
	 * 		This should return 1 concept from SRITEST:AUTO:GM_AND_IMMI_NODE and a FORD concept using Automobiles 1.1	  
	 */
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testResolveVSDWithRefVSDObjects() throws LBException, URISyntaxException{
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setValueSetDefinitionName("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setDefaultCodingScheme("Automobiles");
		vsd.setConceptDomain("Autos");
		
		DefinitionEntry de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		
		vsd.addDefinitionEntry(de);
		
		ValueSetDefinitionReference vsdRef = new ValueSetDefinitionReference();
		vsdRef.setValueSetDefinitionURI("SRITEST:AUTO:GM_AND_IMMI_NODE");
		de.setValueSetDefinitionReference(vsdRef);
		
		de = new DefinitionEntry();
		de.setRuleOrder(2L);
		de.setOperator(DefinitionOperator.OR);
		
		EntityReference entityRef = new EntityReference();
		entityRef.setEntityCode("Ford");
		entityRef.setEntityCodeNamespace("Automobiles");
		entityRef.setLeafOnly(false);
		entityRef.setTransitiveClosure(false);
		de.setEntityReference(entityRef);
		
		vsd.addDefinitionEntry(de);
		
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "1.1"));
		ResolvedValueSetDefinition rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(vsd, csvList, null, null);
		
		Set<String> codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		
		// should return only GM, its immediate children concepts and Ford
		assertTrue(codes.size() == 4);
		assertTrue(codes.contains("GM"));
		assertTrue(codes.contains("GMC"));
		assertTrue(codes.contains("Chevy"));
		assertTrue(codes.contains("Ford"));
		
		codes.clear();
		
		// Now lets create VSD SRITEST:AUTO:GM_AND_IMMI_NODE that will return only GM concept
		ValueSetDefinition vsdR = new ValueSetDefinition();
		vsdR.setValueSetDefinitionURI("SRITEST:AUTO:GM_AND_IMMI_NODE");
		vsdR.setValueSetDefinitionName("SRITEST:AUTO:GM_AND_IMMI_NODE");
		vsdR.setDefaultCodingScheme("Automobiles");
		vsdR.setConceptDomain("Autos");
		
		de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		
		entityRef = new EntityReference();
		entityRef.setEntityCode("GM");
		entityRef.setEntityCodeNamespace("Automobiles");
		entityRef.setLeafOnly(false);
		entityRef.setTransitiveClosure(false);
		
		de.setEntityReference(entityRef);
		
		vsdR.addDefinitionEntry(de);
		
		// now lets call the resolve method by passing created VSD reference
		HashMap<String, ValueSetDefinition> vsdRefList = new HashMap<String, ValueSetDefinition>();
		vsdRefList.put("SRITEST:AUTO:GM_AND_IMMI_NODE", vsdR);
		rvdDef = getValueSetDefinitionService().resolveValueSetDefinition(vsd, csvList, null, vsdRefList, null);
		
		codes = new HashSet<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		
		// should return only GM and Ford concepts
		assertTrue(codes.size() == 2);
		assertTrue(codes.contains("GM"));
		assertTrue(codes.contains("Ford"));
		
		codes.clear();
	}

	/**
	 * This test case calls the export method by passing the VSD URI that is available in the 
	 * service to export to StringBuffer in LexGrid XML format.
	 * Then, unmarshal the received StringBuffer to VSD object and verify the values. 
	 * 
	 * @throws LBException
	 * @throws URISyntaxException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	@Test
	public void testExportValueSetDefinitionByURI() throws LBException, URISyntaxException, MarshalException, ValidationException{
		StringBuffer sb = getValueSetDefinitionService().exportValueSetDefinition(new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		
		Unmarshaller um = new Unmarshaller();
		um.setValidation(false);
		um.setClass(ValueSetDefinition.class);
		
		ValueSetDefinition vdDef = (ValueSetDefinition) um.unmarshal(new StringReader(sb.toString()));
		
		assertTrue(vdDef.getDefaultCodingScheme().equals("fungal_anatomy"));
		assertTrue(vdDef.getDefinitionEntry().length == 2);
		
		assertTrue(vdDef.getStatus().equals("ACTIVE"));
		assertTrue(vdDef.getIsActive());
		
		// value domain definition source
		List<Source> srcs = vdDef.getSourceAsReference();
		assertTrue(srcs.size() == 2);
		for(Source src : srcs)
		{
			String val = src.getContent();
			if (val.equals("OBO"))
			{
				assertTrue(src.getRole().equals("role1"));
				assertTrue(src.getSubRef().equals("subRef1"));
			}
			else if (val.equals("Fungal"))
			{
				assertTrue(src.getRole().equals("role2"));
				assertTrue(src.getSubRef().equals("subRef2"));
			}
		}
		srcs.clear();
		
		// value domain definition context		
		assertTrue(vdDef.getRepresentsRealmOrContextAsReference().size() == 3);		
		Iterator<String> cItr = vdDef.getRepresentsRealmOrContextAsReference().iterator();		
		while(cItr.hasNext())
		{
			String context = cItr.next();
			assertTrue(context.equals("OBO") || context.equals("Fungal") || context.equals("Anatomy"));
		}
		
		Mappings maps = vdDef.getMappings();
		List<SupportedAssociation> supAssns = maps.getSupportedAssociationAsReference();
		
        assertTrue(supAssns.size() == 3);
        int matchCount = supAssns.size();
        for (SupportedAssociation supAssn : supAssns)
        {
            // Note: there should be two matches here - one for the local and one for the global
            // TODO Maybe we should eliminate duplicates?
            if(supAssn.getUri().equals("urn:lsid:bioontology.org:fungal_anatomy:is_a")) {
                matchCount--;
                assertTrue(supAssn.getLocalId().equals("is_a"));
            } else if(supAssn.getUri().equals("urn:oid:1.3.6.1.4.1.2114.108.1.8.1")) {
                matchCount--;
                assertTrue(supAssn.getLocalId().equals("hasSubtype"));
            } else if(supAssn.getUri().equals("urn:oid:11.11.0.1")) {
                matchCount--;
                assertTrue(supAssn.getLocalId().equals("uses"));
            }
        }
        assertTrue(matchCount == 0);
        supAssns.clear();
        
        List<SupportedDataType> supDTs = maps.getSupportedDataTypeAsReference();
        assertTrue(supDTs.size() == 3);
        matchCount = supDTs.size();
        for (SupportedDataType supDT : supDTs)
        {
            if(supDT.getLocalId().equals("testhtml")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getContent().equals("test/html"));
            } else if(supDT.getLocalId().equals("textplain")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getContent().equals("text/plain"));
            } if(supDT.getLocalId().equals("text")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getContent().equals("text"));
            }
        }
        assertTrue(matchCount == 0);
        supDTs.clear();
		
		List<SupportedPropertyQualifier> supPQs = maps.getSupportedPropertyQualifierAsReference();
		
		for(SupportedPropertyQualifier supPQ : supPQs)
		{
			assertTrue(supPQ.getLocalId().equals("PropQual A Namuuuuu") || supPQ.getLocalId().equals("PropQual B Namuuuuu"));
		}
		supPQs.clear();
		
        List<SupportedSource> supSrcs = maps.getSupportedSourceAsReference();
        assertTrue(supSrcs.size() == 4);
        for(SupportedSource supSrc : supSrcs)
        {
            assertTrue(supSrc.getLocalId().equals("OBO") ||
                       supSrc.getLocalId().equals("Fungal") || 
                       supSrc.getLocalId().equals("lexgrid.org") || 
                       supSrc.getLocalId().equals("_111101") );
        }
        supSrcs.clear();
		
		Property prop = (Property) vdDef.getProperties().getPropertyAsReference().get(0);
		
		assertTrue(prop.getValue().getContent().equals("microbial structure ontology AND HyphaInMycelium"));
		
		// property source
		assertTrue(prop.getSourceAsReference().size() == 2);
		Iterator<Source> srcItr = prop.getSourceAsReference().iterator();
		while(srcItr.hasNext()){
			Source source = srcItr.next();
			
			String srcValue = source.getContent();
			String role = source.getRole();
			String subRef = source.getSubRef();
			
			if (srcValue.equals("OBO"))
			{
				assertTrue(role.equals("PropRole1"));
				assertTrue(subRef.equals("PropSubRef1"));
			}
			else 
			{
				assertTrue(role.equals("PropRole2"));
				assertTrue(subRef.equals("PropSubRef2"));
			}
		}
		
		// property usage context
		assertTrue(prop.getUsageContextAsReference().size() == 2);
		cItr = prop.getUsageContextAsReference().iterator();
		while(cItr.hasNext())
		{
			String context = cItr.next();
			assertTrue(context.equals("PropUsageContext OBO") || context.equals("PropUsageContext Fungal"));
		}
		
		
		// property qualifier
		assertTrue(prop.getPropertyQualifierAsReference().size() == 2);
		Iterator<PropertyQualifier> pItr = prop.getPropertyQualifierAsReference().iterator();
		while(pItr.hasNext())
		{
			PropertyQualifier pQual = pItr.next();
			String value = pQual.getValue().getContent();
//			String dataType = pQual.getValue().getDataType();
			String type = pQual.getPropertyQualifierType();
			String name = pQual.getPropertyQualifierName();
			
			assertTrue(value.equals("PropQualValue MicrobialStructureOntology") || value.equals("PropQualValue HyphaInMycelium"));
			assertTrue(name.equals("PropQual A Namuuuuu") || name.equals("PropQual B Namuuuuu"));
			assertTrue(type.equals("pQual Type A") || type.equals("pQual Type B"));
		}
		
	}
	
	/**
	 * This test case call the export method that takes in the VSD object and exports it to StringBuffer in LexGrid XML format.
	 * So, for this, we will create a VSD and pass it to export.
	 * Will get the exported StringBuffer, unmarshal it to VSD object and verify the values.
	 * @throws LBException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	@Test
	public void testExportValueSetDefinitionByObject() throws LBException, MarshalException, ValidationException{
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setValueSetDefinitionName("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setDefaultCodingScheme("Automobiles");
		vsd.setConceptDomain("Autos");
		
		DefinitionEntry de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		
		vsd.addDefinitionEntry(de);
		
		ValueSetDefinitionReference vsdRef = new ValueSetDefinitionReference();
		vsdRef.setValueSetDefinitionURI("SRITEST:AUTO:GM_AND_IMMI_NODE");
		de.setValueSetDefinitionReference(vsdRef);
		
		de = new DefinitionEntry();
		de.setRuleOrder(2L);
		de.setOperator(DefinitionOperator.OR);
		
		EntityReference entityRef = new EntityReference();
		entityRef.setEntityCode("Ford");
		entityRef.setEntityCodeNamespace("Automobiles");
		entityRef.setLeafOnly(false);
		entityRef.setTransitiveClosure(false);
		de.setEntityReference(entityRef);
		
		vsd.addDefinitionEntry(de);
		
		StringBuffer sb = getValueSetDefinitionService().exportValueSetDefinition(vsd);
		
		Unmarshaller um = new Unmarshaller();
		um.setValidation(false);
		um.setClass(ValueSetDefinition.class);
		
		ValueSetDefinition vdDef = (ValueSetDefinition) um.unmarshal(new StringReader(sb.toString()));
		
		assertTrue(vdDef.getValueSetDefinitionURI().equals("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD"));
		assertTrue(vdDef.getValueSetDefinitionName().equals("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD"));
		assertTrue(vdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(vdDef.getConceptDomain().equals("Autos"));
		assertTrue(vdDef.getDefinitionEntry().length == 2);
		
		for(DefinitionEntry defEntry : vdDef.getDefinitionEntryAsReference()){
			vsdRef = defEntry.getValueSetDefinitionReference();
			if (vsdRef != null)
			{
				assertTrue(vsdRef.getValueSetDefinitionURI().equals("SRITEST:AUTO:GM_AND_IMMI_NODE"));
			} 
			else
			{
				entityRef = defEntry.getEntityReference();
				assertTrue(entityRef.getEntityCode().equals("Ford"));
				assertTrue(entityRef.getEntityCodeNamespace().equals("Automobiles"));
				assertFalse(entityRef.isLeafOnly());
				assertFalse(entityRef.isTransitiveClosure());
			}
		}
	}
	
	
	/**
	 * 
	 * @throws LBException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	@Test
	 @Category(RemoveFromDistributedTests.class)
	public void testExportVSResolutionByVSDObject() throws LBException, URISyntaxException, IOException{
		
		File tempExportFile = new File(System.getProperty("java.io.tmpdir"));		

		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setValueSetDefinitionName("VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setDefaultCodingScheme("Automobiles");
		vsd.setConceptDomain("Autos");
		
		DefinitionEntry de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		
		vsd.addDefinitionEntry(de);
		
		ValueSetDefinitionReference vsdRef = new ValueSetDefinitionReference();
		vsdRef.setValueSetDefinitionURI("SRITEST:AUTO:GM_AND_IMMI_NODE");
		de.setValueSetDefinitionReference(vsdRef);
		
		de = new DefinitionEntry();
		de.setRuleOrder(2L);
		de.setOperator(DefinitionOperator.OR);
		
		EntityReference entityRef = new EntityReference();
		entityRef.setEntityCode("Ford");
		entityRef.setEntityCodeNamespace("Automobiles");
		entityRef.setLeafOnly(false);
		entityRef.setTransitiveClosure(false);
		de.setEntityReference(entityRef);
		
		vsd.addDefinitionEntry(de);
		
		// Start the value set resolution export
		getValueSetDefinitionService().exportValueSetResolution(vsd, null, tempExportFile.toURI(), null, null, true, false);
		
		File tempInStreamFile = new File(System.getProperty("java.io.tmpdir") + File.separator + 
		"VSDREF_GM_IMMI_NODE_AND_FORD" + "_" + "UNASSIGNED" + ".xml");
		
		
		// create an input stream for the exported content
		InputStream is = new FileInputStream(tempInStreamFile);
		
		assertTrue(is != null);
		
		// start reading the contents, that is stream the file
		if (is != null) {
			try {
				DataInputStream in = new DataInputStream(is);
		        BufferedReader br = new BufferedReader(new InputStreamReader(in));
			    String strLine;
			    //Read File Line By Line
			    while ((strLine = br.readLine()) != null)   {
			      // Print the content on the console
			      System.out.println (strLine);
			    }
			}finally {
					is.close();
					tempInStreamFile.delete();
			}
		} 
	}

	@Test
	public void testExportVSResolutionByVSDObjectToWriter() throws LBException, URISyntaxException, IOException{
		
		ValueSetDefinition vsd = new ValueSetDefinition();
		vsd.setValueSetDefinitionURI("SRITEST:AUTO:VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setValueSetDefinitionName("VSDREF_GM_IMMI_NODE_AND_FORD");
		vsd.setDefaultCodingScheme("Automobiles");
		vsd.setConceptDomain("Autos");
		
		DefinitionEntry de = new DefinitionEntry();
		de.setRuleOrder(1L);
		de.setOperator(DefinitionOperator.OR);
		
		vsd.addDefinitionEntry(de);
		
		ValueSetDefinitionReference vsdRef = new ValueSetDefinitionReference();
		vsdRef.setValueSetDefinitionURI("SRITEST:AUTO:GM_AND_IMMI_NODE");
		de.setValueSetDefinitionReference(vsdRef);
		
		de = new DefinitionEntry();
		de.setRuleOrder(2L);
		de.setOperator(DefinitionOperator.OR);
		
		EntityReference entityRef = new EntityReference();
		entityRef.setEntityCode("Ford");
		entityRef.setEntityCodeNamespace("Automobiles");
		entityRef.setLeafOnly(false);
		entityRef.setTransitiveClosure(false);
		de.setEntityReference(entityRef);
		
		vsd.addDefinitionEntry(de);
		
		// Start the value set resolution export
		InputStream reader =  getValueSetDefinitionService().exportValueSetResolution(vsd, null, null, null, false);
		
		if (reader != null) {
			StringBuffer buf = new StringBuffer(); 
	        try { 
	            for(int c = reader.read(); c != -1; c = reader.read()) { 
	                buf.append((char)c); 
	            } 
	            System.out.println(buf.toString()); 
	        } catch(IOException e) { 
	            throw e; 
	        } finally { 
	            try { 
	                reader.close(); 
	            } catch(Exception e) { 
	                // ignored 
	            } 
	        } 
		}
	}
	
	@Test
	public void testExportVSDURIResolution() throws LBException, URISyntaxException, IOException{
		// Start the value set resolution export
		InputStream reader =  getValueSetDefinitionService().exportValueSetResolution(new URI("SRITEST:AUTO:EveryThing"), null, null, null, false);
		
		if (reader != null) {
			StringBuffer buf = new StringBuffer(); 
	        try { 
	            for(int c = reader.read(); c != -1; c = reader.read()) { 
	                buf.append((char)c); 
	            } 
	            System.out.println(buf.toString()); 
	        } catch(IOException e) { 
	            throw e; 
	        } finally { 
	            try { 
	                reader.close(); 
	            } catch(Exception e) { 
	                // ignored 
	            } 
	        } 
		}
	}
	
	private LexEVSValueSetDefinitionServices getValueSetDefinitionService(){
		if (vds_ == null) {
			vds_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		}
		return vds_;
	}
	
	public LexEVSValueSetDefinitionServices setValueSetDefinitionService(LexEVSValueSetDefinitionServices svcs){
		if (vds_ == null) {
			vds_ = svcs;
		}
		return vds_;
	}
	
	
	protected void dumpValueSetDefinitionResolution(URI vdURI, AbsoluteCodingSchemeVersionReferenceList csvs) {
	    try {     
            ResolvedValueSetDefinition resDef = getValueSetDefinitionService().resolveValueSetDefinition(vdURI, null, csvs, null, null);
            Iterator<? extends AbsoluteCodingSchemeVersionReference> csList = resDef.getCodingSchemeVersionRefList().iterateAbsoluteCodingSchemeVersionReference();
            System.out.println("Value domain: " + vdURI.toString() + " used: ");
            while(csList.hasNext()) {
                AbsoluteCodingSchemeVersionReference csr = csList.next();
                System.out.println(csr.getCodingSchemeURN() + " : " + csr.getCodingSchemeVersion());
            }
            System.out.println("\n===== Concepts =====");
            ResolvedConceptReferencesIterator crIter = resDef.getResolvedConceptReferenceIterator();
            int cnt = 0;
            while(crIter.hasNext()) {
                ResolvedConceptReference rcr = crIter.next();
                System.out.println("(" + cnt++ + ")" + rcr.getCode() + ":" + rcr.getCodingSchemeURI() + "(" + rcr.getEntityDescription().getContent() + ")");
            }
            
	    } catch (LBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return;
        }  
	}
}