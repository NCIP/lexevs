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
package org.lexevs.cts2.admin.load;

import java.net.URI;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.valueSets.PickListDefinition;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.apache.commons.lang.StringUtils;
import org.lexevs.cts2.LexEvsCTS2;
import org.lexgrid.valuesets.LexEVSPickListDefinitionServices;
import org.lexgrid.valuesets.LexEVSValueSetDefinitionServices;
import org.lexgrid.valuesets.impl.LexEVSPickListDefinitionServicesImpl;
import org.lexgrid.valuesets.impl.LexEVSValueSetDefinitionServicesImpl;

import edu.mayo.informatics.lexgrid.convert.utility.URNVersionPair;

/**
 * @author m004181
 *
 */
public class ValueSetLoadOperationImpl implements ValueSetLoadOperation {
	
	private LexEvsCTS2 lexEvsCts2_;
    private LexEVSValueSetDefinitionServices vsdService_;
    private LexEVSPickListDefinitionServices pldService_;
    
    public ValueSetLoadOperationImpl(LexEvsCTS2 lexEvsCts2){
    	this.lexEvsCts2_ = lexEvsCts2;
    }
    
	/* (non-Javadoc)
	 * @see org.lexevs.cts2.admin.load.ValueSetLoadOperation#load(java.net.URI, java.net.URI, java.lang.String, java.lang.Boolean)
	 */
	@Override
	public URNVersionPair[] load(URI source, URI releaseURI, String loaderName,
			Boolean stopOnErrors) throws LBException {
		return lexEvsCts2_.getAdminOperation().getCodeSystemLoadOperation().load(source, null, null, releaseURI, loaderName, stopOnErrors, true, false, null, null);
	}

	@Override
	public String load(ValueSetDefinition valueSetDefinition, URI releaseURI, Boolean stopOnErrors) throws LBException {
		if (valueSetDefinition == null)
			throw new LBException("Value Set Definition object can not be empty");
		
		if (StringUtils.isEmpty(valueSetDefinition.getValueSetDefinitionURI()))
			throw new LBException("Value Set Definition URI can not be empty");
		
		vsdService_ = LexEVSValueSetDefinitionServicesImpl.defaultInstance();
		vsdService_.loadValueSetDefinition(valueSetDefinition, releaseURI == null ? null : releaseURI.toString(), null);
		return valueSetDefinition.getValueSetDefinitionURI();
	}

	@Override
	public String load(PickListDefinition pickListDefinition, URI releaseURI, Boolean stopOnErrors) throws LBException {
		if (pickListDefinition == null)
			throw new LBException("Pick List Definition object can not be empty");
		
		if (pickListDefinition.getPickListId() == null)
			throw new LBException("Pick List Definition ID can not be empty");
		
		pldService_ = LexEVSPickListDefinitionServicesImpl.defaultInstance();
		pldService_.loadPickList(pickListDefinition, releaseURI == null ? null : releaseURI.toString(), null);
		return pickListDefinition.getPickListId();
	}

}
