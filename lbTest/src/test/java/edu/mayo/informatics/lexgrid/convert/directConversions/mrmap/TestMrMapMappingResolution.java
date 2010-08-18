/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package edu.mayo.informatics.lexgrid.convert.directConversions.mrmap;

import junit.framework.TestCase;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AssociatedConcept;
import org.LexGrid.LexBIG.DataModel.Core.Association;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;

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
				if(!concept.getCode().equals("HEM EYE"))
				{assertNotNull(concept.getEntity().getPresentation(0).getValue().getContent());
				assertNotNull(r.getSourceOf().getAssociation(0).getAssociatedConcepts().getAssociatedConcept(0).getEntity().getPresentation(0).getValue().getContent());
				}

			}
		}

//    

	}