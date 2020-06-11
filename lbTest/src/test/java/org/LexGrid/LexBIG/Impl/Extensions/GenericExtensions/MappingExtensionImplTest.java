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
package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Direction;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping.SearchContext;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOption;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.MappingSortOptionName;
import org.LexGrid.LexBIG.Impl.CodedNodeSetImpl;
import org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions.mapping.CodedNodeSetBackedMapping;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.relations.Relations;
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder;
import org.apache.lucene.queryparser.flexible.standard.builders.StandardBooleanQueryNodeBuilder;
import org.aspectj.apache.bcel.classfile.Method;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.experimental.categories.Category;
import org.lexevs.dao.database.access.association.model.Triple;
import org.lexevs.locator.LexEvsServiceLocator;
import org.LexGrid.LexBIG.Utility.RemoveFromDistributedTests;

@RunWith(BlockJUnit4ClassRunner.class)
public class MappingExtensionImplTest extends LexBIGServiceTestCase {
    final static String testID = "MappingExtensionImplTest";

	@Override
	protected String getTestID() {
		return testID;
	}
	
	@Test
	public void testIsMappingCodingScheme() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		boolean isMappingCS = mappingExtension.isMappingCodingScheme(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION));
		assertTrue(isMappingCS);
	}
	
	@Test
	public void testNotMappingCodingScheme() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		boolean isMappingCS = mappingExtension.isMappingCodingScheme(
				AUTO_SCHEME, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(AUTO_VERSION));
		assertFalse(isMappingCS);
	}
	
	@Test
	public void testResolveMappingWithCodeDifferentNamespaceInSource() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToCodes(Constructors.createConceptReferenceList("C0002"), SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertFalse(ref.getCodeNamespace().equals( "Automobiles_Different_NS"));
		assertEquals(ref.getCodeNamespace(), "Automobiles");
		this.checkResolvedConceptReference(ref);
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testResolveMappingWithCodeDifferentNamespaceInTarget() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToCodes(Constructors.createConceptReferenceList("P0001"), SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertFalse(ref.getSourceOf().
				getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0)
				.getCodeNamespace().equals( "GermanMadePartsNamespace_Different_NS"));
		assertEquals(ref.getSourceOf().
				getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0)
				.getCodeNamespace(), "GermanMadePartsNamespace");
		this.checkResolvedConceptReference(itr.next());
		assertFalse(itr.hasNext());
	}

	@Test
	public void testResolveMappingCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings", 
				null);
		
		int count = 0;
		while(itr.hasNext()) {
			ResolvedConceptReference next = itr.next();
			assertNotNull(next);
			count++;
		}
		
		assertEquals(6,count);
	}
	
	@Test
	public void testResolveMappingAssociationCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings", 
				null);
		
		int count = 0;
		while(itr.hasNext()) {
			ResolvedConceptReference next = itr.next();
			assertEquals(1,next.getSourceOf().getAssociationCount());
			count++;
		}
		
		assertEquals(6,count);
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void testResolveMappingSourceAndTargets() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		@SuppressWarnings("rawtypes")
		List<Tuple<String>> expectedTuples = new ArrayList(Arrays.asList(
				new Tuple<String>("Jaguar", "E0001"),
				new Tuple<String>("A0001", "R0001"),
				new Tuple<String>("C0001", "E0001"),
				new Tuple<String>("C0002", "P0001"),
				new Tuple<String>("005", "P0001"),
				new Tuple<String>("Ford", "E0001")));
				
		
		ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings", 
				null);
		
		int count = 0;
		while(itr.hasNext()) {
			ResolvedConceptReference next = itr.next();
			assertNotNull(next);
			assertTrue(
					expectedTuples.remove(
							new Tuple<String>(next.getCode(), 
									next.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getCode())));
			
			count++;
		}
		
		assertEquals(6,count);
	}
	
	@Test
	public void testResolveMappingSourceAndTargetsHasEverything() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings", 
				null);
		
		int count = 0;
		while(itr.hasNext()) {
			ResolvedConceptReference next = itr.next();
			this.checkResolvedConceptReference(next);
			for(Association assoc : next.getSourceOf().getAssociation()) {
				for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
					this.checkResolvedConceptReference(ac);
				}
			}
			count++;
		}
		
		assertEquals(6,count);
	}
	
	@Test
	public void testHasQualifiers() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		ResolvedConceptReferencesIterator itr = mappingExtension.resolveMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings", 
				null);
		
		Map<String,NameAndValueList> foundQuals = new HashMap<String,NameAndValueList>();
		
		while(itr.hasNext()) {
			ResolvedConceptReference next = itr.next();
			for(Association assoc : next.getSourceOf().getAssociation()) {
				for(AssociatedConcept ac : assoc.getAssociatedConcepts().getAssociatedConcept()) {
					if(ac.getAssociationQualifiers() != null && ac.getAssociationQualifiers().getNameAndValueCount() > 0) {
						foundQuals.put(ac.getCode(), ac.getAssociationQualifiers());
					}
				}
			}
		}

		assertEquals(2,foundQuals.size());
	}

	@Test
	public void testGetMappingCodingSchemesEntityParticipatesIn() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		AbsoluteCodingSchemeVersionReferenceList list = mappingExtension.getMappingCodingSchemesEntityParticipatesIn(
				"C0001", null);
		
		assertEquals(1,list.getAbsoluteCodingSchemeVersionReferenceCount());
		
		assertEquals(MAPPING_SCHEME_URI, list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN());
		assertEquals(MAPPING_SCHEME_VERSION, list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion());
	}
	
	@Test
	public void testGetMappingCodingSchemesEntityWithNamespaceParticipatesIn() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		AbsoluteCodingSchemeVersionReferenceList list = mappingExtension.getMappingCodingSchemesEntityParticipatesIn(
				"C0001", "Automobiles");
		
		assertEquals(1,list.getAbsoluteCodingSchemeVersionReferenceCount());
		
		assertEquals(MAPPING_SCHEME_URI, list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN());
		assertEquals(MAPPING_SCHEME_VERSION, list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion());
	}
	
	@Test
	public void testGetMapping() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		assertNotNull(mapping);
	}
	
	@Test
	public void testResolveMapping() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
	}
	
	@Test
	public void testResolveMappingGet() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(2, itr.get(2, 4).getResolvedConceptReferenceCount());
	}
	
	@Test
	public void testResolveMappingGetOver() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(0, itr.get(200, 400).getResolvedConceptReferenceCount());
	}

	@Test
	public void testResolveMappingNoContainerName() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				null);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
	}

	@Test
	public void testBothResolveMappingStrategies() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		ResolvedConceptReferencesIterator itr1 = mapping.resolveMapping();
	
		Set<String> map1 = new HashSet<String>();

		while(itr1.hasNext()){
			map1.add(itr1.next().getCode());
		}

		ResolvedConceptReferencesIterator itr2 = mappingExtension.resolveMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings",
				null);
		
		Set<String> map2 = new HashSet<String>();
		while(itr2.hasNext()){
			map2.add(itr2.next().getCode());
		}

		assertEquals(map1, map2);
	}

	@Test
	public void testResolveMappingWithRestriction() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("Jaguar", SearchDesignationOption.ALL, "LuceneQuery", null, SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		assertTrue(itr.next().getCode().equals("Jaguar"));
		assertFalse(itr.hasNext());	
	}
	

	@Test
	public void testGetResourceSummariesTargetRestrictionCorrectNumRemaining() throws Exception {
		
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null);
		
		mapping = mapping.restrictToCodes(Constructors.createConceptReferenceList("E0001", "GermanMadePartsNamespace", null), SearchContext.TARGET_CODES);
		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
		list.add(mapOp);
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping(list);
		
		int count = 0;
		int numberRemaining = itr.numberRemaining();
		while(itr.hasNext()){
			ResolvedConceptReference ref = itr.next();
			System.out.println(ref.getCode());
			count++;
		}
		
		assertEquals(count, numberRemaining);
	}
	
