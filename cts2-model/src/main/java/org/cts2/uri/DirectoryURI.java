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
package org.cts2.uri;


import org.cts2.core.Directory;
import org.cts2.core.Filter;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;
import org.cts2.uri.restriction.RestrictionState;

/**
 * The Interface DirectoryURI.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface DirectoryURI {
	
	/**
	 * Gets the restriction state.
	 *
	 * @return the restriction state
	 */
	public RestrictionState<? extends DirectoryURI> getRestrictionState();
	
	/**
	 * Gets the.
	 *
	 * @param <T> the
	 * @param queryControl the query control
	 * @param readContext the read context
	 * @param content the content
	 * @return the t
	 */
	public <T extends Directory<?>> T get(QueryControl queryControl, ReadContext readContext, Class<T> content);

	/**
	 * Count.
	 *
	 * @param readContext the read context
	 * @return the int
	 */
	public int count(ReadContext readContext);
	
	/**
	 * Restrict.
	 *
	 * @param filter the filter
	 * @return the directory uri
	 */
	public DirectoryURI restrict(Filter filter);
	
	/**
	 * Union.
	 *
	 * @param <T> the
	 * @param directoryUri the directory uri
	 * @return the t
	 */
	public <T extends DirectoryURI> T union(T directoryUri);
	
	/**
	 * Intersect.
	 *
	 * @param <T> the
	 * @param directoryUri the directory uri
	 * @return the t
	 */
	public <T extends DirectoryURI> T intersect(T directoryUri);
	
	/**
	 * Difference.
	 *
	 * @param <T> the
	 * @param directoryUri the directory uri
	 * @return the t
	 */
	public <T extends DirectoryURI> T difference(T directoryUri);
	
	/**
	 * Marshall.
	 *
	 * @return the string
	 */
	public String marshall();
}
