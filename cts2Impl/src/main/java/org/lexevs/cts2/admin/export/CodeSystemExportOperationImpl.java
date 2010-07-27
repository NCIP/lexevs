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
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService;

/**
 * Implementation CTS 2 Code System Export Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class CodeSystemExportOperationImpl extends BaseService implements CodeSystemExportOperation {
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.CodeSystemExportOperation#exportCodeSystemContent(java.lang.String, java.lang.String, java.net.URI, java.lang.String)
	 */
	@Override
	public URI exportCodeSystemContent(String codeSystemNameOrURI,
			String codeSystemVersion, URI exportDestination, String exporterName) throws LBException {
		if (StringUtils.isEmpty(exporterName))
			throw new LBException("Code system exporterName is not specified. Call getSupportedCodeSystemExporterNames() to get supported exporters.");
		
		if (!getSupportedExporterNames().contains(exporterName))
			throw new LBException("Exporter name specified is not supported. Call getSupportedCodeSystemExporterNames() to get supported exporters.");
		
		Exporter exporter = getLexBIGServiceManager().getExporter(exporterName);
		exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(codeSystemNameOrURI, codeSystemVersion), exportDestination);
		
		while (exporter.getStatus().getEndTime() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
			}
		}
		
		if (exporter.getReferences() != null)
		{
			URI[] uris = exporter.getReferences();
			return uris[0];
		}
			
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.CodeSystemExportOperation#exportCodedNodeGraph(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph, java.net.URI, boolean, boolean, boolean)
	 */
	@Override
	public URI exportCodedNodeGraph(String codeSystemNameOrURI,
			String codeSystemVersion, CodedNodeGraph cng,
			URI exportDestination, boolean overwrite, boolean stopOnErrors, boolean async) throws LBException {
		
		if (cng == null)
			throw new LBException("Coded Node Graph can not be null");
		if (StringUtils.isEmpty(codeSystemNameOrURI))
			throw new LBException("Code System Name or URI can not be null");
		
		LexGridExport exporter;
        try {
            exporter = (LexGridExport)getLexBIGService().getServiceManager(null).getExporter(LexGridExport.name);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
        
        exporter.setCng(cng);
        exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(codeSystemNameOrURI, codeSystemVersion), exportDestination, overwrite, stopOnErrors, async);
            
        while (exporter.getStatus().getEndTime() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
			}
		}
        
		return exporter.getResourceUri();
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.CodeSystemExportOperation#exportCodedNodeSet(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeSet, java.net.URI, boolean, boolean, boolean)
	 */
	@Override
	public URI exportCodedNodeSet(String codeSystemNameOrURI,
			String codeSystemVersion, CodedNodeSet cns, URI exportDestination,
			boolean overwrite, boolean stopOnErrors, boolean async) throws LBException {
		if (cns == null)
			throw new LBException("Coded Node Set can not be null");
		if (StringUtils.isEmpty(codeSystemNameOrURI))
			throw new LBException("Code System Name or URI can not be null");
		
		LexGridExport exporter;
        try {
            exporter = (LexGridExport)getLexBIGService().getServiceManager(null).getExporter(LexGridExport.name);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
        
        exporter.setCns(cns);
        exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(codeSystemNameOrURI, codeSystemVersion), exportDestination, overwrite, stopOnErrors, async);
            
        while (exporter.getStatus().getEndTime() == null) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {				
			}
		}
        
		return exporter.getResourceUri();
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.CodeSystemExportOperation#getCodeSystemCodedNodeGraph(java.lang.String, java.lang.String)
	 */
	@Override
	public CodedNodeGraph getCodeSystemCodedNodeGraph(
			String codeSystemNameOrURI, String codeSystemVersion)
			throws LBException {
		return getLexBIGService().getNodeGraph(codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.CodeSystemExportOperation#getCodeSystemCodedNodeSet(java.lang.String, java.lang.String)
	 */
	@Override
	public CodedNodeSet getCodeSystemCodedNodeSet(String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexBIGService().getNodeSet(codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion), null);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.CodeSystemExportOperation#getSupportedExporterNames()
	 */
	@Override
	public List<String> getSupportedExporterNames() throws LBException {
		return this.getLexEvsCTS2().getSupportedExporterNames();
	}
}
