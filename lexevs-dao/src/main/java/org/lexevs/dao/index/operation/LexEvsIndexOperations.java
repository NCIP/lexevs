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

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;

/**
 * The Interface LexEvsIndexOperations.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface LexEvsIndexOperations {

	/**
	 * Register coding scheme in the index. No information is actually indexed, but all metadata
	 * and information is created and stored, enabling future indexing to occur on this coding scheme.
	 * 
	 * @param codingSchemeUri the coding scheme uri
	 * @param version the version
	 */
	public void registerCodingSchemeEntityIndex(String codingSchemeUri, String version);
	
	public void cleanUp(List<AbsoluteCodingSchemeVersionReference> expectedCodingSchemes, boolean reindexMissing);

	public String getLexEVSIndexLocation();
	
	public boolean doesIndexExist(AbsoluteCodingSchemeVersionReference ref) throws LBParameterException;
	
}