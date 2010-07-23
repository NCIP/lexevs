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
package org.lexevs.cts2;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Extensions.ExtensionRegistry;
import org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl;
import org.lexevs.cts2.admin.AdminOperation;
import org.lexevs.cts2.admin.AdminOperationImpl;
import org.lexevs.cts2.author.AuthoringOperation;
import org.lexevs.cts2.author.AuthoringOperationImpl;
import org.lexevs.cts2.query.QueryOperation;
import org.lexevs.cts2.query.QueryOperationImpl;

/**
 * Implementation of LexEVS CTS2
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class LexEvsCTS2Impl extends LexEvsBasedService implements LexEvsCTS2 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static transient LexEvsCTS2 lexevsCTS2_;
	private transient AdminOperation adminOp_;
	private transient AuthoringOperation authOp_;
	private transient QueryOperation queryOp_;
	
	public static LexEvsCTS2 defaultInstance(){
		if (lexevsCTS2_ == null)
		{
			lexevsCTS2_ = new LexEvsCTS2Impl();
			try {
				LexEvsCTS2Impl.register();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return lexevsCTS2_;			
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.LexEvsCTS2#getAdminOperation()
	 */
	@Override
	public AdminOperation getAdminOperation() {
		if (adminOp_ == null)
			adminOp_ = new AdminOperationImpl();
		return adminOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.LexEvsCTS2#getAuthoringOperation()
	 */
	@Override
	public AuthoringOperation getAuthoringOperation() {
		if (authOp_ == null)
			authOp_ = new AuthoringOperationImpl();
		return authOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.LexEvsCTS2#getQueryOperation()
	 */
	@Override
	public QueryOperation getQueryOperation() {
		if (queryOp_ == null)
			queryOp_ = new QueryOperationImpl();
		return queryOp_;
	}

	public ExtensionDescriptionList getSupportedSearchAlgorithms() throws LBException{
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null)
			return extensionRegistry.getGenericExtensions();

		return null;
	}

	public List<String> getSupportedSearchAlgorithmNames() throws LBException{
		List<String> searchAlgNames = new ArrayList<String>();
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null && extensionRegistry.getSearchExtensions() != null)
		{
			for (ExtensionDescription ed : extensionRegistry.getSearchExtensions().getExtensionDescription())
				searchAlgNames.add(ed.getName());
		}
		return searchAlgNames;
	}

	public ExtensionDescriptionList getSupportedLoaders() throws LBException{
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null)
			return extensionRegistry.getLoadExtensions();

		return null;
	}

	public List<String> getSupportedLoaderNames() throws LBException{
		List<String> loaderNames = new ArrayList<String>();
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null && extensionRegistry.getLoadExtensions() != null)
		{
			for (ExtensionDescription ed : extensionRegistry.getLoadExtensions().getExtensionDescription())
				loaderNames.add(ed.getName());
		}
		return loaderNames;
	}

	public ExtensionDescriptionList getSupportedExporters() throws LBException{
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null)
			return extensionRegistry.getExportExtensions();

		return null;
	}

	public List<String> getSupportedExporterNames() throws LBException{
		List<String> exporterNames = new ArrayList<String>();
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null && extensionRegistry.getExportExtensions() != null)
		{
			for (ExtensionDescription ed : extensionRegistry.getExportExtensions().getExtensionDescription())
				exporterNames.add(ed.getName());
		}
		return exporterNames;
	}

	public ExtensionDescriptionList getSupportedFilters() throws LBException{
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null)
			return extensionRegistry.getFilterExtensions();

		return null;
	}

	public List<String> getSupportedFilterNames() throws LBException{
		List<String> filterNames = new ArrayList<String>();
		ExtensionRegistry extensionRegistry = getLexBIGServiceManager().getExtensionRegistry();
		if (extensionRegistry != null && extensionRegistry.getFilterExtensions() != null)
		{
			for (ExtensionDescription ed : extensionRegistry.getFilterExtensions().getExtensionDescription())
				filterNames.add(ed.getName());
		}
		return filterNames;
	}

	@Override
	public ServiceInfo getServiceInfo() {
		return new ServiceInfo();
	}

	@Override
	public String getDescription() {
		return getServiceInfo().getServiceDescription();
	}

	@Override
	public String getName() {
		return getServiceInfo().getServiceName();
	}

	@Override
	public String getProvider() {
		return getServiceInfo().getServiceProvider();
	}

	@Override
	public String getVersion() {
		return getServiceInfo().getServiceVersion();
	}

	public static void register() throws LBParameterException, LBException {
		ExtensionDescription temp = new ExtensionDescription();
		temp.setExtensionBaseClass(LexEvsCTS2Impl.class.getInterfaces()[0].getName());
		temp.setExtensionClass(LexEvsCTS2Impl.class.getName());
		ServiceInfo serviceInfo = new ServiceInfo();
		temp.setDescription(serviceInfo.getServiceDescription());
		temp.setName(serviceInfo.getServiceName());
		temp.setVersion(serviceInfo.getServiceVersion());

		// Registered here as part of the impl to avoid the LexBig service
		// manager API. If writing an add-on extension, registration should be
		// performed through the proper interface.
		ExtensionRegistryImpl.instance().registerGenericExtension(temp);
	}

	public static void main(String[] args){
		LexEvsCTS2Impl cts2 = new LexEvsCTS2Impl();
		System.out.println(cts2.getServiceInfo().getServiceName());
		System.out.println(cts2.getServiceInfo().getServiceProvider());
		System.out.println(cts2.getServiceInfo().getServiceDescription());
		System.out.println(cts2.getServiceInfo().getServiceVersion());

		try {
			ExtensionDescriptionList loaders = cts2.getSupportedLoaders();
			if (loaders != null)
			{
				for (ExtensionDescription loader : loaders.getExtensionDescription())
				{
					System.out.println("loader getExtensionBaseClass : " + loader.getExtensionBaseClass());
					System.out.println("loader getExtensionClass : " + loader.getExtensionClass());
					System.out.println("loader getName : " + loader.getName());
					System.out.println("--------------------------");
				}
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ExtensionDescriptionList exporters = cts2.getSupportedExporters();
			if (exporters != null)
			{
				for (ExtensionDescription exporter : exporters.getExtensionDescription())
				{
					System.out.println("exporter getExtensionBaseClass : " + exporter.getExtensionBaseClass());
					System.out.println("exporter getExtensionClass : " + exporter.getExtensionClass());
					System.out.println("exporter getName : " + exporter.getName());
					System.out.println("--------------------------");
				}
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			ExtensionDescriptionList exporters = cts2.getLexBIGServiceManager().getExtensionRegistry().getGenericExtensions();
			if (exporters != null)
			{
				for (ExtensionDescription exporter : exporters.getExtensionDescription())
				{
					System.out.println("generic getExtensionBaseClass : " + exporter.getExtensionBaseClass());
					System.out.println("generic getExtensionClass : " + exporter.getExtensionClass());
					System.out.println("generic getName : " + exporter.getName());
					System.out.println("--------------------------");
				}
			}
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	
}
