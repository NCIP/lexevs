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

/**
 * The Interface ValueSetDefinitionLoader.
 * 
 * @author <a href="mailto:kanjamala.pradip@mayo.edu">Pradip Kanjamala</a>
 */
public interface ResolvedValueSetDefinitionLoader  extends Loader {
	
	public static String NAME = "ResolvedValueSetDefinitionLoader";
	public static String VERSION = "1.0";
	public static String DESCRIPTION = "This loader loads the Resolved Value Set Definition  into the LexGrid database.";
	
	
	/* @param valueSetDefinitionURI - the URI of the value set definition
	 * @param valueSetDefintionRevisionId - the version of the value set definition
	 * @param csVersionList - a list of coding scheme URI's and versions to be used.  These will be used only if they are present in the service.  If absent, the most recent version will be used instead.
	 * @param csVersionTag - the tag (e.g "devel", "production", ...) to be used to reconcile coding schemes when more than one is present. Note that non-tagged versions will be used if the tagged version is missing.
	 */

	public void load(URI valueSetDefinitionURI, String valueSetDefinitionRevisionId, AbsoluteCodingSchemeVersionReferenceList csVersionList, String csVersionTag, String version) throws Exception;

	public void validate(URI valueSetDefinitionURI, int validationLevel) throws Exception;

	

}