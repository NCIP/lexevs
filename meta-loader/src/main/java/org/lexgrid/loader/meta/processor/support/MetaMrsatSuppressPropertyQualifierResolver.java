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

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.processor.support.OptionalPropertyQualifierResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
public class MetaMrsatSuppressPropertyQualifierResolver implements
	OptionalPropertyQualifierResolver<Mrsat> {
	
	private static String NO_SUPPRESS_VALUE = "N";

	public String getQualifierName() {
		return MetaLoaderConstants.SUPPRESS_QUALIFIER;
	}

	public Text getQualifierValue(Mrsat item) {
		return DaoUtility.createText(item.getSuppress());
	}

	@Override
	public String getPropertyQualifierType(Mrsat item) {
		return null;
	}

	@Override
	public boolean toProcess(Mrsat item) {
		return !item.getSuppress().equalsIgnoreCase(NO_SUPPRESS_VALUE);
	}
}
