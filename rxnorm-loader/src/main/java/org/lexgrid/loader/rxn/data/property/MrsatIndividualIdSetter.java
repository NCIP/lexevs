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
package org.lexgrid.loader.rxn.data.property;

import org.apache.commons.lang.StringUtils;
import org.lexgrid.loader.data.property.RandomGuidIndividualIdSetter;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrsatIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatIndividualIdSetter extends RandomGuidIndividualIdSetter<Mrsat> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Mrsat item){
		if (StringUtils.isNotBlank(item.getAtui())) {
		   return item.getAtui();
		} else {
			return super.addId(item);
		}
	}

}
