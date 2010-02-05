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
package org.lexgrid.loader.umls.hardcodedvalues;

import java.util.List;

import org.LexGrid.persistence.dao.LexEvsDao;
import org.LexGrid.persistence.model.CodingScheme;
import org.LexGrid.persistence.model.CodingSchemeMultiAttrib;
import org.LexGrid.persistence.model.CodingSchemeMultiAttribId;
import org.LexGrid.persistence.model.Relation;
import org.LexGrid.persistence.model.RelationId;
import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.hardcodedvalues.AbstractHardcodedValuesFactory;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.umls.constants.UmlsConstants;

/**
 * A factory for creating UmlsHardcodedValues objects.
 */
public class UmlsHardcodedValuesFactory extends AbstractHardcodedValuesFactory {

	/** The coding scheme name setter. */
	private CodingSchemeNameSetter codingSchemeNameSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.hardcodedvalues.AbstractHardcodedValuesFactory#buildList(java.util.List)
	 */
	@Override
	public List buildList(List hardcodedValues) {
		
		Relation relation = new Relation();
		RelationId relationId = new RelationId();
		relationId.setCodingSchemeName(codingSchemeNameSetter.getCodingSchemeName());
		relationId.setContainerName(RrfLoaderConstants.UMLS_RELATIONS_NAME);
		relation.setEntityDescription(RrfLoaderConstants.UMLS_RELATIONS_ENTITY_DESCRIPTION);
		relation.setIsNative(true);
		relation.setId(relationId);
		hardcodedValues.add(relation);		
		
		return hardcodedValues;	
	}
	

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeNameSetter getCodingSchemeNameSetter() {
		return codingSchemeNameSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeNameSetter the new coding scheme name setter
	 */
	public void setCodingSchemeNameSetter(
			CodingSchemeNameSetter codingSchemeNameSetter) {
		this.codingSchemeNameSetter = codingSchemeNameSetter;
	}
}
