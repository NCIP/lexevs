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
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.processor.support.AbstractBasicMultiAttribResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefCvfMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefCvfMultiAttribResolver extends AbstractBasicMultiAttribResolver<Mrdef>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getAttributeValue(java.lang.Object)
	 */
	public String getAttributeValue(Mrdef item) {
		return RrfLoaderConstants.CVF_QUALIFIER;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getTypeName()
	 */
	public String getTypeName() {
		return SQLTableConstants.TBLCOLVAL_QUALIFIER;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getVal1(java.lang.Object)
	 */
	public String getVal1(Mrdef item) {
		return item.getCvf();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.MultiAttribResolver#getVal2(java.lang.Object)
	 */
	public String getVal2(Mrdef item) {
		return null;
	}
}
