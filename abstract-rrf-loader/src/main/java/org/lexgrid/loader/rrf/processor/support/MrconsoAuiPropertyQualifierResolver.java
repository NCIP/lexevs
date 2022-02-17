
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MrconsoAuiMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoAuiPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrconso>{

	public String getQualifierName(Mrconso item) {
		return RrfLoaderConstants.AUI_QUALIFIER;
	}

	public Text getQualifierValue(Mrconso item) {
		return DaoUtility.createText(item.getAui());
	}

}