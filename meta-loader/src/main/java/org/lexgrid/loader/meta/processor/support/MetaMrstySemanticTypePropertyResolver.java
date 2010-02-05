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
package org.lexgrid.loader.meta.processor.support;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.processor.support.AbstractBasicPropertyResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrsty;

/**
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaMrstySemanticTypePropertyResolver extends AbstractBasicPropertyResolver<Mrsty> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getDegreeOfFidelity(java.lang.Object)
	 */
	public String getDegreeOfFidelity(Mrsty item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getFormat(java.lang.Object)
	 */
	public String getFormat(Mrsty item) {
		return SQLTableConstants.TBLCOLVAL_FORMAT_TXT_PLAIN;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getIsActive(java.lang.Object)
	 */
	public boolean getIsActive(Mrsty item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getLanguage(java.lang.Object)
	 */
	public String getLanguage(Mrsty item) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getMatchIfNoContext(java.lang.Object)
	 */
	public boolean getMatchIfNoContext(Mrsty item) {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyName(java.lang.Object)
	 */
	public String getPropertyName(Mrsty item) {
		return RrfLoaderConstants.SEMANTIC_TYPES_PROPERTY;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyType(java.lang.Object)
	 */
	public String getPropertyType(Mrsty item) {
		return SQLTableConstants.TBLCOLVAL_PROPERTY;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getPropertyValue(java.lang.Object)
	 */
	public String getPropertyValue(Mrsty item) {
		return item.getSty();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.PropertyResolver#getRepresentationalForm(java.lang.Object)
	 */
	public String getRepresentationalForm(Mrsty item) {
		return null;
	}
}
