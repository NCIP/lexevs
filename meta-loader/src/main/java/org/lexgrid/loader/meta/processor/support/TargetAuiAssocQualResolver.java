
package org.lexgrid.loader.meta.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.processor.support.AbstractAssociationQualifierResolver;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class TargetAuiAssocQualResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class TargetAuiAssocQualResolver extends AbstractAssociationQualifierResolver<Mrrel>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AbstractNullValueSkippingOptionalQualifierResolver#getQualifierName()
	 */
	public String getQualifierName(Mrrel item) {
		return MetaLoaderConstants.TARGET_AUI_QUALIFIER;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.AbstractNullValueSkippingOptionalQualifierResolver#getQualifierValue(java.lang.Object)
	 */
	public Text getQualifierValue(Mrrel item) {
		return DaoUtility.createText(item.getAui1());
	}
}