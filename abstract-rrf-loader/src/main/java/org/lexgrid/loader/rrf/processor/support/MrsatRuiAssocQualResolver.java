package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractAssociationQualifierResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

import edu.mayo.informatics.lexgrid.convert.directConversions.mrmap.MrSat;

public class MrsatRuiAssocQualResolver extends AbstractAssociationQualifierResolver<Mrsat> {

	@Override
	public Text getQualifierValue(Mrsat item) {
		return DaoUtility.createText(item.getAtv());
	}

	@Override
	public String getQualifierName(Mrsat item) {
		return item.getAtn();
	}

}
