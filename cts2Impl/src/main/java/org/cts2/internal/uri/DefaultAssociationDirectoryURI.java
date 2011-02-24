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
package org.cts2.internal.uri;

import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.cts2.association.AssociationDirectory;
import org.cts2.association.AssociationList;
import org.cts2.core.Filter;
import org.cts2.internal.mapper.BeanMapper;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.AssociationDirectoryURI;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 *
 */
public class DefaultAssociationDirectoryURI extends AbstractLexEvsDirectoryURI implements AssociationDirectoryURI{

	public DefaultAssociationDirectoryURI(LexBIGService lexBIGService,
			BeanMapper beanMapper) {
		super(lexBIGService, beanMapper);
	}

	@Override
	public int count(ReadContext readContext) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object marshall() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unmarshall() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AssociationDirectoryURI restrict(Filter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationDirectory resolve(
			QueryControl queryControl, ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssociationList resolveAsList(
			QueryControl queryControl, ReadContext readContext) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
