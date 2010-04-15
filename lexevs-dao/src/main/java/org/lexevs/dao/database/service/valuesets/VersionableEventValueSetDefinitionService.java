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
package org.lexevs.dao.database.service.valuesets;

import java.net.URI;
import java.util.List;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.naming.Mappings;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.lexevs.dao.database.access.valuesets.ValueSetDefinitionDao;
import org.lexevs.dao.database.service.AbstractDatabaseService;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class VersionableEventValueSetDefinitionService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventValueSetDefinitionService extends AbstractDatabaseService implements ValueSetDefinitionService{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionURISForName(java.lang.String)
	 */
	@Override
	public List<String> getValueSetDefinitionURISForName(String valueSetDefinitionName) throws LBException {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionURIsForName(valueSetDefinitionName);
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#getValueSetDefinitionByUri(java.net.URI)
	 */
	@Override
	public ValueSetDefinition getValueSetDefinitionByUri(URI uri) {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionByURI(uri.toString());
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#insertValueSetDefinition(org.LexGrid.valuesets.ValueSetDefinition, java.lang.String)
	 */
	@Override
	@Transactional
	public void insertValueSetDefinition(ValueSetDefinition definition, String systemReleaseUri) throws LBException {
		insertValueSetDefinition(definition, systemReleaseUri, null);
	}
	
	@Override
	public void insertValueSetDefinition(ValueSetDefinition definition,
			String systemReleaseUri, Mappings mappings) throws LBException {
		
		ValueSetDefinitionDao vsdDao = this.getDaoManager().getCurrentValueSetDefinitionDao();
		
		vsdDao.insertValueSetDefinition(systemReleaseUri, definition, mappings);
	}
	

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuesets.ValueSetDefinitionService#insertValueDomains(org.LexGrid.valueDomains.ValueDomains, java.lang.String)
	 */
	@Override
	public void insertValueSetDefinitions(ValueSetDefinitions valueSetDefinitions,
			String systemReleaseUri) throws LBException {
		
		for (ValueSetDefinition vsd : valueSetDefinitions.getValueSetDefinitionAsReference())
		{
			insertValueSetDefinition(vsd, systemReleaseUri);
		}
	}

	@Override
	public List<String> listValueSetDefinitionURIs() {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getValueSetDefinitionURIs();
	}
	


	@Override
	public List<String> getAllValueSetDefinitionsWithNoName()
			throws LBException {
		return this.getDaoManager().getCurrentValueSetDefinitionDao().getAllValueSetDefinitionsWithNoName();
	}

	@Override
	public void removeValueSetDefinition(String valueSetDefinitionURI) {
		this.getDaoManager().getCurrentValueSetDefinitionDao().removeValueSetDefinitionByValueSetDefinitionURI(valueSetDefinitionURI);
	}

	
}
