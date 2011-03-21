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
package org.cts2.internal.model.uri;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.internal.model.uri.restrict.AssociationRestrictionHandler;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;

/**
 * The Class DefaultEntityDirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultAssociationDirectoryURI extends AbstractNonIterableLexEvsBackedResolvingDirectoryURI<CodedNodeGraph,AssociationDirectoryURI> implements AssociationDirectoryURI {
	
	/** The coded node set. */
	private CodedNodeGraph codedNodeGraph;
	
	/** The bean mapper. */
	private BeanMapper beanMapper;
	
	/**
	 * Instantiates a new default code system version directory uri.
	 *
	 * @param codedNodeSet the coded node set
	 * @param restrictionHandler the restriction handler
	 * @param beanMapper the bean mapper
	 */
	public DefaultAssociationDirectoryURI(
			CodedNodeGraph codedNodeGraph,
			AssociationRestrictionHandler restrictionHandler,
			BeanMapper beanMapper) {
		super(restrictionHandler);
		this.codedNodeGraph = codedNodeGraph;
		this.beanMapper = beanMapper;
	}

	@Override
	protected CodedNodeGraph getOriginalState() {
		return this.codedNodeGraph;
	}

	@Override
	protected <O> O transform(CodedNodeGraph lexevsObject, Class<O> clazz) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected AssociationDirectoryURI clone() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	protected int doCount(ReadContext readContext) {
		// TODO Auto-generated method stub
		return 0;
	}

}
