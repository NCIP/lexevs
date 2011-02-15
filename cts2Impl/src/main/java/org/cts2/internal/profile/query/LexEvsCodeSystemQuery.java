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
import org.cts2.profile.query.CodeSystemQuery;
import org.cts2.uri.DirectoryURI;

/**
 * The Class LexEvsCodeSystemQuery.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsCodeSystemQuery extends AbstractBaseQueryService<CodeSystemDirectory> implements CodeSystemQuery {

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.CodeSystemQuery#getAllCodeSystems()
	 */
	@Override
	public DirectoryURI<CodeSystemDirectory> getAllCodeSystems() {
		return this.getDirectoryURIFactory().getDirectoryURI();
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.CodeSystemQuery#resolveAsList(org.cts2.uri.DirectoryURI)
	 */
	@Override
	public CodeSystemList resolveAsList(
			DirectoryURI<CodeSystemDirectory> directoryUri) {
		// TODO Auto-generated method stub
		return null;
	}
}
