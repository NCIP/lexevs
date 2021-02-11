
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefCvfMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefCvfPropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrdef>{

	public String getQualifierName(Mrdef item) {
		return RrfLoaderConstants.CVF_QUALIFIER;
	}

	public Text getQualifierValue(Mrdef item) {
		return DaoUtility.createText(item.getCvf());
	}
}