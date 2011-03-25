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

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.cts2.codesystemversion.CodeSystemVersion;
import org.cts2.internal.model.resource.factory.CodeSystemVersionFactory;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.profile.read.CodeSystemVersionReadService;
import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.utility.ExecutionUtils;

/**
 * The Class LexEvsCodeSystemVersionReadService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class LexEvsCodeSystemVersionReadService extends AbstractBaseReadService<CodeSystemVersion> implements CodeSystemVersionReadService {
 
	
	/** The code system version factory. */
	private CodeSystemVersionFactory codeSystemVersionFactory;
	
	/* (non-Javadoc)
	 * @see org.cts2.profile.read.CodeSystemVersionReadService#existsCodeSystemVersionForCodeSystem(org.cts2.service.core.NameOrURI, org.cts2.service.core.NameOrURI, org.cts2.service.core.ReadContext)
	 */
	@Override
	public boolean existsCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem,
			NameOrURI tag, 
			ReadContext context) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.read.CodeSystemVersionReadService#existsExternalId(org.cts2.service.core.NameOrURI, java.lang.String, org.cts2.service.core.ReadContext)
	 */
	@Override
	public boolean existsExternalId(
			NameOrURI codeSystem,
			String externalIdentifier, 
			ReadContext context) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public CodeSystemVersion read(
			NameOrURI id, 
			QueryControl queryControl, 
			ReadContext readContext) {
		
		return this.doRead(id, queryControl, readContext);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.read.BaseReadService#exists(org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public boolean exists(
			NameOrURI id, 
			QueryControl queryControl,
			ReadContext readContext) {
		return this.doRead(
				id, 
				queryControl, 
				readContext) != null;
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.read.CodeSystemVersionReadService#getCodeSystemVersionByExternalId(org.cts2.service.core.NameOrURI, java.lang.String, org.cts2.service.core.QueryControl)
	 */
	@Override
	public boolean getCodeSystemVersionByExternalId(
			NameOrURI codeSystem,
			String externalIdentifier, 
			QueryControl queryControl) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.read.CodeSystemVersionReadService#getCodeSystemVersionForCodeSystem(org.cts2.service.core.NameOrURI, org.cts2.service.core.NameOrURI, org.cts2.service.core.QueryControl)
	 */
	@Override
	public boolean getCodeSystemVersionForCodeSystem(
			NameOrURI codeSystem,
			NameOrURI tag, 
			QueryControl queryControl) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.cts2.internal.profile.read.AbstractBaseReadService#doRead(org.cts2.service.core.NameOrURI)
	 */
	protected CodeSystemVersion doRead(
			final NameOrURI nameOrUri, 
			QueryControl queryControl, 
			final ReadContext readContext) {
		
		ProfileUtils.validateReadContext(readContext);

		return ExecutionUtils.callWithTimeout(new Callable<CodeSystemVersion>(){

			@Override
			public CodeSystemVersion call() throws Exception {
			    String revisionId = null;
			    Date revisionDate  = null;
				
			    if(readContext != null){
			    	revisionId = readContext.getChangeSetContext();
			    	revisionDate = readContext.getReferenceTime();
				}
			    
			    if(StringUtils.isNotBlank(revisionId)){
			    	return codeSystemVersionFactory.getCodeSystemVersionByRevisionId(nameOrUri, revisionId);
			    }
			    
			    if(revisionDate != null){
			    	return codeSystemVersionFactory.getCodeSystemVersionByDate(nameOrUri, revisionDate);
			    }
				
			    return codeSystemVersionFactory.getCurrentCodeSystemVersion(nameOrUri);
			}
			
		}, queryControl, TimeUnit.MILLISECONDS);	
	}
	

	/**
	 * Gets the code system version factory.
	 *
	 * @return the code system version factory
	 */
	public CodeSystemVersionFactory getCodeSystemVersionFactory() {
		return codeSystemVersionFactory;
	}

	/**
	 * Sets the code system version factory.
	 *
	 * @param codeSystemVersionFactory the new code system version factory
	 */
	public void setCodeSystemVersionFactory(
			CodeSystemVersionFactory codeSystemVersionFactory) {
		this.codeSystemVersionFactory = codeSystemVersionFactory;
	}
}