//	@Test
//	public void testGetResourceSummariesTargetRestrictionPerformanceTest() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		
//		mapping = mapping.restrictToCodes(Constructors.createConceptReferenceList("A00.9", "ICD10"), SearchContext.TARGET_CODES);
////		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
////		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
////		list.add(mapOp);
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}	
//	
//	@Test
//	public void testGetResourceSummariesSourceRestrictionPerformanceTest() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		
//		mapping = mapping.restrictToCodes(Constructors.createConceptReferenceList("446672004", "SNOMEDCT_US"), SearchContext.SOURCE_CODES);
////		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
////		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
////		list.add(mapOp);
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}	
//	
//	@Test
//	public void testGetResourceSummariesSourceAndTargetRestrictionPerformanceTest() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		ConceptReferenceList list = new ConceptReferenceList();
//		list.addConceptReference(Constructors.createConceptReference("446672004", "SNOMEDCT_US"));
//		list.addConceptReference(Constructors.createConceptReference("A00.9", "ICD10"));
//		mapping = mapping.restrictToCodes(list, SearchContext.SOURCE_OR_TARGET_CODES);
////		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
////		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
////		list.add(mapOp);
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}
//	
//	@Test
//	public void testGetResourceSummariesSourceAndTargetRestrictionPerformanceTestDifferentTargetDiffSource() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		ConceptReferenceList list = new ConceptReferenceList();
//		list.addConceptReference(Constructors.createConceptReference("446672004", "SNOMEDCT_US"));
//		list.addConceptReference(Constructors.createConceptReference("A02.8", "ICD10"));
//		mapping = mapping.restrictToCodes(list, SearchContext.SOURCE_OR_TARGET_CODES);
////		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
////		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
////		list.add(mapOp);
//		long start = System.currentTimeMillis();
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		System.out.println(((System.currentTimeMillis() - start)/1000) + " seconds");
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}
//	
//	@Test
//	public void testGetResourceSummariesSourceAndTargetRestrictionPerformanceTest2sources() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		ConceptReferenceList list = new ConceptReferenceList();
//		list.addConceptReference(Constructors.createConceptReference("446672004", "SNOMEDCT_US"));
//		list.addConceptReference(Constructors.createConceptReference("63650001", "SNOMEDCT_US"));
//		list.addConceptReference(Constructors.createConceptReference("A00.9", "ICD10"));
//		mapping = mapping.restrictToCodes(list, SearchContext.SOURCE_OR_TARGET_CODES);
////		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
////		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
////		list.add(mapOp);
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}
//	
//	@Test
//	public void testGetResourceSummariesSourceAndTargetRestrictionPerformanceTest2Targets() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		ConceptReferenceList list = new ConceptReferenceList();
//		list.addConceptReference(Constructors.createConceptReference("A00.9", "ICD10"));
//		list.addConceptReference(Constructors.createConceptReference("A02.8", "ICD10"));
//		mapping = mapping.restrictToCodes(list, SearchContext.SOURCE_OR_TARGET_CODES);
////		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
////		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
////		list.add(mapOp);
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}
//	
//	@Test
//	public void testGetResourceSummariesSourceAndTargetRestrictionPerformanceTestTargets() throws Exception {
//		
//		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
//		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
//	
//		Mapping mapping = mappingExtension.getMapping(
//				"urn:oid:C3645687.SNOMEDCT_US.ICD10", 
//				Constructors.createCodingSchemeVersionOrTagFromVersion("20180901"), "C3645687");
//		mapping = mapping.restrictToMatchingDesignations("Cholera, unspecified", null, "LuceneQuery", null, SearchContext.SOURCE_OR_TARGET_CODES);
//		MappingSortOption mapOp = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);
//		List<MappingSortOption> list = new ArrayList<MappingSortOption>();
//		list.add(mapOp);
//		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
//		
//		int count = 0;
//		int numberRemaining = itr.numberRemaining();
//		while(itr.hasNext()){
//			ResolvedConceptReference ref = itr.next();
//			System.out.println(ref.getCode());
//			count++;
//		}
//		System.out.println("Count: " + count);
//		assertEquals(count, numberRemaining);
//	}
//	
//	
	
	
	@Test
	public void testGetResourceSummariesTargetRestrictionCorrectNumRemainingNoSort() throws Exception {
		
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), null);
		
		mapping = mapping.restrictToCodes(Constructors.createConceptReferenceList("E0001", "GermanMadePartsNamespace", null), SearchContext.TARGET_CODES);
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		int count = 0;
		itr.hasNext();
		int numberRemaining = itr.numberRemaining();
		while(itr.hasNext()){
			ResolvedConceptReference ref = itr.next();
			System.out.println(ref.getCode());
			count++;
		}
		assertEquals(count, numberRemaining);
	}
	
	
	
	@Test
	public void testResolveMappingWithRestrictionCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("Jaguar", SearchDesignationOption.ALL, "LuceneQuery", null, SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(1,itr.numberRemaining());
	}

	@Test
	public void testResolveMappingWithRestrictionEither() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		assertEquals("C0001",itr.next().getCode());
		assertTrue(itr.next().getCode().equals("005"));
		assertFalse(itr.hasNext());	
	}
	
	@Test
	public void testResolveMappingWithRestrictionEitherFromBothFromSourceAndTarget() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("ford OR engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		ConceptReference ref = itr.next();
		
		assertTrue(ref.getCode().equals("Ford") || ref.getCode().equals("Jaguar") || ref.getCode().equals("C0001"));
		assertTrue(itr.hasNext());
		
		ref = itr.next();
		assertTrue(ref.getCode().equals("Ford") || ref.getCode().equals("Jaguar") || ref.getCode().equals("C0001"));
		
		ref = itr.next();
		assertTrue(ref.getCode().equals("Ford") || ref.getCode().equals("Jaguar") || ref.getCode().equals("C0001"));
	
		assertFalse(itr.hasNext());	
	}
	
	@Test
	public void testResolveMappingWithRestrictionEitherCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();

		assertTrue(itr.numberRemaining() > 0);
	}
	
	@Test
	public void testResolveMappingWithRestrictionSource() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals("Jaguar",ref.getCode());
		
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(1, itr.numberRemaining());
	}
	

	@Test
	public void testResolveMappingWithRestrictionTarget() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		Set<String> codes = new HashSet<String>();
		for(int i=0;i<3;i++){
			codes.add(itr.next().getCode());
		}
		
		assertFalse(itr.hasNext());
		
		assertEquals(codes, new HashSet<String>(Arrays.asList("C0001", "Ford", "Jaguar")));
	}
	
	@Test
	public void testResolveMappingWithRestrictionTargetCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();

		assertEquals(3, itr.numberRemaining());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceAndTarget() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals("Jaguar",ref.getCode());
		
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceAndTargetCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(1, itr.numberRemaining());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceAndTargetAndEither() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine OR jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		ResolvedConceptReference ref = itr.next();
		assertEquals("Jaguar",ref.getCode());
		
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceAndTargetAndEitherCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine OR jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(1, itr.numberRemaining());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceAndTargetAndBothWrongEither() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		mapping = mapping.restrictToMatchingDesignations("___INVALID___", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();

		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testResolveMappingWithRestrictionSourceAndTargetAndBothWrongEitherCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("jaguar", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_CODES);
		mapping = mapping.restrictToMatchingDesignations("engine", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.TARGET_CODES);
		mapping = mapping.restrictToMatchingDesignations("___INVALID___", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertEquals(0, itr.numberRemaining());
	}
	
	@Test
	public void testResolveMappingWithRestrictionWithSortAsc() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("*", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		MappingSortOption so = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);

		List<MappingSortOption> sortOptionList = Arrays.asList(so);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping(sortOptionList);
		
		assertTrue(itr.hasNext());
		assertEquals("005",itr.next().getCode());
	}
	
	@Test
	public void testResolveMappingWithRestrictionWithSortAsCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("*", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		MappingSortOption so = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.ASC);

		List<MappingSortOption> sortOptionList = Arrays.asList(so);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping(sortOptionList);
		
		assertTrue(itr.hasNext());
		assertEquals("005",itr.next().getCode());
	}
	
	@Test
	public void testResolveMappingWithRestrictionWithSortDesc() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("*", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		MappingSortOption so = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.DESC);

		List<MappingSortOption> sortOptionList = Arrays.asList(so);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping(sortOptionList);

		assertEquals(6, itr.numberRemaining());
	}
	
	@Test
	public void testResolveMappingWithRestrictionWithSortDescCount() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("*", SearchDesignationOption.ALL, "LuceneQuery", null,  SearchContext.SOURCE_OR_TARGET_CODES);
		
		MappingSortOption so = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.DESC);

		List<MappingSortOption> sortOptionList = Arrays.asList(so);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping(sortOptionList);

		assertEquals(6, itr.numberRemaining());
	}
	

	@Test
	public void testRestrictToRelationshipWithSorting() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToRelationship("piston", SearchDesignationOption.ALL, "LuceneQuery", null, Constructors.createLocalNameList("mapsTo"));
		
		MappingSortOption so = new MappingSortOption(MappingSortOptionName.SOURCE_CODE, Direction.DESC);

		List<MappingSortOption> sortOptionList = Arrays.asList(so);
		
		//C0002, 005
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping(sortOptionList);
		
		assertTrue(itr.hasNext());
		assertEquals("C0002",itr.next().getCode());
		assertEquals("005",itr.next().getCode());
		
		assertFalse(itr.hasNext());
	}

	@Test
	public void testRestrictToRelationshipNoSorting() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToRelationship("piston", SearchDesignationOption.ALL, "LuceneQuery", null, Constructors.createLocalNameList("mapsTo"));
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		assertEquals("C0002",itr.next().getCode());
		assertEquals("005",itr.next().getCode());
		
		assertFalse(itr.hasNext());
	}
	
	@Test

	public void testRestrictToRelationshipNoSortingTwoAssociations() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToRelationship("engine", SearchDesignationOption.ALL, "LuceneQuery", null, Constructors.createLocalNameList(new String[]{"mapsTo", "hasPart"}));
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		
		String[] expectedCodes = new String[]{"C0001", "Jaguar", "Ford"};
		String[] codes = new String[]{itr.next().getCode(), itr.next().getCode(), itr.next().getCode()};
		
		Arrays.sort(expectedCodes);
		Arrays.sort(codes);
		assertTrue(Arrays.equals(codes, expectedCodes));
		
		assertFalse(itr.hasNext());
	}
	
	@Test
	public void testRestrictToRelationshipBadRelationship() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToRelationship("engine", SearchDesignationOption.ALL, "LuceneQuery", null, Constructors.createLocalNameList("INVALID!!!"));
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertFalse(itr.hasNext());
	}
	
	

	private void checkResolvedConceptReference(ResolvedConceptReference next) {
		assertNotNull(next);
		assertNotNull(next.getCode());
		assertNotNull(next.getCodeNamespace());
		assertNotNull(next.getCodingSchemeName());
		assertNotNull(next.getCodingSchemeURI());
		assertNotNull(next.getCodingSchemeVersion());
		assertNotNull(next.getEntityDescription().getContent());
	}
	
	private class Tuple<T> {
		private T a;
		private T b;
		
		private Tuple(T a, T b){
			this.a = a;
			this.b = b;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((a == null) ? 0 : a.hashCode());
			result = prime * result + ((b == null) ? 0 : b.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("rawtypes")
			Tuple other = (Tuple) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (a == null) {
				if (other.a != null)
					return false;
			} else if (!a.equals(other.a))
				return false;
			if (b == null) {
				if (other.b != null)
					return false;
			} else if (!b.equals(other.b))
				return false;
			return true;
		}

		private MappingExtensionImplTest getOuterType() {
			return MappingExtensionImplTest.this;
		}
	}

}