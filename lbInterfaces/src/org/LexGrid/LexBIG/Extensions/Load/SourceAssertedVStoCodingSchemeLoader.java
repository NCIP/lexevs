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
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.codingSchemes.CodingScheme;

/**
 * The Interface ValueSetDefinitionLoader.
 * 
 * @author <a href="mailto:bauer.scott@mayo.edu">Scott Bauer</a>
 */
public interface SourceAssertedVStoCodingSchemeLoader  extends Loader {
	
	public static String NAME = "SourceAssertedVStoCodingSchemeLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads a value set asserted by the source system instead of a rules based definition.";
	
	
	/*
	 * @param scheme - the Scheme to be loaded
	 */
  
	public void load(CodingScheme scheme);
	public void validate(URI valueSetDefinitionURI, int validationLevel) throws Exception;

}