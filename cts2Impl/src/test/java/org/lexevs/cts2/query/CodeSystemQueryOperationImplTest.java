package org.lexevs.cts2.query;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Exceptions.LBResourceUnavailableException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.types.EntityTypes;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;
import org.junit.Test;

// Test cases are based on Automobiles.xml and German_Made_Parts.xml
public class CodeSystemQueryOperationImplTest {

	private CodeSystemQueryOperationImpl query = new CodeSystemQueryOperationImpl();

	@Test
	public void testListCodeSystems() {

		// an empty query example, fetch all codingschemes.
		CodingSchemeSummary queryByExample = new CodingSchemeSummary();
		CodingSchemeRenderingList results = query
				.listCodeSystems(queryByExample);
		assertEquals(true, results.getCodingSchemeRenderingCount() > 1);

		// search by uri
		queryByExample = new CodingSchemeSummary();
		queryByExample.setCodingSchemeURI("urn:oid:11.11.0.2");
		results = query.listCodeSystems(queryByExample);
		assertEquals(1, results.getCodingSchemeRenderingCount());
		assertEquals("GMP", results.getCodingSchemeRendering(0)
				.getCodingSchemeSummary().getFormalName());

		// search by version
		queryByExample = new CodingSchemeSummary();
		queryByExample.setRepresentsVersion("1.0");
		results = query.listCodeSystems(queryByExample);
		assertEquals("Automobiles", results.getCodingSchemeRendering(0)
				.getCodingSchemeSummary().getLocalName());

		// search by formalName
		queryByExample = new CodingSchemeSummary();
		queryByExample.setFormalName("autos");
		results = query.listCodeSystems(queryByExample);
		assertEquals("Automobiles", results.getCodingSchemeRendering(0)
				.getCodingSchemeSummary().getLocalName());

		// search by localName
		queryByExample = new CodingSchemeSummary();
		queryByExample.setLocalName("Automobiles");
		results = query.listCodeSystems(queryByExample);
		assertEquals("Automobiles", results.getCodingSchemeRendering(0)
				.getCodingSchemeSummary().getLocalName());

		// search by localName & version
		queryByExample = new CodingSchemeSummary();
		queryByExample.setLocalName("Automobiles");
		queryByExample.setCodingSchemeURI("8.0");
		results = query.listCodeSystems(queryByExample);
		assertEquals(0, results.getCodingSchemeRenderingCount());

		queryByExample = new CodingSchemeSummary();
		queryByExample.setLocalName("Automobiles");
		queryByExample.setCodingSchemeURI("urn:oid:11.11.0.1");
		results = query.listCodeSystems(queryByExample);
		assertEquals(1, results.getCodingSchemeRenderingCount());

	}

	@Test
	public void testGetCodeSystemDetailsTest() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		CodingScheme result = query.getCodeSystemDetails("Automobiles",
				versionOrTag);
		assertEquals("Automobiles", result.getCodingSchemeName());

		// null codingschemeName
		versionOrTag.setVersion("1.0");
		result = query.getCodeSystemDetails(null, versionOrTag);
		assertEquals(null, result);

	}

	@Test
	public void testListCodeSystemConcepts() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		// without sortoption
		ResolvedConceptReferencesIterator conceptIterator = query
				.listCodeSystemConcepts("Automobiles", versionOrTag,
						Constructors.createLocalNameList(EntityTypes.CONCEPT
								.toString()), null);
		try {
			assertEquals(true, conceptIterator.hasNext());
		} catch (LBResourceUnavailableException e) {
			fail("expect a not null iterator, but interupt by an exception");
		}

		// with sortoption
		try {
			conceptIterator = query.listCodeSystemConcepts("Automobiles",
					versionOrTag, Constructors
							.createLocalNameList(EntityTypes.CONCEPT.toString()),
					Constructors.createSortOptionList(new String[] { "code" },
							new Boolean[] { new Boolean(true) }));
			while(conceptIterator.hasNext()) {
				ResolvedConceptReference conRef = conceptIterator.next();
				System.out.println(conRef.getCode());
				
			}
		} catch (LBParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBResourceUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LBInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test
	public void testGetConceptDetails() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		Entity entity = query.getConceptDetails("Automobiles", versionOrTag,
				"C0001", "Automobiles");
		assertEquals("C0001", entity.getEntityCode());

	}

	@Test
	public void testListAssociationTypes() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		List<SupportedAssociation> associationList = query
				.listAssociationTypes("Automobiles", versionOrTag);
		assertEquals(false, associationList.isEmpty());

	}

	@Test
	public void testGetAssociationTypeDetails() {
		CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
		versionOrTag.setVersion("1.0");
		AssociationEntity associationEntity = query.getAssociationTypeDetails(
				"Automobiles", versionOrTag, "uses");
		assertEquals("uses", associationEntity.getEntityCode());

	}

}
