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
package org.lexgrid.valuesets.dto;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * Contains coding scheme version reference list that was used to resolve the value domain
 * and the coded node set.
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ResolvedValueSetCodedNodeSet {

	private AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList_;

	private CodedNodeSet codedNodeSet_;

	public CodedNodeSet getCodedNodeSet() {
		return codedNodeSet_;
	}

	public void setCodedNodeSet(CodedNodeSet codedNodeSet) {
		this.codedNodeSet_ = codedNodeSet;
	}

	public AbsoluteCodingSchemeVersionReferenceList getCodingSchemeVersionRefList() {
		return codingSchemeVersionRefList_;
	}

	public void setCodingSchemeVersionRefList(
			AbsoluteCodingSchemeVersionReferenceList codingSchemeVersionRefList) {
		this.codingSchemeVersionRefList_ = codingSchemeVersionRefList;
	}
}