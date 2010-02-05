package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.util.sql.lgTables.SQLTableConstants;
import org.lexgrid.loader.processor.support.AbstractBasicMultiAttribResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.data.property.MrrankUtility;
import org.lexgrid.loader.rrf.model.Mrconso;

public class MrrankMultiattributeResolver extends AbstractBasicMultiAttribResolver<Mrconso> {

	private MrrankUtility mrRankUtility;
	
	public String getAttributeValue(Mrconso item) {
		return RrfLoaderConstants.MRRANK_PROPERTY_QUALIFIER_NAME;
	}

	public String getTypeName() {
		return SQLTableConstants.TBLCOLVAL_QUALIFIER;
	}

	public String getVal1(Mrconso item) {
		int rank = mrRankUtility.getRank(item.getSab(), item.getTty());
		return Integer.toString(rank);
	}

	public String getVal2(Mrconso item) {
		return null;
	}

	public MrrankUtility getMrRankUtility() {
		return mrRankUtility;
	}

	public void setMrRankUtility(MrrankUtility mrRankUtility) {
		this.mrRankUtility = mrRankUtility;
	}
}
