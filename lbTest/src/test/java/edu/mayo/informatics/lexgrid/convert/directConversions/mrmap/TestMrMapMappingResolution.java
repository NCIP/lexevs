package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import java.io.File;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.loaders.LexGridMultiLoaderImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.LexBIGServiceManager;
import org.LexGrid.LexBIG.Utility.ConvenienceMethods;
import org.LexGrid.LexOnt.CodingSchemeManifest;

import edu.mayo.informatics.lexgrid.convert.exceptions.LgConvertException;
import edu.mayo.informatics.lexgrid.convert.utility.ManifestUtil;
import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

public class TestMrMapMappingResolution extends TestCase {

	public void testResolutionToMappedSchemes() throws LBException{
		
		CodingSchemeVersionOrTag version = new CodingSchemeVersionOrTag();
		version.setVersion("200909");
			CodedNodeGraph cng = LexBIGServiceImpl.defaultInstance().getNodeGraph("MDR:MDR12_1_TO_CST:CST95", version, "CL413321");
			ResolvedConceptReferenceList list = cng.resolveAsList(null, true, false, 20, 3, null, null, null,null, 100);

			ResolvedConceptReference[] refs = list.getResolvedConceptReference();
			for(ResolvedConceptReference r: refs){
				assertNotNull(r.getEntity().getPresentation(0).getValue().getContent());
				Association assoc = r.getSourceOf().getAssociation(0);
				assertNotNull(assoc);
				AssociatedConcept concept = assoc.getAssociatedConcepts().getAssociatedConcept(0);
				assertNotNull(concept.getEntity().getPresentation(0).getValue().getContent());
				assertNotNull(r.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getEntity().getPresentation(0).getValue().getContent());

			}
		}

//    

	}


