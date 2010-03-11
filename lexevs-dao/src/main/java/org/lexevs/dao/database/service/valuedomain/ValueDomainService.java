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
package org.lexevs.dao.database.service.valuedomain;

import java.util.List;

import org.LexGrid.valueDomains.ValueDomainDefinition;
import org.LexGrid.valueDomains.ValueDomains;
import org.lexevs.dao.database.service.DatabaseService;

/**
 * The Interface ValueDomainService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public interface ValueDomainService extends DatabaseService {

	/**
	 * Insert value domain definition.
	 * 
	 * @param definition the definition
	 */
	public void insertValueDomainDefinition(ValueDomainDefinition definition);
	
	/**
	 * Insert value domains.
	 * 
	 * @param valueDomains the value domains
	 * @param systemReleaseUri the system release uri
	 */
	public void insertValueDomains(ValueDomains valueDomains, String systemReleaseUri);
	
	/**
	 * Find by value uris by domain name.
	 * 
	 * @param valueDomainName the value domain name
	 * 
	 * @return the list< string>
	 */
	public List<String> findByValueUrisByDomainName(String valueDomainName);
	
	/**
	 * Gets the value domain definition by uri.
	 * 
	 * @param uri the uri
	 * 
	 * @return the value domain definition by uri
	 */
	public ValueDomainDefinition getValueDomainDefinitionByUri(String uri);
}
