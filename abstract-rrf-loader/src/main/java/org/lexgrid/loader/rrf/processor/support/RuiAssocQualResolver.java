
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.processor.support.AbstractAssociationQualifierResolver;
import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class RelaAssocQualResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RuiAssocQualResolver extends AbstractAssociationQualifierResolver<Mrrel>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AbstractNullValueSkippingOptionalQualifierResolver#getQualifierName()
	 */
	public String getQualifierName(Mrrel item) {
		return RrfLoaderConstants.RUI_QUALIFIER;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AbstractNullValueSkippingOptionalQualifierResolver#getQualifierValue(java.lang.Object)
	 */
	public Text getQualifierValue(Mrrel item) {
		return DaoUtility.createText(item.getRui());
	}
}