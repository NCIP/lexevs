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
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.uri.operation.Resolvable;
import org.cts2.uri.operation.Restrictable;

/**
 * The Class AbstractBaseQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractDirectoryResolvableQueryService
	<U extends Resolvable<D,L> & Restrictable<U> & DirectoryURI, 
	D extends Directory<U>, 
	L extends Directory<U>>
	extends AbstractBaseQueryService<U> {

	public D resolve(U directoryURI, QueryControl queryControl, ReadContext readContext) {
		return directoryURI.resolve(queryControl, readContext);
	}
	
	public L resolveAsList(U directoryURI, QueryControl queryControl, ReadContext readContext) {
		return directoryURI.resolveAsList(queryControl, readContext);
	}
}
