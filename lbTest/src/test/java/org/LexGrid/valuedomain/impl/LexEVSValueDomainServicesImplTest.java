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
package org.LexGrid.valuedomain.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.LexGrid.emf.commonTypes.Property;
import org.LexGrid.emf.commonTypes.PropertyQualifier;
import org.LexGrid.emf.commonTypes.Source;
import org.LexGrid.emf.naming.Mappings;
import org.LexGrid.emf.naming.SupportedAssociation;
import org.LexGrid.emf.naming.SupportedDataType;
import org.LexGrid.emf.naming.SupportedPropertyQualifier;
import org.LexGrid.emf.naming.SupportedSource;
import org.LexGrid.emf.valueDomains.ValueDomainDefinition;
import org.LexGrid.emf.versions.EntryState;
import org.LexGrid.versions.types.ChangeType;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.lexgrid.valuedomain.LexEVSValueDomainServices;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainCodedNodeSet;
import org.lexgrid.valuedomain.dto.ResolvedValueDomainDefinition;
import org.lexgrid.valuedomain.impl.LexEVSValueDomainServicesImpl;

/**
 * JUnit for Value Domain extension.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEVSValueDomainServicesImplTest extends TestCase {
	private LexBIGService lbsm_ = null;
	private LexEVSValueDomainServices vds_;
	
	@Test
	public void testGetAllValueDomainsWithNoNames() throws LBException, URISyntaxException {
		URI[] uris = getValueDomainService().getAllValueDomainsWithNoName();
		ArrayList<URI> blankURIS = new ArrayList<URI>();
		
		for(URI uri : uris)
		{
			blankURIS.add(uri);
		}
		
		assertTrue(blankURIS.contains((URI)new URI("SRITEST:AUTO:AutomobilesNoName")));
	}

	@Test
	public void testGetCodingSchemesInValueDomain() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList csvrList = getValueDomainService().getCodingSchemesInValueDomain(new URI("SRITEST:FA:HyphaInMycelium"));
		String urn = null;
		String version = null;
		
		for (int i = 0; i < csvrList.getAbsoluteCodingSchemeVersionReferenceCount(); i++){
			AbsoluteCodingSchemeVersionReference csvr = csvrList.getAbsoluteCodingSchemeVersionReference(i);
			urn = csvr.getCodingSchemeURN();
			version = csvr.getCodingSchemeVersion();
		}
		
        assertTrue(urn.equals("urn:lsid:bioontology.org:fungal_anatomy"));
        assertTrue(version == null || version.equals("UNASSIGNED"));
		
		csvrList = getValueDomainService().getCodingSchemesInValueDomain(new URI("SRITEST:AUTO:Ford"));
		
		for (int i = 0; i < csvrList.getAbsoluteCodingSchemeVersionReferenceCount(); i++){
			AbsoluteCodingSchemeVersionReference csvr = csvrList.getAbsoluteCodingSchemeVersionReference(i);
			urn = csvr.getCodingSchemeURN();
			version = csvr.getCodingSchemeVersion();
		}
	}

	@Test
	public void testGetValueDomainDefinition() throws LBException, URISyntaxException {
		ValueDomainDefinition vdDef = getValueDomainService().getValueDomainDefinition(new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"));
		
		assertTrue(vdDef.getDefaultCodingScheme().equals("fungal_anatomy"));
		assertTrue(vdDef.getDefinitionEntry().size() == 2);
		
		assertTrue(vdDef.getStatus().equals("ACTIVE"));
		assertTrue(vdDef.getIsActive());
		
		// value domain definition source
		List<Source> srcs = vdDef.getSource();
		assertTrue(srcs.size() == 2);
		for(Source src : srcs)
		{
			String val = src.getValue();
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
		assertTrue(vdDef.getRepresentsRealmOrContext().size() == 3);		
		Iterator<String> cItr = vdDef.getRepresentsRealmOrContext().iterator();		
		while(cItr.hasNext())
		{
			String context = cItr.next();
			assertTrue(context.equals("OBO") || context.equals("Fungal") || context.equals("Anatomy"));
		}
		
		
		// value domain definition entry state
		EntryState vdES = vdDef.getEntryState();
		
		assertTrue(vdES.getContainingRevision().equals("R001"));
		assertTrue(vdES.getPrevRevision().equals("R00A"));
		assertTrue(vdES.getChangeType().getName().equals(ChangeType.NEW.value()));
		
		Mappings maps = vdDef.getMappings();
		List<SupportedAssociation> supAssns = maps.getSupportedAssociation();
		
        assertTrue(supAssns.size() == 4);
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
        
        List<SupportedDataType> supDTs = maps.getSupportedDataType();
        assertTrue(supDTs.size() == 3);
        matchCount = supDTs.size();
        for (SupportedDataType supDT : supDTs)
        {
            if(supDT.getLocalId().equals("testhtml")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getValue().equals("test/html"));
            } else if(supDT.getLocalId().equals("textplain")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getValue().equals("text/plain"));
            } if(supDT.getLocalId().equals("text")) {
                matchCount--;
                assertTrue(StringUtils.isEmpty(supDT.getUri()));
                assertTrue(supDT.getValue().equals("text"));
            }
        }
        assertTrue(matchCount == 0);
        supDTs.clear();
		
		List<SupportedPropertyQualifier> supPQs = maps.getSupportedPropertyQualifier();
		
		for(SupportedPropertyQualifier supPQ : supPQs)
		{
			assertTrue(supPQ.getLocalId().equals("PropQual A Namuuuuu") || supPQ.getLocalId().equals("PropQual B Namuuuuu"));
		}
		supPQs.clear();
		
        List<SupportedSource> supSrcs = maps.getSupportedSource();
        assertTrue(supSrcs.size() == 4);
        for(SupportedSource supSrc : supSrcs)
        {
            assertTrue(supSrc.getLocalId().equals("OBO") ||
                       supSrc.getLocalId().equals("Fungal") || 
                       supSrc.getLocalId().equals("lexgrid.org") || 
                       supSrc.getLocalId().equals("_111101") );
        }
        supSrcs.clear();
		
		Property prop = (Property) vdDef.getProperties().getProperty().get(0);
		
		assertTrue(prop.getValue().getValue().equals("microbial structure ontology AND HyphaInMycelium"));
		
		// property source
		assertTrue(prop.getSource().size() == 2);
		Iterator<Source> srcItr = prop.getSource().iterator();
		while(srcItr.hasNext()){
			Source source = srcItr.next();
			
			String srcValue = source.getValue();
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
		assertTrue(prop.getUsageContext().size() == 2);
		cItr = prop.getUsageContext().iterator();
		while(cItr.hasNext())
		{
			String context = cItr.next();
			assertTrue(context.equals("PropUsageContext OBO") || context.equals("PropUsageContext Fungal"));
		}
		
		
		// property qualifier
		assertTrue(prop.getPropertyQualifier().size() == 2);
		Iterator<PropertyQualifier> pItr = prop.getPropertyQualifier().iterator();
		while(pItr.hasNext())
		{
			PropertyQualifier pQual = pItr.next();
			String value = pQual.getValue().getValue();
			String dataType = pQual.getValue().getDataType();
			String type = pQual.getPropertyQualifierType();
			String name = pQual.getPropertyQualifierName();
			
			assertTrue(value.equals("PropQualValue MicrobialStructureOntology") || value.equals("PropQualValue HyphaInMycelium"));
			assertTrue(name.equals("PropQual A Namuuuuu") || name.equals("PropQual B Namuuuuu"));
			assertTrue(type.equals("pQual Type A") || type.equals("pQual Type B"));
		}
		
		EntryState propES = prop.getEntryState();
		
		// entry state for this property
		assertTrue(propES.getContainingRevision().equals("PR001"));
		assertTrue(propES.getPrevRevision().equals("PR00A"));
		assertTrue(propES.getChangeType().getName().equals(ChangeType.NEW.value()));
	}

	@Test
	public void testGetValueDomainEntitiesForTerm() throws LBException, URISyntaxException {
		
		ResolvedValueDomainCodedNodeSet vdcns = getValueDomainService().getValueDomainEntitiesForTerm("General Motors", MatchAlgorithms.exactMatch.name(), new URI("SRITEST:AUTO:AllDomesticANDGM"), null, null);
		CodedNodeSet cns = null;
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
		
		vdcns = getValueDomainService().getValueDomainEntitiesForTerm("General Motors", MatchAlgorithms.exactMatch.name(), new URI("SRITEST:AUTO:AllDomesticANDGM1"), null, null);
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
        

		
		vdcns = getValueDomainService().getValueDomainEntitiesForTerm("General Motors", MatchAlgorithms.exactMatch.name(), new URI("SRITEST:AUTO:AllDomesticButGM"), null, null);
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
		
		vdcns = getValueDomainService().getValueDomainEntitiesForTerm("hypha", MatchAlgorithms.LuceneQuery.name(), new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null, null);
		
		if (vdcns != null)
		{
			cns = vdcns.getCodedNodeSet();
			csvrList = vdcns.getCodingSchemeVersionRefList();
			
			rcrList = cns.resolveToList(null, null, null, null, false, 1024); // this should contain codes : FAO:0001004, FAO:0001003, FAO:0001014, FAO:0001013
			
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
	public void testIsEntityInDomainStringURI() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReference csvr = getValueDomainService().isEntityInDomain("Chevy", new URI("SRITEST:AUTO:EveryThing"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("Jaguar", new URI("SRITEST:AUTO:GM"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("GM", new URI("SRITEST:AUTO:AllDomesticButGM"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("Ford", new URI("SRITEST:AUTO:AllDomesticButGM"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("Ford", new URI("SRITEST:AUTO:AllDomesticANDGM1"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("Chevy", new URI("SRITEST:AUTO:AllDomesticANDGM1"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001013", new URI("SRITEST:FA:MicrobialStructureOntologyLeafOnly"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001004", new URI("SRITEST:FA:MicrobialStructureOntologyLeafOnly"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0000001", new URI("SRITEST:FA:MicrobialStructureOntologyLeafOnly"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001003", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0000001", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001004", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001013", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001001", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		assertTrue(csvr == null);
		
		csvr = getValueDomainService().isEntityInDomain("FAO:0001010", new URI("SRITEST:FA:MicrobialStructureOntologyAndHyphaInMycelium"), null);
		assertTrue(csvr == null);
	}

	@Test
	public void testIsEntityInDomainStringURICodingSchemeVersionOrTagURI() throws LBException, URISyntaxException {
	    AbsoluteCodingSchemeVersionReferenceList incsvrl = new AbsoluteCodingSchemeVersionReferenceList();
	    incsvrl.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "1.0"));	    
		AbsoluteCodingSchemeVersionReference csvr = getValueDomainService().isEntityInDomain("Focus", new URI("urn:oid:11.11.0.1"), new URI("SRITEST:AUTO:AllDomesticButGM"), incsvrl, null);
		assertTrue(csvr == null);
		
		incsvrl.setAbsoluteCodingSchemeVersionReference(0, Constructors.createAbsoluteCodingSchemeVersionReference("urn:oid:11.11.0.1", "2.0"));
        csvr = getValueDomainService().isEntityInDomain("Focus", new URI("urn:oid:11.11.0.1"), new URI("SRITEST:AUTO:AllDomesticButGM"), incsvrl, null);
		assertTrue(csvr != null);
		
		csvr = getValueDomainService().isEntityInDomain("Focus", new URI("urn:oid:11.11.0.1"), new URI("SRITEST:AUTO:AllDomesticButGM"), null, "devel");
        assertTrue(csvr == null);
        
        csvr = getValueDomainService().isEntityInDomain("Focus", new URI("urn:oid:11.11.0.1"), new URI("SRITEST:AUTO:AllDomesticButGM"), null, LBConstants.KnownTags.PRODUCTION.toString());
        assertTrue(csvr == null);
	}

	@Test
	public void testIsSubDomain() throws LBException, URISyntaxException {
	    //dumpValueDomainResolution(new URI("SRITEST:AUTO:DomesticAutoMakers"), new AbsoluteCodingSchemeVersionReferenceList());
	    //dumpValueDomainResolution(new URI("SRITEST:AUTO:GM"), new AbsoluteCodingSchemeVersionReferenceList());

		assertFalse(getValueDomainService().isSubDomain(new URI("SRITEST:AUTO:DomesticAutoMakers"), new URI("SRITEST:AUTO:GM"), null, null));
		
		assertTrue(getValueDomainService().isSubDomain(new URI("SRITEST:AUTO:GM"), new URI("SRITEST:AUTO:DomesticAutoMakers"), null, null));
		
		assertFalse(getValueDomainService().isSubDomain(new URI("SRITEST:AUTO:Ford"), new URI("SRITEST:AUTO:AllDomesticANDGM"), null, null));
	}

	@Test
	public void testListValueDomains() throws LBException {
		URI[] uris = getValueDomainService().listValueDomains("Domestic Auto Makers");
		String uri = uris[0].toString();
		
		assertTrue(uri.equals("SRITEST:AUTO:DomesticAutoMakers"));
		
		uris = getValueDomainService().listValueDomains(null);
		assertTrue(uris.length > 0);
	}

	@Test
	public void testResolveValueDomain() throws LBException, URISyntaxException {
		AbsoluteCodingSchemeVersionReferenceList csvList = new AbsoluteCodingSchemeVersionReferenceList();
		csvList.addAbsoluteCodingSchemeVersionReference(Constructors.createAbsoluteCodingSchemeVersionReference("Automobiles", "2.0"));
		ResolvedValueDomainDefinition rvdDef = getValueDomainService().resolveValueDomain(new URI("SRITEST:AUTO:Ford"), csvList, null);
		
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getValueDomainURI().equals(new URI("SRITEST:AUTO:Ford")));
		assertTrue(rvdDef.getValueDomainName().equals("Ford"));
		
		ArrayList<String> codes = new ArrayList<String>();
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
		rvdDef = getValueDomainService().resolveValueDomain(new URI("SRITEST:AUTO:Ford"), csvList, null);
		
		assertTrue(rvdDef.getDefaultCodingScheme().equals("Automobiles"));
		assertTrue(rvdDef.getCodingSchemeVersionRefList().getAbsoluteCodingSchemeVersionReference()[0].getCodingSchemeURN().equals("urn:oid:11.11.0.1"));
		assertTrue(rvdDef.getCodingSchemeVersionRefList().getAbsoluteCodingSchemeVersionReference()[0].getCodingSchemeVersion().equals("1.0"));
		assertTrue(rvdDef.getValueDomainURI().equals(new URI("SRITEST:AUTO:Ford")));
		assertTrue(rvdDef.getValueDomainName().equals("Ford"));
		
		codes = new ArrayList<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		assertTrue(codes.size() == 2);
		assertTrue(codes.contains("Jaguar"));
		assertTrue(codes.contains("Ford"));
		
		//***************------------------*****************************

		rvdDef = getValueDomainService().resolveValueDomain(new URI("SRITEST:AUTO:DomasticLeafOnly"), null, LBConstants.KnownTags.PRODUCTION.name());
		
		codes = new ArrayList<String>();
		while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
		{
			ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
			codes.add(rcr.getCode());
		}
		assertTrue(codes.size() == 5);
		assertFalse(codes.contains("73"));        // We never pull retired codes (!)
		assertTrue(codes.contains("Chevy"));
		assertTrue(codes.contains("F150"));
		assertTrue(codes.contains("Focus"));
		assertTrue(codes.contains("Jaguar"));
		assertTrue(codes.contains("Windsor"));
		codes.clear();
		
        rvdDef = getValueDomainService().resolveValueDomain(new URI("SRITEST:AUTO:AllDomesticANDGM1"), null, null);
        //dumpValueDomainResolution(new URI("SRITEST:AUTO:AllDomesticANDGM1"), null);
        
        codes = new ArrayList<String>();
        while (rvdDef.getResolvedConceptReferenceIterator().hasNext())
        {
            ResolvedConceptReference rcr = rvdDef.getResolvedConceptReferenceIterator().next();
            codes.add(rcr.getCode());
        }
        assertTrue("Size: " + codes.size(), codes.size() == 3);
        assertTrue(codes.contains("GM"));
        assertTrue(codes.contains("Chevy"));
        codes.clear();
	}

	@Test
	public void testIsDomain() throws LBException {
		assertTrue(getValueDomainService().isDomain("VD005", "Automobiles", Constructors.createCodingSchemeVersionOrTag(null, "2.0")));
	}
	
	private LexEVSValueDomainServices getValueDomainService(){
		if (vds_ == null) {
			LexEVSValueDomainServicesImpl valueDomainService = new LexEVSValueDomainServicesImpl();
			valueDomainService.setLexBIGService(ServiceHolder.instance().getLexBIGService());
			vds_ = valueDomainService;
		}
		return vds_;
	}
	
	protected void dumpValueDomainResolution(URI vdURI, AbsoluteCodingSchemeVersionReferenceList csvs) {
	    try {     
            ResolvedValueDomainDefinition resDef = getValueDomainService().resolveValueDomain(vdURI, csvs, null);
            Iterator<AbsoluteCodingSchemeVersionReference> csList = resDef.getCodingSchemeVersionRefList().iterateAbsoluteCodingSchemeVersionReference();
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