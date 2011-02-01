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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension.Mapping;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;

public class MappingExtensionImplTest extends LexBIGServiceTestCase {
    final static String testID = "MappingExtensionImplTest";

	@Override
	protected String getTestID() {
		return testID;
	}

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
		
		assertEquals(5,count);
	}
	
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
		
		assertEquals(5,count);
	}
	
	
	
	@SuppressWarnings("unchecked")
	public void testResolveMappingSourceAndTargets() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		List<Tuple<String>> expectedTuples = new ArrayList(Arrays.asList(
				new Tuple<String>("Jaguar", "E0001"),
				new Tuple<String>("A0001", "R0001"),
				new Tuple<String>("C0001", "T0001"),
				new Tuple<String>("005", "P0001"),
				new Tuple<String>("Ford", "T0001")));
				
		
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
		
		assertEquals(5,count);
	}
	
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
		
		assertEquals(5,count);
	}
	
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

	public void testGetMappingCodingSchemesEntityParticipatesIn() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		AbsoluteCodingSchemeVersionReferenceList list = mappingExtension.getMappingCodingSchemesEntityParticipatesIn(
				"C0001", null);
		
		assertEquals(1,list.getAbsoluteCodingSchemeVersionReferenceCount());
		
		assertEquals(MAPPING_SCHEME_URI, list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeURN());
		assertEquals(MAPPING_SCHEME_VERSION, list.getAbsoluteCodingSchemeVersionReference(0).getCodingSchemeVersion());
	}
	
	public void testGetMapping() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
		
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		assertNotNull(mapping);
	}
	
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
	
	public void testResolveMappingWithRestriction() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("Jaguar", SearchDesignationOption.ALL, "LuceneQuery", null);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		assertTrue(itr.next().getCode().equals("Jaguar"));
		assertFalse(itr.hasNext());	
	}

	public void testResolveMappingWithRestrictionMultiple() throws LBException {
		LexBIGService lbs = ServiceHolder.instance().getLexBIGService();
		MappingExtension mappingExtension = (MappingExtension) lbs.getGenericExtension("MappingExtension");
	
		Mapping mapping = mappingExtension.getMapping(
				MAPPING_SCHEME_URI, 
				Constructors.createCodingSchemeVersionOrTagFromVersion(MAPPING_SCHEME_VERSION), 
				"AutoToGMPMappings");
		
		mapping = mapping.restrictToMatchingDesignations("car", SearchDesignationOption.ALL, "LuceneQuery", null);
		
		ResolvedConceptReferencesIterator itr = mapping.resolveMapping();
		
		assertTrue(itr.hasNext());
		assertEquals("C0001",itr.next().getCode());
		assertTrue(itr.next().getCode().equals("005"));
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