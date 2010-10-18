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
package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.processor.support.RelationResolver;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class AbstractRrfRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractRrfRelationResolver implements RelationResolver<Mrrel> {

	/** The coding scheme name setter. */
	private CodingSchemeIdSetter codingSchemeIdSetter;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getContainerName()
	 */
	public abstract String getContainerName();

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelation(java.lang.Object)
	 */
	public String getRelation(Mrrel item){
		return item.getRel();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSource(java.lang.Object)
	 */
	public abstract String getSource(Mrrel item);

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTarget(java.lang.Object)
	 */
	public abstract String getTarget(Mrrel item);

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceScheme(java.lang.Object)
	 */
	public String getSourceScheme(Mrrel item) {
		return item.getSab();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getRelationNamespace(java.lang.Object)
	 */
	public String getRelationNamespace(Mrrel item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getSourceNamespace(java.lang.Object)
	 */
	public String getSourceNamespace(Mrrel item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationResolver#getTargetNamespace(java.lang.Object)
	 */
	public String getTargetNamespace(Mrrel item) {
		return codingSchemeIdSetter.getCodingSchemeName();
	}

	/**
	 * Gets the coding scheme name setter.
	 * 
	 * @return the coding scheme name setter
	 */
	public CodingSchemeIdSetter getCodingSchemeIdSetter() {
		return codingSchemeIdSetter;
	}

	/**
	 * Sets the coding scheme name setter.
	 * 
	 * @param codingSchemeIdSetter the new coding scheme name setter
	 */
	public void setCodingSchemeIdSetter(
			CodingSchemeIdSetter codingSchemeIdSetter) {
		this.codingSchemeIdSetter = codingSchemeIdSetter;
	}
}