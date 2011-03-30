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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.internal.model.uri.DefaultAssociationDirectoryURI;
import org.cts2.internal.model.uri.restrict.NonIterableBasedResolvingRestrictionHandler;
import org.cts2.internal.profile.ProfileUtils;
import org.cts2.uri.AssociationDirectoryURI;
import org.springframework.util.Assert;

/**
 * An Association Directory URI Factory
 * 
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class AssociationDirectoryURIFactory extends
		AbstractCompositeDirectoryURIFactory<AssociationDirectoryURI> {
	
	//TODO Check for correct implementation recent changes
	private NonIterableBasedResolvingRestrictionHandler<CodedNodeGraph,AssociationDirectoryURI> restrictionHandler;

	@Override
	protected AssociationDirectoryURI doBuildDirectoryURI() {
		Assert.notNull(this.restrictionHandler);		
			return new DefaultAssociationDirectoryURI(this.getLexBigService(), this.restrictionHandler, this.getBeanMapper());
	}

	public NonIterableBasedResolvingRestrictionHandler<CodedNodeGraph, AssociationDirectoryURI> getRestrictionHandler() {
		return restrictionHandler;
	}

	public void setRestrictionHandler(
			NonIterableBasedResolvingRestrictionHandler<CodedNodeGraph, AssociationDirectoryURI> restrictionHandler) {
		this.restrictionHandler = restrictionHandler;
	}
}
