package org.LexGrid.LexBIG.Impl.Extensions.GenericExtensions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.Impl.testUtility.ServiceHolder;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
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
