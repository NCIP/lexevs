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
package org.lexgrid.loader.meta.data.codingscheme;

import java.util.Properties;
import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;

import org.lexgrid.loader.constants.LoaderConstants;

/**
 * The Class MetaCodingSchemeIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaCodingSchemeIdSetter implements CodingSchemeIdSetter {

	/** The coding scheme properties. */
	private Properties codingSchemeProperties;
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter#getCodingSchemeName()
	 */
	public String getCodingSchemeName() {
		return codingSchemeProperties.getProperty(LoaderConstants.CODING_SCHEME_NAME_PROPERTY);
	}

	public String getCodingSchemeUri() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}

	public String getCodingSchemeVersion() {
		// TODO Auto-generated method stub (IMPLEMENT!)
		throw new UnsupportedOperationException();
	}
	
	/**
	 * Gets the coding scheme properties.
	 * 
	 * @return the coding scheme properties
	 */
	public Properties getCodingSchemeProperties() {
		return codingSchemeProperties;
	}

	/**
	 * Sets the coding scheme properties.
	 * 
	 * @param codingSchemeProperties the new coding scheme properties
	 */
	public void setCodingSchemeProperties(Properties codingSchemeProperties) {
		this.codingSchemeProperties = codingSchemeProperties;
	}
}
