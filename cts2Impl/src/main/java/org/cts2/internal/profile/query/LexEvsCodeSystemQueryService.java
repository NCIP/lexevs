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
package org.cts2.internal.profile.query;

import org.cts2.codesystem.CodeSystemDirectory;
import org.cts2.codesystem.CodeSystemList;
import org.cts2.profile.query.CodeSystemQueryService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.CodeSystemDirectoryURI;

/**
 * The Class LexEvsCodeSystemQuery.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsCodeSystemQueryService 
	extends AbstractBaseQueryService<CodeSystemDirectoryURI> 
	implements CodeSystemQueryService {

	@Override
	public CodeSystemDirectoryURI getAllCodeSystems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemDirectory resolve(
			CodeSystemDirectoryURI codeSystemQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CodeSystemList resolveAsList(
			CodeSystemDirectoryURI codeSystemQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
