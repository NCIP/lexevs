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
package org.cts2.internal.profile.read;

import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.entity.EntityDescription;
import org.cts2.internal.model.resource.factory.EntityDescriptionFactory;
import org.cts2.profile.read.EntityDescriptionReadService;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

/**
 * The Class LexEvsCodeSystemVersionReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsEntityDescriptionReadService extends AbstractBaseReadService<CodeSystemVersion> implements EntityDescriptionReadService {

	/** The code system version factory. */
	private EntityDescriptionFactory entityDescriptionFactory;

	@Override
	public EntityDescription read(
			EntityNameOrURI id,
			NameOrURI codeSystemVersion, 
			QueryControl queryControl,
			ReadContext readContext) {
		return this.entityDescriptionFactory.getEntityDescription(id, codeSystemVersion);
	}

	@Override
	public boolean exists(
			EntityNameOrURI id, 
			NameOrURI codeSystemVersion,
			QueryControl queryControl, 
			ReadContext readContext) {
		if (this.entityDescriptionFactory.getEntityDescription(id, codeSystemVersion) != null)
			return true;
		return false;
	}

	public void setEntityDescriptionFactory(EntityDescriptionFactory entityDescriptionFactory) {
		this.entityDescriptionFactory = entityDescriptionFactory;
	}

	public EntityDescriptionFactory getEntityDescriptionFactory() {
		return entityDescriptionFactory;
	}
}
