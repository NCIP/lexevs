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
package org.lexgrid.loader.meta.hardcodedvalues;

import org.LexGrid.relations.Relations;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * A factory for creating MetaHardcodedValues objects.
 */
public class MetaHardcodedValuesTasklet extends AbstractIntrospectiveHardcodedValues {
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractHardcodedValuesFactory#buildList(java.util.List)
	 */
	@Override
	public void loadObjects() {
		Relations relation = new Relations();

		relation.setContainerName(RrfLoaderConstants.UMLS_RELATIONS_NAME);

		this.getSupportedAttributeTemplate().addSupportedContainerName(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				RrfLoaderConstants.UMLS_RELATIONS_NAME, 
				null, 
				RrfLoaderConstants.UMLS_RELATIONS_NAME);	
		
		this.getSupportedAttributeTemplate().addSupportedNamespace(
				getCodingSchemeIdSetter().getCodingSchemeUri(), 
				getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				getCodingSchemeIdSetter().getCodingSchemeName(), 
				null, 
				getCodingSchemeIdSetter().getCodingSchemeName(), 
				getCodingSchemeIdSetter().getCodingSchemeName());
		
		this.getDatabaseServiceManager().
		getRelationService().
		insertRelation(
				this.getCodingSchemeIdSetter().getCodingSchemeUri(), 
				this.getCodingSchemeIdSetter().getCodingSchemeVersion(), 
				relation);
	}
}