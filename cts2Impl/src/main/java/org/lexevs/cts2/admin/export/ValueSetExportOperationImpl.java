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
package org.lexevs.cts2.admin.export;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Export.Exporter;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.BaseService;
import org.lexevs.cts2.LexEvsCTS2Impl;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

/**
 * Implementation of CTS 2 Value Set Export Operation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ValueSetExportOperationImpl extends BaseService implements ValueSetExportOperation{

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.ValueSetExportOperation#exportValueSetDefinition(java.net.URI, java.lang.String, java.lang.String, boolean, boolean)
	 */
	@Override
	public void exportValueSetDefinition(URI valueSetDefinitionURI, String valueSetDefinitionVersion, String xmlFullPathName, boolean overwrite, boolean failOnAllErrors) throws LBException {
		LexEVSValueSetDefinitionServices vsdServ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		vsdServ.exportValueSetDefinition(valueSetDefinitionURI, valueSetDefinitionVersion, xmlFullPathName, overwrite, failOnAllErrors);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.ValueSetExportOperation#exportValueSetContents(java.net.URI, java.lang.String, java.net.URI, java.lang.String)
	 */
	@Override
	public URI exportValueSetContents(URI valueSetDefinitionURI, String valueSetDefinitionVersion, URI exportDestination, String exporterName)
			throws LBException {
		if (StringUtils.isEmpty(exporterName))
			throw new LBException("Value Set exporterName is not specified. Call getExporterNames() to get supported exporters.");
		
		if (!getSupportedExporterNames().contains(exporterName))
			throw new LBException("Exporter name specified is not supported. Call getSystemExporterNames() to get supported exporters.");
		
		Exporter exporter = getLexBIGServiceManager().getExporter(exporterName);
		exporter.exportValueSetDefinition(valueSetDefinitionURI, valueSetDefinitionVersion, exportDestination);
		
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
	 * @see org.lexevs.cts2.admin.export.ValueSetExportOperation#exportValueSetContents(java.net.URI, java.lang.String, java.net.URI, org.LexGrid.LexBIG.DataModel.Collections.AbsoluteCodingSchemeVersionReferenceList, java.lang.String, boolean, boolean)
	 */
	@Override
	public URI exportValueSetContents(URI valueSetDefinitionURI, String valueSetDefinitionVersion, 
			URI exportDestination, AbsoluteCodingSchemeVersionReferenceList csVersionList,
            String csVersionTag, boolean overwrite, boolean failOnAllErrors) throws LBException {
		LexEVSValueSetDefinitionServices vsdServ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		return vsdServ.exportValueSetResolution(valueSetDefinitionURI, valueSetDefinitionVersion, exportDestination, csVersionList, csVersionTag, overwrite, failOnAllErrors);
	}

	/*
	 * (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.ValueSetExportOperation#getSupportedExporterNames()
	 */
	@Override
	public List<String> getSupportedExporterNames() throws LBException {
		return this.getLexEvsCTS2().getSupportedExporterNames();
	}
}