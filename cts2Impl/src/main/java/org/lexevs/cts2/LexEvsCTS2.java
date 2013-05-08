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
package org.lexevs.cts2;

import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Extensions.Generic.GenericExtension;
import org.lexevs.cts2.admin.AdminOperation;
import org.lexevs.cts2.author.AuthoringOperation;
import org.lexevs.cts2.query.QueryOperation;

/**
 * Main interface for LexEVS CTS2 implementation.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public interface LexEvsCTS2 extends GenericExtension{
	/**
	 * Returns LexEVS implementation of CTS2 Administration Operations.
	 * 
	 * @return AdminOperation
	 */
	public AdminOperation getAdminOperation();
	
	/**
	 * Returns LexEVS implementation of CTS Authoring Operations.
	 * 
	 * @return AuthoringOperations
	 */
	public AuthoringOperation getAuthoringOperation();
	
	/**
	 * Returns LexEVS implementation of CTS Query Operations.
	 * 
	 * @return QueryOperation
	 */
	public QueryOperation getQueryOperation();
	
	/**
	 * Returns service provider information.
	 * 
	 * @return serviceInfo
	 */
	public ServiceInfo getServiceInfo();
	
	/**
	 * Returns list of Search Algorithms supported by this LexEVS instance.
	 * 
	 * @return List of Supported Search Algorithms
	 * @throws LBException
	 */
	public ExtensionDescriptionList getSupportedSearchAlgorithms() throws LBException;
    
	/**
	 * Returns list of Loaders supported by this LexEVS instance.
	 *  
	 * @return List of supported Loaders
	 * @throws LBException
	 */
    public ExtensionDescriptionList getSupportedLoaders() throws LBException;
    
    /**
	 * Returns list of Exporters supported by this LexEVS instance.
	 *  
	 * @return List of supported Exporters
	 * @throws LBException
	 */
    public ExtensionDescriptionList getSupportedExporters() throws LBException;
    
    /**
	 * Returns list of Search Algorithm names supported by this LexEVS instance.
	 * 
	 * @return List of Supported Search Algorithm names
	 * @throws LBException
	 */
    public List<String> getSupportedSearchAlgorithmNames() throws LBException;
    
    /**
	 * Returns list of Loader names supported by this LexEVS instance.
	 *  
	 * @return List of supported Loader names
	 * @throws LBException
	 */
    public List<String> getSupportedLoaderNames() throws LBException;
    
    /**
	 * Returns list of Exporter names supported by this LexEVS instance.
	 *  
	 * @return List of supported Exporter names
	 * @throws LBException
	 */
    public List<String> getSupportedExporterNames() throws LBException;
}