
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MrconsoSuppressPropertyQualifierResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoSuppressPropertyQualifierResolver extends AbstractSuppressPropertyQualifierResolver<Mrconso>{

	public Text getQualifierValue(Mrconso item) {
		return DaoUtility.createText(item.getSuppress());
	}

}