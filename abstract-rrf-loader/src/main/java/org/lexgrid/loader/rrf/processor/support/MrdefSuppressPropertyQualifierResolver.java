
package org.lexgrid.loader.rrf.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefSuppressMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefSuppressPropertyQualifierResolver extends AbstractSuppressPropertyQualifierResolver<Mrdef>{

	public Text getQualifierValue(Mrdef item) {
		return DaoUtility.createText(item.getSuppress());
	}

}