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
package org.lexevs.dao.index.operation;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.lexevs.dao.index.indexer.IndexCreator;

public class DefaultLexEvsIndexOperations implements LexEvsIndexOperations {
	
	private IndexCreator indexCreator;
	
	@Override
	public void registerCodingSchemeEntityIndex(String codingSchemeUri,
			String version) {
		AbsoluteCodingSchemeVersionReference ref = new AbsoluteCodingSchemeVersionReference();
		ref.setCodingSchemeURN(codingSchemeUri);
		ref.setCodingSchemeVersion(version);
		
		indexCreator.index(ref, null, true);
	}
	
	public void setIndexCreator(IndexCreator indexCreator) {
		this.indexCreator = indexCreator;
	}
	public IndexCreator getIndexCreator() {
		return indexCreator;
	}
}
