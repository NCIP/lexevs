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
package org.LexGrid.LexBIG.GridInterface.TerminologyServiceInterface;

import gov.nih.nci.cagrid.data.client.DataServiceDiscoveryClient;

import java.net.URI;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;

/**
 * Client interface for discoverying terminology services
 * 
 * @author solbrigcvs
 * @version 1.0
 * @created 09-Feb-2006 10:22:07 PM
 */
public abstract class TerminologyDiscoveryClient extends
		DataServiceDiscoveryClient {

	public TerminologyDiscoveryClient() {

	}

	public void finalize() throws Throwable {
		super.finalize();
	}

	/**
	 * Return a list of all codings schemes that are recorded in the registry,
	 * and the service or services that represent them.
	 */
	public CodingSchemeRenderingList getAllTerminologyServices() {
		return null;
	}

	/**
	 * Return a list of all services that represent the supplied codingSchemeURN
	 * and (optional) version.
	 * 
	 * @param codingSchemeURNorName
	 * @param codingSchemeVersion
	 */
	public CodingSchemeRenderingList discoverTerminologyServicesByURN(
			String codingSchemeURNorName,
			CodingSchemeVersionOrTag codingSchemeVersion) {
		return null;
	}

	/**
	 * Discover services whose name, description or other property contains the
	 * supplied search string.
	 * 
	 * @param searchString
	 *            Search string
	 */
	public CodingSchemeRenderingList discoverTerminologyServicesBySearchString(
			String searchString) {
		return null;
	}

	/**
	 * 
	 * @param serviceHandle
	 *            The handle of the service being queried
	 */
	public CodingSchemeRenderingList discoverCodingSchemesByService(
			URI serviceHandle) {
		return null;
	}

}