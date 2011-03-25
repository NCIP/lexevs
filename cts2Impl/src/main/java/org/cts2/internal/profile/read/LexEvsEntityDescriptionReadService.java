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

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.cts2.core.EntityReference;
import org.cts2.entity.EntityDescription;
import org.cts2.entity.EntityList;
import org.cts2.internal.model.resource.factory.EntityDescriptionFactory;
import org.cts2.profile.read.EntityDescriptionReadService;
import org.cts2.service.core.EntityNameOrURI;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.utility.ExecutionUtils;
import org.cts2.valueset.ValueSetDefinition;

/**
 * The Class LexEvsCodeSystemVersionReadService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsEntityDescriptionReadService extends
		AbstractBaseReadService<EntityDescription> implements
		EntityDescriptionReadService {

	/** The code system version factory. */
	private EntityDescriptionFactory entityDescriptionFactory;

	private EntityDescription doRead(final EntityNameOrURI id,
			final NameOrURI codeSystemVersion, QueryControl queryControl,
			ReadContext readContext) {

		return ExecutionUtils.callWithTimeout(
				new Callable<EntityDescription>() {

					@Override
					public EntityDescription call() throws Exception {
						return entityDescriptionFactory.getEntityDescription(
								id, codeSystemVersion);
					}
				}, queryControl, TimeUnit.MILLISECONDS);
	}

	@Override
	public EntityDescription read(EntityNameOrURI id,
			NameOrURI codeSystemVersion, QueryControl queryControl,
			ReadContext readContext) {
		return this.doRead(id, codeSystemVersion, queryControl, readContext);
	}

	@Override
	public boolean exists(EntityNameOrURI id, NameOrURI codeSystemVersion,
			QueryControl queryControl, ReadContext readContext) {
		return this.doRead(id, codeSystemVersion, queryControl, readContext) != null;
	}

	public void setEntityDescriptionFactory(
			EntityDescriptionFactory entityDescriptionFactory) {
		this.entityDescriptionFactory = entityDescriptionFactory;
	}

	public EntityDescriptionFactory getEntityDescriptionFactory() {
		return entityDescriptionFactory;
	}

	@Override
	public EntityList readEntityDescriptions(final EntityNameOrURI id,
			QueryControl queryControl, ReadContext context) {
		
		return ExecutionUtils.callWithTimeout(new Callable<EntityList>() {
			@Override
			public EntityList call() throws Exception {
				return entityDescriptionFactory.getEntityDescriptionList(id);
			}
		}, queryControl, TimeUnit.MILLISECONDS );
	}

	@Override
	public EntityReference availableDescriptions(EntityNameOrURI id,
			ReadContext context) {
		return this.entityDescriptionFactory.availableDescriptions(id);
	}

	@Override
	public EntityDescription readByCodeSystem(EntityNameOrURI id,
			NameOrURI codeSystem, NameOrURI tag, QueryControl queryControl,
			ReadContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsInCodeSystem(EntityNameOrURI id, NameOrURI codeSystem,
			NameOrURI tag, ReadContext context) {
		// TODO Auto-generated method stub
		return false;
	}
}
