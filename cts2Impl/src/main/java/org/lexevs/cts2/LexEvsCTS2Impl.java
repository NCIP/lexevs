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

import org.LexGrid.LexBIG.DataModel.Collections.ExtensionDescriptionList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.ExtensionDescription;
import org.LexGrid.LexBIG.Exceptions.LBException;
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
public class LexEvsCTS2Impl extends BaseService implements LexEvsCTS2 {
	private AdminOperation adminOp_;
	private AuthoringOperation authOp_;
	private QueryOperation queryOp_;
	private static LexEvsCTS2 lexevsCTS2_ = null;
	
	public static LexEvsCTS2 defaultInstance(){
		if (lexevsCTS2_ == null)
			lexevsCTS2_ = new LexEvsCTS2Impl();
		
		return lexevsCTS2_;			
	}
	
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.LexEvsCTS2#getAdminOperation()
	 */
	@Override
	public AdminOperation getAdminOperation() {
		if (adminOp_ == null)
			adminOp_ = new AdminOperationImpl(lexevsCTS2_);
		return adminOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.LexEvsCTS2#getAuthoringOperation()
	 */
	@Override
	public AuthoringOperation getAuthoringOperation() {
		if (authOp_ == null)
			authOp_ = new AuthoringOperationImpl(lexevsCTS2_);
		return authOp_;
	}

	/* (non-Javadoc)
	 * @see org.lexevs.cts2.LexEvsCTS2#getQueryOperation()
	 */
	@Override
	public QueryOperation getQueryOperation() {
		if (queryOp_ == null)
			queryOp_ = new QueryOperationImpl(lexevsCTS2_);
		return queryOp_;
	}
	
	@Override
	public ServiceInfo getServiceInfo() {
		return new ServiceInfo();
	}
	
	public static void main(String[] args){
		LexEvsCTS2 cts2 = new LexEvsCTS2Impl();
		System.out.println(cts2.getServiceInfo().getServiceName());
		System.out.println(cts2.getServiceInfo().getServiceProvider());
		System.out.println(cts2.getServiceInfo().getServiceDescription());
		System.out.println(cts2.getServiceInfo().getServiceVersion());
		
		try {
			ExtensionDescriptionList loaders = cts2.getSupportedCodeSystemLoaders();
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
			ExtensionDescriptionList exporters = cts2.getSupportedCodeSystemExporters();
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
		
	}	
}
