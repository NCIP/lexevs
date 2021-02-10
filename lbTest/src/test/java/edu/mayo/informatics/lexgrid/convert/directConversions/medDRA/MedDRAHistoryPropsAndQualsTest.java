
package edu.mayo.informatics.lexgrid.convert.directConversions.medDRA;


import static org.junit.Assert.assertTrue;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Impl.function.LexBIGServiceTestCase;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.commonTypes.Property;
import org.LexGrid.commonTypes.PropertyQualifier;
import org.LexGrid.concepts.Entities;
import org.LexGrid.concepts.Entity;
import org.junit.Before;
import org.junit.Test;

public class MedDRAHistoryPropsAndQualsTest extends DataLoadTestBase {
	private ResolvedConceptReferenceList refs;
	
	@Before
	public void setUp() throws Exception {
		super.setUp();
		CodingSchemeVersionOrTag version = new CodingSchemeVersionOrTag();
		version.setVersion(LexBIGServiceTestCase.MEDDRA_VERSION);
		CodedNodeSet set = lbs.getCodingSchemeConcepts(LexBIGServiceTestCase.MEDDRA_SCHEME, version);
		ConceptReferenceList list = Constructors.createConceptReferenceList("10016968");
		set = set.restrictToCodes(list);
		refs = set.resolveToList(null, null, null, 1);
	}

	@Test
	public void testForPropsAndQuals() {
		ResolvedConceptReference ref = refs.getResolvedConceptReference(0);
		ref.getCode();

		for (Property prop : ref.getEntity().getAllProperties()) {
			if (prop.getPropertyName().equals("EDIT_ACTION")) {

				assertEquals(prop.getValue().getContent(), "A");

				for (PropertyQualifier q : prop.getPropertyQualifier()) {
					if (q.getPropertyQualifierName().equals("lit_currency")) {
						assertEquals(q.getValue().getContent(), "Y");
					}
					if (q.getPropertyQualifierName().equals(
							"term_addition_version")) {
						assertEquals(q.getValue().getContent(), "2.1");
					}
					if (q.getPropertyQualifierName().equals("term_name")) {
						assertEquals(q.getValue().getContent(), "Foot edema");
					}
					if (q.getPropertyQualifierName().equals("term_type")) {
						assertEquals(q.getValue().getContent(), "LLT");
					}
				}

			}
			if (prop.getPropertyName().equals("UMLS_CUI")) {
				assertTrue(prop.getValue().getContent().equals("C0574002"));
			}

		}
	}	

}