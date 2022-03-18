
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.QualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrhier;

/**
 * The Class HcdQualifierResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class HcdQualifierResolver implements QualifierResolver<Mrhier> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.QualifierResolver#getQualifierName()
	 */
	public String getQualifierName(Mrhier item) {
		return RrfLoaderConstants.HCD_QUALIFIER;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.QualifierResolver#getQualifierValue(java.lang.Object)
	 */
	public Text getQualifierValue(Mrhier item) {
		return DaoUtility.createText(item.getHcd() + ":" + item.getPtr());
	}
}