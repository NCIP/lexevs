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
package org.lexgrid.valuedomain.dto;

import java.net.URI;
import java.util.List;

import org.LexGrid.commonTypes.Source;

/**
 * A reference to a value domain definition.
 * 
 * @author <A HREF="mailto:dwarkanath.sridhar@mayo.edu">Sridhar Dwarkanath</A>
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class ValueDomainDefSummary implements java.io.Serializable {

	// --------------------------/
	// - Class/Member Variables -/
	// --------------------------/

	/**
	 * The URI of the value domain
	 */
	private URI valueDomainURI_;

	private List<Source> source_;

	private List<String> representsRealmOrContext_;

	private String defaultCodingScheme_;

	private String valueDomainName_;

	public ValueDomainDefSummary() {
		super();
	}

	public URI getValueDomainURI() {
		return valueDomainURI_;
	}

	public void setValueDomainURI(URI valueDomainURI) {
		this.valueDomainURI_ = valueDomainURI;
	}

	public List<Source> getSource() {
		return source_;
	}

	public void setSource(List<Source> source) {
		this.source_ = source;
	}

	public List<String> getRepresentsRealmOrContext() {
		return representsRealmOrContext_;
	}

	public void setRepresentsRealmOrContext(
			List<String> representsRealmOrContext) {
		this.representsRealmOrContext_ = representsRealmOrContext;
	}

	public String getDefaultCodingScheme() {
		return defaultCodingScheme_;
	}

	public void setDefaultCodingScheme(String defaultCodingScheme) {
		this.defaultCodingScheme_ = defaultCodingScheme;
	}

	public String getValueDomainName() {
		return valueDomainName_;
	}

	public void setValueDomainName(String valueDomainName) {
		this.valueDomainName_ = valueDomainName;
	}

	// -----------/
	// - Methods -/
	// -----------/

}