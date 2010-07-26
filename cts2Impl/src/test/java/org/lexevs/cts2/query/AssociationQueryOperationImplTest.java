package org.lexevs.cts2.query;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;

public class AssociationQueryOperationImplTest {
	private AssociationQueryOperationImpl query = new AssociationQueryOperationImpl();

	@Test
	public void testListAssociations() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");

		// isBackward is false
		ResolvedConceptReferenceList list1 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005", null, false,
				-1, -1);
		assertEquals(3, list1.getResolvedConceptReference(0).getSourceOf()
				.getAssociation(0).getAssociatedConcepts()
				.getAssociatedConceptCount());

		// isBackward is true
		ResolvedConceptReferenceList list2 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005", null, true,
				-1, -1);
		assertEquals(1, list2.getResolvedConceptReference(0).getTargetOf()
				.getAssociation(0).getAssociatedConcepts()
				.getAssociatedConceptCount());

		// a not existed association name is specified.
		ResolvedConceptReferenceList list3 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005",
				"associationName", false, -1, -1);
		assertEquals(0, list3.getResolvedConceptReferenceCount());

		// an association name is specified.
		ResolvedConceptReferenceList list4 = query.listAssociations(
				"Automobiles", versionOrTag, "Automobiles", "005",
				"hasSubtype", false, -1, -1);
		assertEquals(3, list4.getResolvedConceptReference(0).getSourceOf()
				.getAssociation(0).getAssociatedConcepts()
				.getAssociatedConceptCount());

	}

	@Test
	public void testComputeSubsumptionRelationship() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		
		ConceptReference parentConRef = Constructors.createConceptReference("005", "Automobiles", "Automobiles");
		ConceptReference childConRef = Constructors.createConceptReference("C", "Automobiles", "Automobiles");
		boolean result = query.computeSubsumptionRelationship("Automobiles", versionOrTag, "hasSubtype", parentConRef, childConRef);
		assertEquals(true, result);
	}

	@Test
	public void testDetermineTransitiveConceptRelationship() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAssociationDetails() {
		fail("Not yet implemented");
	}

}
