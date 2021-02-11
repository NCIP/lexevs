
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import junit.framework.TestCase;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.Generic.MappingExtension;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.custom.relations.TerminologyMapBean;

public class TestMrMapMappingResolution extends TestCase {

	public void testResolutionToMappedSchemes() throws LBException{
		
		CodingSchemeVersionOrTag version = new CodingSchemeVersionOrTag();
		version.setVersion("200909");
			CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance().getNodeGraph("MDR12_1_TO_CST95", version, "CL413321");
			ResolvedConceptReferenceList list = cng.resolveAsList(null, true, false, 20, 3, null, null, null,null, 100);

			ResolvedConceptReference[] refs = list.getResolvedConceptReference();
			for(ResolvedConceptReference r: refs){
				assertNotNull(r.getEntity().getPresentation(0).getValue().getContent());
				Association assoc = r.getSourceOf().getAssociation(0);
				assertNotNull(assoc);
				AssociatedConcept concept = assoc.getAssociatedConcepts().getAssociatedConcept(0);
				if(!concept.getCode().equals("HEM EYE"))
				{assertNotNull(concept.getEntity().getPresentation(0).getValue().getContent());
				assertNotNull(r.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getEntity().getPresentation(0).getValue().getContent());
				}

			}
		}
	
	public void testMrMapMappingResolutionMappingExtension() throws LBParameterException, LBInvocationException{
		MappingExtension mapExt = (MappingExtension) LexBIGServiceImpl.defaultInstance().getGenericExtension("MappingExtension");
		List<TerminologyMapBean> tmb = mapExt.resolveBulkMapping("MDR12_1_TO_CST95", "200909");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("THIRST")).findAny().get().getMapRank(), "2");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("EAR DIS")).findAny().get().getMapRank(), "1");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("PLEURAL DIS")).findAny().get().getMapRank(), "1");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("HYPERTROPHY")).findAny().get().getMapRank(), "1");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("PYELONEPHRITIS")).findAny().get().getMapRank(), "1");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("ANOMALY CONGEN")).findAny().get().getMapRank(), "2");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("SKIN DISCOLOR")).findAny().get().getMapRank(), "");
		//Asserted in mr map but no terminology map
		assertTrue(tmb.stream().noneMatch(x -> x.getTargetCode().equals("HEM EYE")));
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("ENTERITIS")).findAny().get().getMapRank(), "4");
		assertEquals(tmb.stream().filter(x -> x.getTargetCode().equals("EDEMA TONGUE")).findAny().get().getMapRank(), "1");
		
	}

	}