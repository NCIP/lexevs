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
package org.lexgrid.loader.meta.hardcodedvalues;

import java.util.ArrayList;
import java.util.List;

import javax.management.relation.Relation;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.hardcodedvalues.AbstractIntrospectiveHardcodedValues;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * A factory for creating MetaHardcodedValues objects.
 */
public class MetaHardcodedValuesTasklet extends AbstractIntrospectiveHardcodedValues {

	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractHardcodedValuesFactory#buildList(java.util.List)
	 */
	@Override
	public void loadObjects() {
		/*
		List<Object> hardcodedValues = new ArrayList<Object>();
		
		Relation relation = new Relation();
		RelationId relationId = new RelationId();
		relationId.setCodingSchemeName(codingSchemeIdSetter.getCodingSchemeName());
		relationId.setContainerName(RrfLoaderConstants.UMLS_RELATIONS_NAME);
		relation.setEntityDescription(RrfLoaderConstants.UMLS_RELATIONS_ENTITY_DESCRIPTION);
		relation.setIsNative(true);
		relation.setId(relationId);
		hardcodedValues.add(relation);
		
		this.getSupportedAttributeTemplate()
			.addSupportedNamespace(codingSchemeIdSetter.getCodingSchemeName(), 
					codingSchemeIdSetter.getCodingSchemeName(), 
					null, 
					codingSchemeIdSetter.getCodingSchemeName(), 
					codingSchemeIdSetter.getCodingSchemeName());
		
		return hardcodedValues;	
		*/
	}

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeNameSetter() {
		return codingSchemeIdSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeIdSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}
