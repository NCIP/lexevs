/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and 
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
package org.lexgrid.exporter.owlrdf.interfaces;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;

/**
 * Exports content to OWL/RDF format.
 */
public interface OwlRdfExporter extends Exporter {

	/**
	 * Export content from the underlying LexGrid repository.
	 * 
	 * An exception is raised if resources cannot be accessed or another load
	 * operation is already in progress.
	 * 
	 * @param source
	 *            The absolute version identifier of the coding scheme to export.
	 * @param destination
	 *            URI corresponding directory to write the the OWL/RDF file.
	 * @param overwrite
	 *            True indicates to overwrite an existing file if present. False
	 *            indicates to stop if the destination file already exists.
	 * @param stopOnErrors
	 *            True means stop if any export error is detected. False means
	 *            attempt to continue writing what can be exported if
	 *            recoverable errors are encountered.
	 * @param async
	 *            Flag controlling whether export occurs in the calling thread.
	 *            If true, the export will occur in a separate asynchronous
	 *            process. If false, this method blocks until the export
	 *            operation completes or fails. Regardless of setting, the
	 *            getStatus and getLog calls are used to fetch results.
	 * @throws LBException
	 */
	public void export(AbsoluteCodingSchemeVersionReference source,
			URI destination, boolean overwrite, boolean stopOnErrors,
			boolean async) throws LBException;

}