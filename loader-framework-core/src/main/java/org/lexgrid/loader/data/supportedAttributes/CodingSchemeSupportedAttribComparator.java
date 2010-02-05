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
package org.lexgrid.loader.data.supportedAttributes;

import java.util.Comparator;

import org.LexGrid.persistence.model.CodingSchemeSupportedAttrib;

/**
 * The Class CodingSchemeSupportedAttribComparator.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class CodingSchemeSupportedAttribComparator implements Comparator<CodingSchemeSupportedAttrib>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(CodingSchemeSupportedAttrib o1,
			CodingSchemeSupportedAttrib o2) {
		if(o1.getId().getCodingSchemeName().equals(o2.getId().getCodingSchemeName()) &&
				o1.getId().getCodingSchemeName().equals(o2.getId().getCodingSchemeName()) &&
				o1.getId().getId().equals(o2.getId().getId()) &&
				o1.getId().getIdValue().equals(o2.getId().getIdValue()) &&
				o1.getId().getSupportedAttributeTag().equals(o2.getId().getSupportedAttributeTag()) &&
				o1.getId().getVal1().equals(o2.getId().getVal1()) &&
				o1.getUri().equals(o2.getUri()) &&
				o1.getVal2().equals(o2.getVal2())) {
			return 0;
		} else {
			return -1;
		}	
	}
}
