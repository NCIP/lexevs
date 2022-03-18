
package org.lexgrid.loader.meta.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.processor.support.OptionalQualifierResolver;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class SelfReferencingAssocQualResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class SelfReferencingAssocQualResolver implements OptionalQualifierResolver<Mrrel>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.QualifierResolver#getQualifierName()
	 */
	public String getQualifierName(Mrrel item) {
		return MetaLoaderConstants.SELF_REFERENCING_QUALIFIER;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.QualifierResolver#getQualifierValue(java.lang.Object)
	 */
	public Text getQualifierValue(Mrrel item) {
		return DaoUtility.createText(MetaLoaderConstants.SELF_REFERENCING_QUALIFIER_TRUE_VALUE);
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.OptionalQualifierResolver#toProcess(java.lang.Object)
	 */
	public boolean toProcess(Mrrel item) {
		return item.getCui1().equals(item.getCui2());
	}	
}