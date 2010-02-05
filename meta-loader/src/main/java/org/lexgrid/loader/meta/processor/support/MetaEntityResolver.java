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

import org.lexgrid.loader.processor.support.AbstractBasicEntityResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MetaEntityResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaEntityResolver extends AbstractBasicEntityResolver<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getIsActive(java.lang.Object)
	 */
	public boolean getIsActive(Mrconso item) {	
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getIsAnonymous(java.lang.Object)
	 */
	public boolean getIsAnonymous(Mrconso item) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getIsDefined(java.lang.Object)
	 */
	public boolean getIsDefined(Mrconso item) {
		return true;
	}
}
