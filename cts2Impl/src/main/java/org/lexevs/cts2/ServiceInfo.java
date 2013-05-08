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

/**
 * Class ServiceInfo provides information about the service provider.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 */
public class ServiceInfo {

	private String serviceName = "LexEVSCTS2Impl";
	private String serviceDescription = "LexEVS CTS2 Implementation";;
	private String serviceProvider = "Mayo Clinic";
	private String serviceVersion = "1.0";

	public ServiceInfo(){
	}

	public void finalize() throws Throwable {
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * @return the serviceProvider
	 */
	public String getServiceProvider() {
		return serviceProvider;
	}

	/**
	 * @return the serviceVersion
	 */
	public String getServiceVersion() {
		return serviceVersion;
	}
}