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
package org.cts2.internal.model.uri.factory;

import junit.framework.Assert;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.cts2.internal.model.uri.DefaultEntityDirectoryURI;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.uri.EntityDirectoryURI;


/**
 * A factory for creating CodeSystemVersionDirectoryURIFactory objects.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class EntityDirectoryURIFactory extends AbstractCompositeDirectoryURIFactory<EntityDirectoryURI> {

	private NonIterableBasedResolvingRestrictionHandler<CodedNodeSet,EntityDirectoryURI> restrictionHandler;
	
	@Override
	protected EntityDirectoryURI doBuildDirectoryURI() {
		Assert.assertNotNull(this.restrictionHandler);

		return new DefaultEntityDirectoryURI(this.getLexBigService(), this.restrictionHandler, this.getBeanMapper());
	}

	public void setRestrictionHandler(NonIterableBasedResolvingRestrictionHandler<CodedNodeSet,EntityDirectoryURI> restrictionHandler) {
		this.restrictionHandler = restrictionHandler;
	}

	public NonIterableBasedResolvingRestrictionHandler<CodedNodeSet,EntityDirectoryURI> getRestrictionHandler() {
		return restrictionHandler;
	}
}
