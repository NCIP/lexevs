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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService;
import org.lexevs.cts2.LexEvsCTS2;


/**
 * @author m004181
 *
 */
public class CodeSystemExportOperationImpl extends BaseService implements CodeSystemExportOperation {

	private LexEvsCTS2 lexevsCts2_;
	
	public CodeSystemExportOperationImpl(LexEvsCTS2 lexevsCts2){
		this.lexevsCts2_ = lexevsCts2;
	}
	
	@Override
	public URI exportCodeSystemContent(String codeSystemNameOrURI,
			String codeSystemVersion, URI exportDestination, String exporterName) throws LBException {
		if (StringUtils.isEmpty(exporterName))
			throw new LBException("Code system exporterName is not specified. Call getSupportedCodeSystemExporterNames() to get supported exporters.");
		
		if (!getSupportedCodeSystemExporterNames().contains(exporterName))
			throw new LBException("Exporter name specified is not supported. Call getSupportedCodeSystemExporterNames() to get supported exporters.");
		
		Exporter exporter = getLexBIGServiceManager().getExporter(exporterName);
		exporter.export(Constructors.createAbsoluteCodingSchemeVersionReference(codeSystemNameOrURI, codeSystemVersion), exportDestination);
		
		if (exporter.getReferences() != null)
		{
			URI[] uris = exporter.getReferences();
			return uris[0];
		}
			
		return null;
	}

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
            
        if (exporter.getReferences() != null)
		{
			URI[] uris = exporter.getReferences();
			return uris[0];
		}
		return null;
	}

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
            
        if (exporter.getReferences() != null)
		{
			URI[] uris = exporter.getReferences();
			return uris[0];
		}
		return null;
	}

	@Override
	public CodedNodeGraph getCodeSystemCodedNodeGraph(
			String codeSystemNameOrURI, String codeSystemVersion)
			throws LBException {
		return getLexBIGService().getNodeGraph(codeSystemNameOrURI, 
				Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion), null);
	}

	@Override
	public CodedNodeSet getCodeSystemCodedNodeSet(String codeSystemNameOrURI,
			String codeSystemVersion) throws LBException {
		return getLexBIGService().getNodeSet(codeSystemNameOrURI, Constructors.createCodingSchemeVersionOrTag(null, codeSystemVersion), null);
	}
	

}
