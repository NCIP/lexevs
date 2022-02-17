
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MrconsoLuiMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoLuiPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrconso>{

	public String getQualifierName(Mrconso item) {
		return RrfLoaderConstants.LUI_QUALIFIER;
	}

	public Text getQualifierValue(Mrconso item) {
		return DaoUtility.createText(item.getLui());
	}

}