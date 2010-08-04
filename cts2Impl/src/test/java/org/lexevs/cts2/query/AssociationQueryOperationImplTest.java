package org.lexevs.cts2.query;

import static org.junit.Assert.*;

import org.LexGrid.LexBIG.DataModel.Collections.AssociatedConceptList;
import org.LexGrid.LexBIG.DataModel.Collections.AssociationList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.junit.Test;
import org.lexevs.dao.database.service.association.AssociationService.AssociationTriple;

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

		ConceptReference parentConRef = Constructors.createConceptReference(
				"005", "Automobiles", "Automobiles");
		ConceptReference childConRef = Constructors.createConceptReference("C",
				"Automobiles", "Automobiles");
		boolean result = query.computeSubsumptionRelationship("Automobiles",
				versionOrTag, "hasSubtype", parentConRef, childConRef);
		assertEquals(true, result);
	}

	@Test
	public void testDetermineTransitiveConceptRelationship() {
		String codingSchemeUri = "urn:oid:11.11.0.1";
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		String containerName = "relations";
		String associationName = "hasSubtype";
		String sourceCode = "005", sourceNS = "Automobiles", targetCode = "C", targetNS = "Automobiles";

		ResolvedConceptReference path = query
				.determineTransitiveConceptRelationship(codingSchemeUri,
						versionOrTag, containerName, associationName,
						sourceCode, sourceNS, targetCode, targetNS);
		// the path string should be
		// 005|,Automobiles->A|,Automobiles->B|,Automobiles->C|,Automobiles
		// now let is traverse the graph to see if it is correct

		// for root
		assertEquals("005", path.getCode());
		assertEquals("Automobiles", path.getCodeNamespace());
		assertEquals("urn:oid:11.11.0.1", path.getCodingSchemeURI());
		assertEquals("1.0", path.getCodingSchemeVersion());

		AssociationList assnList = path.getSourceOf();
		assertEquals(1, assnList.getAssociationCount());

		Association assn = assnList.getAssociation(0);
		if (assn == null)
			fail("associaton is null");
		assertEquals("hasSubtype", assn.getAssociationName());

		AssociatedConceptList assnConList = assn.getAssociatedConcepts();
		assertEquals(1, assnConList.getAssociatedConceptCount());

		// 2nd node
		ResolvedConceptReference assnCon = assnConList.getAssociatedConcept(0);
		assertEquals("A", assnCon.getCode());
		assertEquals("Automobiles", assnCon.getCodeNamespace());
		assertEquals("urn:oid:11.11.0.1", assnCon.getCodingSchemeURI());
		assertEquals("1.0", assnCon.getCodingSchemeVersion());

		assnList = assnCon.getSourceOf();
		assertEquals(1, assnList.getAssociationCount());

		assn = assnList.getAssociation(0);
		if (assn == null)
			fail("associaton is null");
		assertEquals("hasSubtype", assn.getAssociationName());

		assnConList = assn.getAssociatedConcepts();
		assertEquals(1, assnConList.getAssociatedConceptCount());

		// 3rd node
		assnCon = assnConList.getAssociatedConcept(0);
		assertEquals("B", assnCon.getCode());
		assertEquals("Automobiles", assnCon.getCodeNamespace());
		assertEquals("urn:oid:11.11.0.1", assnCon.getCodingSchemeURI());
		assertEquals("1.0", assnCon.getCodingSchemeVersion());

		assnList = assnCon.getSourceOf();
		assertEquals(1, assnList.getAssociationCount());

		assn = assnList.getAssociation(0);
		if (assn == null)
			fail("associaton is null");
		assertEquals("hasSubtype", assn.getAssociationName());

		assnConList = assn.getAssociatedConcepts();
		assertEquals(1, assnConList.getAssociatedConceptCount());

		// 4th node
		assnCon = assnConList.getAssociatedConcept(0);
		assertEquals("C", assnCon.getCode());
		assertEquals("Automobiles", assnCon.getCodeNamespace());
		assertEquals("urn:oid:11.11.0.1", assnCon.getCodingSchemeURI());
		assertEquals("1.0", assnCon.getCodingSchemeVersion());
	}

	@Test
	public void testGetAssociationDetails() {

		// association target
		String codingSchemeUri = "urn:oid:11.11.0.1";
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");

		AssociationTriple associationTriple = query.getAssociationDetails(
				codingSchemeUri, versionOrTag, "instance001");
		assertEquals("hasSubtype", associationTriple
				.getAssociationPredicateName());
		assertEquals("relations", associationTriple.getRelationContainerName());
		assertEquals("005", associationTriple.getAssociationSource()
				.getSourceEntityCode());
		assertEquals("Automobiles", associationTriple.getAssociationSource()
				.getSourceEntityCodeNamespace());
		assertEquals(1, associationTriple.getAssociationSource()
				.getTargetCount());
		assertEquals(0, associationTriple.getAssociationSource()
				.getTargetDataCount());
		assertEquals("A", associationTriple.getAssociationSource().getTarget(0)
				.getTargetEntityCode());
		assertEquals("Automobiles", associationTriple.getAssociationSource()
				.getTarget(0).getTargetEntityCodeNamespace());

		
		// target data
		codingSchemeUri = "CameraRegisteredName";
		versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("CameraV1");

		associationTriple = query.getAssociationDetails(codingSchemeUri,
				versionOrTag, "_@cff6b89f-2da0-4706-8649-e08af164e682");
		assertEquals("domain", associationTriple.getAssociationPredicateName());
		assertEquals("associations", associationTriple.getRelationContainerName());
		assertEquals("aperture", associationTriple.getAssociationSource()
				.getSourceEntityCode());
		assertEquals("camera", associationTriple.getAssociationSource()
				.getSourceEntityCodeNamespace());
		assertEquals(1, associationTriple.getAssociationSource()
				.getTargetDataCount());
		assertEquals(0, associationTriple.getAssociationSource()
				.getTargetCount());
		assertEquals("string", associationTriple.getAssociationSource()
				.getTargetData(0).getAssociationDataText().getContent());

	}
}
