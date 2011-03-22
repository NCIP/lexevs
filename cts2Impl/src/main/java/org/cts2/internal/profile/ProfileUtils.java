/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.profile;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.DataModel.Core.types.CodingSchemeVersionStatus;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.cts2.core.EntityReference;

/**
 * The Class ProfileUtils.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ProfileUtils {

	/**
	 * Union all.
	 *
	 * @param lexBigService the lex big service
	 * @return the coded node set
	 * @throws LBException the LB exception
	 */
	public static CodedNodeSet unionAll(LexBIGService lexBigService) throws LBException {
		CodedNodeSet cns = null;
		for(CodingSchemeRendering csr : lexBigService.getSupportedCodingSchemes().getCodingSchemeRendering()){
			CodedNodeSet newCns = lexBigService.getNodeSet(
					csr.getCodingSchemeSummary().getCodingSchemeURI(),
					Constructors.createCodingSchemeVersionOrTagFromVersion(csr.getCodingSchemeSummary().getRepresentsVersion()),
					null);
			
			if(cns == null){
				cns = newCns;
			} else {
				cns = cns.union(newCns);
			}
		}
		
		return cns;
	}
	
	/**
	 * Gets the all active coding schemes.
	 *
	 * @param lexBigService the lex big service
	 * @return the all active coding schemes
	 * @throws LBException the LB exception
	 */
	public static List<AbsoluteCodingSchemeVersionReference> getAllActiveCodingSchemes(LexBIGService lexBigService) throws LBException {
		List<AbsoluteCodingSchemeVersionReference> returnList = new ArrayList<AbsoluteCodingSchemeVersionReference>();
		
		for(CodingSchemeRendering csr : lexBigService.getSupportedCodingSchemes().getCodingSchemeRendering()){
			if(csr.getRenderingDetail().getVersionStatus().equals(CodingSchemeVersionStatus.ACTIVE)){
				returnList.add(Constructors.createAbsoluteCodingSchemeVersionReference(csr.getCodingSchemeSummary()));
			}
		}
		
		return returnList;
	}
	
	
	public static ConceptReferenceList entityReferenceToConceptReferenceList(List<EntityReference> entities){
		ConceptReferenceList returnList = new ConceptReferenceList();
		
		for(EntityReference entityReference : entities) {
			ConceptReference ref = new ConceptReference();
			ref.setCode(entityReference.getLocalEntityName().getName());
			ref.setCodeNamespace(entityReference.getLocalEntityName().getNamespace());
			
			returnList.addConceptReference(ref);
		}
		
		return returnList;
	}
	
	public static CodedNodeGraph unionAllGraphs(LexBIGService lexBigService) throws LBException {
		CodedNodeGraph cng = null;
		for(CodingSchemeRendering csr : lexBigService.getSupportedCodingSchemes().getCodingSchemeRendering()){
			CodedNodeGraph newCns = lexBigService.getNodeGraph(
					csr.getCodingSchemeSummary().getCodingSchemeURI(),
					Constructors.createCodingSchemeVersionOrTagFromVersion(csr.getCodingSchemeSummary().getRepresentsVersion()),
					null);
			
			if(cng == null){
				cng = newCns;
			} else {
				cng = cng.union(newCns);
			}
		}
		
		return cng;
	}
}
