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

import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.internal.profile.AbstractBaseService;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;

/**
 * The Class AbstractBaseQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractBaseQueryService<T extends Directory> extends AbstractBaseService<T>{

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.BaseQueryService#resolve(org.cts2.uri.DirectoryURI, org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext)
	 */
	@Override
	public T resolve(DirectoryURI<T> directoryUri, QueryControl queryControl, ReadContext readContext) {
		return directoryUri.resolve(queryControl, readContext);
	}

	/* (non-Javadoc)
	 * @see org.cts2.profile.query.BaseQueryService#count(org.cts2.uri.DirectoryURI, org.cts2.service.core.ReadContext)
	 */
	@Override
	public int count(DirectoryURI<T> directoryUri, ReadContext readContext) {
		return directoryUri.count(readContext);
	}
	
	/* (non-Javadoc)
	 * @see org.cts2.profile.query.BaseQueryService#restrict(org.cts2.uri.DirectoryURI, org.cts2.core.Filter)
	 */
	@Override
	public T restrict(DirectoryURI<T> directoryUri, Filter filter) {
		return directoryUri.restrict(filter);
	}
}
