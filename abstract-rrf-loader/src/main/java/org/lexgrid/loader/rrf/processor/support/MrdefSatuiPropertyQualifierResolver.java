
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefSatuiMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefSatuiPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrdef>{

	public String getQualifierName(Mrdef item) {
		return RrfLoaderConstants.SATUI_QUALIFIER;
	}

	public Text getQualifierValue(Mrdef item) {
		return DaoUtility.createText(item.getSatui());
	}

}