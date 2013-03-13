/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
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
package org.LexGrid.LexBIG.Extensions.Index;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Extensions.Load.Loader;

/**
 * Manages registered index extensions. A single loader may be used to create
 * and maintain multiple indexes over one or more coding schemes.
 * <p>
 * It is the responsibility of the loader to properly interpret each index it
 * services by name, version, and provider.
 */
public interface IndexLoader extends Loader {

	/**
	 * Remove index entries for the designated coding scheme.
	 * 
	 * @param ref
	 *            Reference to the target coding scheme. If null, indexes will
	 *            be removed for all available content.
	 * @param index
	 *            The index to be cleared. If null or empty, all indexes
	 *            controlled by the loader are removed.
	 * @param async
	 *            Flag controlling whether this operation occurs in the calling
	 *            thread. If true, the load will occur in a separate
	 *            asynchronous process. If false, this method blocks until the
	 *            operation completes or fails. Regardless of setting, the
	 *            getStatus and getLog calls are used to fetch results.
	 * @throws LBInvocationException
	 */
	public void clear(AbsoluteCodingSchemeVersionReference ref, Index index,
			boolean async) throws LBException;

	/**
	 * Load index entries for the designated coding scheme.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param ref
	 *            Reference to the target coding scheme. If null, indexes will
	 *            be created for all available content.
	 * @param index
	 *            The index to be created. If null or empty, all indexes
	 *            controlled by the loader are created.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether this operation occurs in the calling
	 *            thread. If true, the load will occur in a separate
	 *            asynchronous process. If false, this method blocks until the
	 *            operation completes or fails. Regardless of setting, the
	 *            getStatus and getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void load(AbsoluteCodingSchemeVersionReference ref, Index index,
			boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Force (re)build of index entries for the designated coding scheme.
	 * 
	 * @param ref
	 *            Reference to the target coding scheme. If null, indexes will
	 *            be rebuilt for all available content.
	 * @param index
	 *            The index to be rebuilt. If null or empty, all indexes
	 *            controlled by the loader are rebuilt.
	 * @param async
	 *            Flag controlling whether this operation occurs in the calling
	 *            thread. If true, the load will occur in a separate
	 *            asynchronous process. If false, this method blocks until the
	 *            operation completes or fails. Regardless of setting, the
	 *            getStatus and getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void rebuild(AbsoluteCodingSchemeVersionReference ref, Index index,
			boolean async) throws LBException;

}