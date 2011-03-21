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

import org.cts2.core.VersionTagReference;
import org.cts2.entity.EntityDirectory;
import org.cts2.entity.EntityList;
import org.cts2.profile.query.EntityDescriptionQueryService;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.EntityDirectoryURI;

/**
 * The Class LexEvsEntityDescriptionQueryService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a> <a
 *         href="mailto:lian.zonghui@mayo.edu">Zonghui Lian</a>
 */
public class LexEvsEntityDescriptionQueryService extends
		AbstractBaseQueryService<EntityDirectoryURI> implements
		EntityDescriptionQueryService {

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.EntityDescriptionQueryService#getEntities()
	 */
	@Override
	public EntityDirectoryURI getEntities() {
		return this.getDirectoryURIFactory().getDirectoryURI();
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.EntityDescriptionQueryService#resolve(org.cts2.uri.EntityDirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public EntityDirectory resolve(EntityDirectoryURI entityQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return entityQueryURI.get(queryControl, readContext,
				EntityDirectory.class);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.EntityDescriptionQueryService#resolveAsList(org.cts2.uri.EntityDirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public EntityList resolveAsList(EntityDirectoryURI codeSystemQueryURI,
			QueryControl queryControl, ReadContext readContext) {
		return codeSystemQueryURI.get(queryControl, readContext,
				EntityList.class);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.EntityDescriptionQueryService#restrictToCodeSystems(org.cts2.uri.EntityDirectoryURI, org.cts2.service.core.NameOrURI, org.cts2.core.VersionTagReference)
	 */
	@Override
	public EntityDirectoryURI restrictToCodeSystems(
			EntityDirectoryURI entityDirectoryUri, NameOrURI codeSystems,
			VersionTagReference tag) {
		return entityDirectoryUri.restrictToCodeSystems(codeSystems, tag);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.EntityDescriptionQueryService#restrictToCodeSystemVersions(org.cts2.uri.EntityDirectoryURI, org.cts2.service.core.NameOrURI)
	 */
	@Override
	public EntityDirectoryURI restrictToCodeSystemVersions(
			EntityDirectoryURI entityDirectoryUri, NameOrURI codeSystemVersions) {
		return entityDirectoryUri.restrictToCodeSystemVersions(codeSystemVersions);
	}
}
