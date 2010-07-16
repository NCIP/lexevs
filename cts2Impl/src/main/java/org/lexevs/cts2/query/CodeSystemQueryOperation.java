/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.query;

import java.util.Iterator;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeSummary;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.codingSchemes.CodingScheme;
import org.LexGrid.concepts.Entity;
import org.LexGrid.naming.SupportedAssociation;
import org.LexGrid.relations.AssociationEntity;


public interface CodeSystemQueryOperation {

	public CodingSchemeRenderingList listCodeSystems(CodingSchemeSummary queryByExample);
	
	public CodingScheme getCodeSystemDetails(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag);
	
	public Iterator<ResolvedConceptReference> listCodeSystemConcepts(CodedNodeSet cns);
	
	public Entity getConceptDetails(String codingSchemeUri, CodingSchemeVersionOrTag versionOrTag, String code, String namespace);
	
	public List<SupportedAssociation> listAssociationTypes(String codingSchemeName, CodingSchemeVersionOrTag versionOrTag);
	
	public AssociationEntity getAssociationTypeDetails(String codingSchemeUri, CodingSchemeVersionOrTag versionOrTag, String associationName);

}
