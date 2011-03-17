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

import java.util.concurrent.Callable;

import org.cts2.core.Directory;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.DirectoryURI;
import org.cts2.utility.ExecutionUtils;

/**
 * The Class AbstractResolvingDirectoryURI.
 *
 * @param <T> the
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractResolvingDirectoryURI<T extends DirectoryURI> extends AbstractDirectoryURI<T> {
	
	/* (non-Javadoc)
	 * @see org.cts2.uri.DirectoryURI#get(org.cts2.service.core.QueryControl, org.cts2.service.core.ReadContext, java.lang.Class)
	 */
	public <D extends Directory<?>> D get(final QueryControl queryControl, final ReadContext readContext, final Class<D> content){
		final QueryControl validatedQueryControl = validateQueryControl(queryControl);
		return ExecutionUtils.callWithTimeout(new Callable<D>(){

			@Override
			public D call() {
				return doGet(validatedQueryControl, readContext, content);
			}
			
		}, queryControl);
	}

	/**
	 * Do resolve.
	 *
	 * @param <D> the
	 * @param format the format
	 * @param queryControl the query control
	 * @param readContext the read context
	 * @param resolveClass the resolve class
	 * @return the d
	 */
	protected abstract <D extends Directory<?>> D doGet(
			QueryControl queryControl,
			ReadContext readContext,
			Class<D> resolveClass);
}
