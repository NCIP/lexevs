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
package org.cts2.profile.author;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.core.SourceAndNotation;
import org.cts2.service.codesystemversion.UpdateCodeSystemVersionRequest;
import org.cts2.service.core.NameOrURI;

/**
 * The Interface CodeSystemVersionAuthoringService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface CodeSystemVersionAuthoringService extends BaseAuthoringService {

	/**
	 * Creates the code system version.
	 *
	 * @param changeSetUri the change set uri
	 * @param documentUri the document uri
	 * @param name the name
	 * @param sourceAndNotation the source and notation
	 * @param versionOf the version of
	 */
	public void createCodeSystemVersion(String changeSetUri, String documentUri, String name, SourceAndNotation sourceAndNotation, NameOrURI versionOf);
	
	/**
	 * Update code system version.
	 *
	 * @param codeSystemVersion the code system version
	 * @param request the request
	 * @return the code system version
	 */
	public CodeSystemVersion updateCodeSystemVersion(NameOrURI codeSystemVersion, UpdateCodeSystemVersionRequest request);
}
