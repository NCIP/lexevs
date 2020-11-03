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

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.data.property.MrrankUtility;
import org.lexgrid.loader.rrf.model.Mrconso;

public class MrrankPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrconso> {

	private MrrankUtility mrRankUtility;

	public String getQualifierName(Mrconso item) {
		return RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME;
	}

	public Text getQualifierValue(Mrconso item) {
		int rank = mrRankUtility.getRank(item.getSab(), item.getTty());
		return DaoUtility.createText(Integer.toString(rank));
	}
	
	public MrrankUtility getMrRankUtility() {
		return mrRankUtility;
	}

	public void setMrRankUtility(MrrankUtility mrRankUtility) {
		this.mrRankUtility = mrRankUtility;
	}
}