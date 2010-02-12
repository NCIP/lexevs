package org.lexgrid.loader.rrf.processor.support;

import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.data.property.MrrankUtility;
import org.lexgrid.loader.rrf.model.Mrconso;

public class MrrankPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrconso> {

	private MrrankUtility mrRankUtility;

	public String getQualifierName() {
		return RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME;
	}

	public String getQualifierValue(Mrconso item) {
		int rank = mrRankUtility.getRank(item.getSab(), item.getTty());
		return Integer.toString(rank);
	}
	
	public MrrankUtility getMrRankUtility() {
		return mrRankUtility;
	}

	public void setMrRankUtility(MrrankUtility mrRankUtility) {
		this.mrRankUtility = mrRankUtility;
	}
}
