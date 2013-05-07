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
package org.lexgrid.loader.rxn.processor.support;

import org.lexgrid.loader.processor.support.RelationContainerResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;

/**
 * The Class RxnRelationContainerResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RxnRelationContainerResolver implements RelationContainerResolver {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.RelationContainerResolver#getRelationContainer(java.lang.Object)
	 */
	public String getRelationContainer(Object item) {
		return RrfLoaderConstants.UMLS_RELATIONS_NAME;
	}

}
