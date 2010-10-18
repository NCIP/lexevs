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
package org.lexevs.cts2.admin.load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;
import org.lexevs.cts2.BaseService;

public class AssociationLoadOperationImpl extends BaseService implements AssociationLoadOperation {

	@Override
	public void importAssociationVersion(
			CodingScheme codeSystem, 
			URI metadata,
			Boolean stopOnErrors, 
			Boolean async, 
			Boolean overwriteMetadata,
			String versionTag, 
			Boolean activate) throws LBException {
		this.getCodeSystemLoadOperation().load(
				codeSystem, 
				metadata, 
				stopOnErrors, 
				async, 
				overwriteMetadata, 
				versionTag, 
				activate);
	}

	@Override
	public void importAssociationVersion(
			URI source, 
			URI metadata,
			URI manifest, 
			String loaderName,
			Boolean stopOnErrors, 
			Boolean async, 
			Boolean overwriteMetadata,
			String versionTag, 
			Boolean activate) throws LBException {
		this.getCodeSystemLoadOperation().load(
				source, 
				metadata, 
				manifest,
				loaderName, 
				stopOnErrors, 
				async, 
				overwriteMetadata, 
				versionTag, 
				activate);
	}

	protected CodeSystemLoadOperation getCodeSystemLoadOperation() {
		return this.getLexEvsCTS2().getAdminOperation().getCodeSystemLoadOperation();
	}
}