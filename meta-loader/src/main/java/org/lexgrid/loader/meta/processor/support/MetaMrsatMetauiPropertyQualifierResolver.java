
package org.lexgrid.loader.meta.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * @author <a href="mailto:scott.bauer@mayo.edu">Scott Bauer</a>
 */
public class MetaMrsatMetauiPropertyQualifierResolver extends
	AbstractPropertyQualifierResolver<Mrsat> {

	public String getQualifierName(Mrsat item) {
		return MetaLoaderConstants.METAUI_QUALIFIER;
	}

	public Text getQualifierValue(Mrsat item) {
		return DaoUtility.createText(item.getMetaui());
	}
}