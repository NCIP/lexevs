
package org.lexgrid.loader.meta.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.rrf.model.Mrsat;
import org.lexgrid.loader.rrf.processor.support.AbstractSuppressPropertyQualifierResolver;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
public class MetaMrsatSuppressPropertyQualifierResolver extends AbstractSuppressPropertyQualifierResolver<Mrsat> {
	
	public Text getQualifierValue(Mrsat item) {
		return DaoUtility.createText(item.getSuppress());
	}
}