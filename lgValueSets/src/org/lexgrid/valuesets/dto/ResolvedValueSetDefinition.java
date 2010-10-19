/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.lexgrid.valuesets.dto;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.annotations.LgClientSideSafe;

/**
 * A resolved Value Domain definition containing the coding scheme version reference list
 * that was used to resolve the value domain and an iterator for resolved concepts.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
@LgClientSideSafe
public class ResolvedValueSetDefinition extends VSDSummary implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/
	
	private AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList;
	
	private ResolvedConceptReferencesIterator resolvedConceptReferenceIterator;

	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemeVersionRefList() {
		return codingSchemeVersionRefList;
	}

	public void setCodingSchemeVersionRefList(
			AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList) {
		this.codingSchemeVersionRefList = codingSchemeVersionRefList;
	}

	public ResolvedConceptReferencesIterator getResolvedConceptReferenceIterator() {
		return resolvedConceptReferenceIterator;
	}

	public void setResolvedConceptReferenceIterator(
			ResolvedConceptReferencesIterator resolvedConceptReferenceIterator) {
		this.resolvedConceptReferenceIterator = resolvedConceptReferenceIterator;
	}    

}