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

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Impl.exporters.LexGridExport;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.lexevs.cts2.BaseService;

/**
 * The Class AssociationExportOperationImpl.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AssociationExportOperationImpl extends BaseService implements AssociationExportOperation {

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.export.AssociationExportOperation#exportAssociation(java.lang.String, java.lang.String, org.LexGrid.LexBIG.LexBIGService.CodedNodeGraph, java.net.URI, boolean, boolean, boolean)
	 */
	@Override
	public void exportAssociation(
			String codeSystemNameOrURI,
			String codeSystemVersion, 
			CodedNodeGraph cng,
			URI exportDestination, 
			boolean overwrite, 
			boolean stopOnErrors,
			boolean async) throws LBException {
		
		LexGridExport exporter;
        try {
            exporter = (LexGridExport)getLexBIGService().getServiceManager(null).getExporter(LexGridExport.name);
        } catch (LBException e) {
            throw new RuntimeException(e);
        }
        
        exporter.setCng(cng);
        
        exporter.export(
        		Constructors.createAbsoluteCodingSchemeVersionReference(codeSystemNameOrURI, codeSystemVersion), 
        		exportDestination, 
        		overwrite, 
        		stopOnErrors, 
        		async);
	}
}