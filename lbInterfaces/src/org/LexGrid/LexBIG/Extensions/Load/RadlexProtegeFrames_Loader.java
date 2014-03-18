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
package org.LexGrid.LexBIG.Extensions.Load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;

/**
 * Validates and/or loads RadLex content, provided by the
 * Radiological Society of North America in Protégé frame format.
 */
public interface RadlexProtegeFrames_Loader extends Loader {

	/**
	 * Load the Radiological Lexicon (RadLex) ontology from a Protégé
	 * Frames (.pprj) file format.  This will also result in implicit
	 * generation of standard indices required by the LexBIG runtime.
	 * <p>
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            URI corresponding to the Protégé project (typically .pprj) file.
	 * @param stopOnErrors
	 *            True means stop if any load error is detected. False means
	 *            attempt to load what can be loaded if recoverable errors are
	 *            encountered.
	 * @param async
	 *            Flag controlling whether load occurs in the calling thread.  
	 *            If true, the load will occur in a separate asynchronous process.
	 *            If false, this method blocks until the load operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void load(URI source, boolean stopOnErrors, boolean async) throws LBException;

	/**
	 * Validate content for a candidate resource without performing a load.
	 * 
	 * Returns without exception if validation succeeds.
	 * 
	 * @throws LBException
	 * 
	 * @param source
	 *            URI corresponding to the Protégé project (typically .pprj) file.
	 * @param validationLevel
	 *            Supported levels of validation include: 0 = Verify the source file
	 *            exists.
	 * @throws LBException
	 */
	public void validate(URI source, int validationLevel) throws LBException;

}