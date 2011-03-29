/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
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
package org.cts2.internal.model.uri.factory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.valueSets.ValueSetDefinition;
import org.LexGrid.valueSets.ValueSetDefinitions;
import org.cts2.internal.model.uri.DefaultValueSetDefinitionDirectoryURI;
import org.cts2.internal.model.uri.restrict.IterableBasedResolvingRestrictionHandler;
import org.cts2.uri.ValueSetDefinitionDirectoryURI;

/**
 * A factory for creating ValueSetDefinitionDirectoryURIFactory objects.
 * 
 * @author <a href="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</a>
 */
public class ValueSetDefinitionDirectoryURIFactory extends AbstractCompositeDirectoryURIFactory<ValueSetDefinitionDirectoryURI> {
	
	private IterableBasedResolvingRestrictionHandler<ValueSetDefinition,ValueSetDefinitionDirectoryURI> restrictionHandler;

	/* (non-Javadoc)
	 * @see org.cts2.internal.uri.factory.AbstractDirectoryURIFactory#doGetDirectoryURI()
	 */
	@Override
	protected ValueSetDefinitionDirectoryURI doBuildDirectoryURI() {
		Assert.assertNotNull(this.restrictionHandler);
		
		ValueSetDefinitions valueSetDefinitions = new ValueSetDefinitions();
		
		try {
			List<String> valueSetURIs = this.getLexEVSValueSetDefinitionService().listValueSetDefinitionURIs();
			
			for (String vsdURI : valueSetURIs)
			{
				valueSetDefinitions.addValueSetDefinition(this.getLexEVSValueSetDefinitionService().getValueSetDefinition(new URI(vsdURI), null));
			}
		} catch (LBInvocationException e) {
			throw new RuntimeException(e);
		} catch (LBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new DefaultValueSetDefinitionDirectoryURI(valueSetDefinitions, this.restrictionHandler, this.getBeanMapper());
	}

	public IterableBasedResolvingRestrictionHandler<ValueSetDefinition, ValueSetDefinitionDirectoryURI> getRestrictionHandler() {
		return restrictionHandler;
	}

	public void setRestrictionHandler(
			IterableBasedResolvingRestrictionHandler<ValueSetDefinition, ValueSetDefinitionDirectoryURI> restrictionHandler) {
		this.restrictionHandler = restrictionHandler;
	}
}
