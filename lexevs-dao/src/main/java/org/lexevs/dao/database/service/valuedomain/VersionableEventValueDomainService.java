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
import org.lexevs.dao.database.service.AbstractDatabaseService;

/**
 * The Class VersionableEventValueDomainService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class VersionableEventValueDomainService extends AbstractDatabaseService implements ValueDomainService{

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuedomain.ValueDomainService#findByValueUrisByDomainName(java.lang.String)
	 */
	@Override
	public List<String> findByValueUrisByDomainName(String valueDomainName) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuedomain.ValueDomainService#getValueDomainDefinitionByUri(java.lang.String)
	 */
	@Override
	public ValueDomainDefinition getValueDomainDefinitionByUri(String uri) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuedomain.ValueDomainService#insertValueDomainDefinition(org.LexGrid.valueDomains.ValueDomainDefinition)
	 */
	@Override
	public void insertValueDomainDefinition(ValueDomainDefinition definition) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see org.lexevs.dao.database.service.valuedomain.ValueDomainService#insertValueDomains(org.LexGrid.valueDomains.ValueDomains, java.lang.String)
	 */
	@Override
	public void insertValueDomains(ValueDomains valueDomains,
			String systemReleaseUri) {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	
}
