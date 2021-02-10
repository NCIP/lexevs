
package org.LexGrid.LexBIG.mapping;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.LexEVSAuthoringServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.commonTypes.Properties;
import org.LexGrid.naming.Mappings;
import org.LexGrid.relations.Relations;
import org.LexGrid.versions.EntryState;

import junit.framework.TestCase;

public class LexEVSLoadedMappingsTest extends TestCase {

	private LexBIGService lbs;
	private CodingSchemeVersionOrTag csvt;
	private LexEVSAuthoringServiceImpl authoring;
	
	public void setUp(){
			authoring = new LexEVSAuthoringServiceImpl();
			lbs = LexBIGServiceImpl.defaultInstance();
			csvt = new CodingSchemeVersionOrTag();
			csvt.setVersion("1.0");
	
	}
	
	public void testLoadedMappedAssociations() throws LBException {
		String[] codes = new String[] { "T0001", "P0001", "A0001", "005" };
		List<String> codeList = Arrays.asList(codes);
		ConceptReference cr = new ConceptReference();
		cr.setCode("T0001");
		cr.setCodeNamespace("GermanMadePartsNamespace");
		cr.setCodingSchemeName("GermanMadeParts");
		ConceptReference cr2 = new ConceptReference();
		cr2.setCode("005");
		cr2.setCodeNamespace("Automobiles");
		cr2.setCodingSchemeName("Automobiles");
		CodedNodeGraph cng = lbs.getNodeGraph(
				"http://default.mapping.container", csvt, "Mapping_relations");
		ResolvedConceptReferenceList rcrl = cng.resolveAsList(
				cr, true, false, -1,
				-1, null, null, null, -1);
		ResolvedConceptReferenceList rcrl1 = cng.resolveAsList(
			cr2, false, true, -1,
				-1, null, null, null, -1);
		ResolvedConceptReference[] conceptList1 = rcrl
				.getResolvedConceptReference();
		ResolvedConceptReference[] conceptList2 = rcrl1
				.getResolvedConceptReference();
		for (ResolvedConceptReference rcr : conceptList1) {
			Association[] assoc = rcr.getSourceOf().getAssociation();
			for (Association a : assoc) {
				AssociatedConcept[] concepts = a.getAssociatedConcepts()
						.getAssociatedConcept();
				for (AssociatedConcept ac : concepts) {
					if (!codeList.contains(ac.getConceptCode())) {
						fail("code not found in associated concept list");
					}
				}
			}
		}

		for (ResolvedConceptReference rcr : conceptList2) {
			Association[] assoc = rcr.getTargetOf().getAssociation();
			for (Association a : assoc) {
				AssociatedConcept[] concepts = a.getAssociatedConcepts()
						.getAssociatedConcept();
				for (AssociatedConcept ac : concepts) {
					if (!codeList.contains(ac.getConceptCode())) {
						fail("code not found in associated concept list");
					}
				}
			}
		}
	}
	

	public void testLoadedMappingsEntities() throws LBException {

		CodedNodeSet cns = lbs.getCodingSchemeConcepts(
				"http://default.mapping.container", csvt);
		String[] codes = new String[] { "T0001", "P0001", "A0001", "005" };
		ConceptReferenceList list = ConvenienceMethods
				.createConceptReferenceList(codes);
		cns = cns.restrictToCodes(list);
		ResolvedConceptReferencesIterator rcrl = cns.resolve(null, null, null);
		List<String> codeList = Arrays.asList(codes);
		while (rcrl.hasNext()) {
			ResolvedConceptReference rcr = rcrl.next();
			if (!codeList.contains(rcr.getConceptCode())) {
				fail("code not found in concept reference list");
			}
		}
	}

}