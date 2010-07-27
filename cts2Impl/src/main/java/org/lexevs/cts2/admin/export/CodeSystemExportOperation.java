/*
 * Copyright: (c) 2004-2009 Mayo Foundation for Medical Education and Research
 * (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the triple-shield Mayo
 * logo are trademarks and service marks of MFMER.
 * 
 * Except as contained in the copyright notice above, or as used to identify
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 * 
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.lexevs.cts2.admin.export;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;

/**
 * CTS 2 Code System Export Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface CodeSystemExportOperation {
	/**
	 * Exports contents of the code system using the exporter specified.
	 * @param codeSystemNameOrURI URI of the Code System to be exported
	 * @param codeSystemVersion Version of the Code System to be exported
	 * @param exportDestination Destination path information for the exported file.
	 * @param exporter Name of the exporter to use. Use getSupportedExporterNames to get all the exporters supported by this instance of CTS2. 
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportCodeSystemContent(String codeSystemNameOrURI, String codeSystemVersion, 
			URI exportDestination, String exporter) throws LBException;
	
	/**
	 * Returns CodedNodeSet(CNS) for a Code System Version. This CNS can be used to apply further restrictions before exporting.
	 *   
	 * @param codeSystemNameOrURI URI of the Code System to be exported
	 * @param codeSystemVersion Version of the Code System to be exported
	 * @return CodedNodeSet of the Code System Version
	 * @throws LBException
	 */
	public CodedNodeSet getCodeSystemCodedNodeSet(String codeSystemNameOrURI, String codeSystemVersion) 
		throws LBException;
	
	/**
	 * Returns CodedNodeGraph(CNG) for a Code System Version. This CNG can be used to apply further restrictions before exporting.
	 * 
	 * @param codeSystemNameOrURI URI of the Code System to be exported
	 * @param codeSystemVersion Version of the Code System to be exported
	 * @return CodedNodeGraph of the Code System Version
	 * @throws LBException
	 */
	public CodedNodeGraph getCodeSystemCodedNodeGraph(String codeSystemNameOrURI, String codeSystemVersion) 
		throws LBException;
	
	/**
	 * Resolves the given CodedNodeSet(CNS) and exports the contents.
	 * 
	 * @param codeSystemNameOrURI URI of the Code System to be used for resolving the CNS
	 * @param codeSystemVersion Version of the Code System to be used for resolving the CNS
	 * @param cns Coded Node Set
	 * @param exportDestination Destination for the exported file
	 * @param overwrite True means, any existing file will be overwritten
	 * @param stopOnErrors True means stop if any export error is detected. False means
	 *            attempt to export what can be exported if recoverable errors are
	 *            encountered.
	 * @param async Flag controlling whether export occurs in the calling thread.  
	 *            If true, the export will occur in a separate asynchronous process.
	 *            If false, this method blocks until the export operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportCodedNodeSet(String codeSystemNameOrURI,
			String codeSystemVersion, CodedNodeSet cns, URI exportDestination, boolean overwrite, 
			boolean stopOnErrors, boolean async) throws LBException;
	
	/**
	 * Resolves the given CodedNodeGraph(CNG) and exports the contents.
	 * 
	 * @param codeSystemNameOrURI URI of the Code System to be used for resolving the CNG
	 * @param codeSystemVersion Version of the Code System to be used for resolving the CNG
	 * @param cng Coded Node Graph
	 * @param exportDestination Destination for the exported file
	 * @param overwrite True means, any existing file will be overwritten
	 * @param stopOnErrors True means stop if any export error is detected. False means
	 *            attempt to export what can be exported if recoverable errors are
	 *            encountered.
	 * @param async Flag controlling whether export occurs in the calling thread.  
	 *            If true, the export will occur in a separate asynchronous process.
	 *            If false, this method blocks until the export operation
	 *            completes or fails. Regardless of setting, the getStatus and
	 *            getLog calls are used to fetch results.
	 * @return URI of destination if successfully exported.
	 * @throws LBException
	 */
	public URI exportCodedNodeGraph(String codeSystemNameOrURI,
			String codeSystemVersion, CodedNodeGraph cng, URI exportDestination, boolean overwrite, 
			boolean stopOnErrors, boolean async) throws LBException;
	
	/**
	 * Returns list of Exporter names supported by this LexEVS instance.
	 *  
	 * @return List of supported Exporter names
	 * @throws LBException
	 */
	public List<String> getSupportedExporterNames() throws LBException;
	
}
