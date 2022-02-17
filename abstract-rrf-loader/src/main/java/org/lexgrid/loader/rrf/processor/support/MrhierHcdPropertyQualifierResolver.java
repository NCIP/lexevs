
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrhier;

/**
 * The Class MrdefAuiMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrhierHcdPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrhier>{


	public String getQualifierName(Mrhier item) {
		return RrfLoaderConstants.HCD_QUALIFIER;
	}

	public Text getQualifierValue(Mrhier item) {
		return DaoUtility.createText(item.getHcd() + ":" + item.getPtr());
	}
}