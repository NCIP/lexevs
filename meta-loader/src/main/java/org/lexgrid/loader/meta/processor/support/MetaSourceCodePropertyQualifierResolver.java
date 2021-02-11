
package org.lexgrid.loader.meta.processor.support;

import org.LexGrid.commonTypes.Text;
import org.lexevs.dao.database.utility.DaoUtility;
import org.lexgrid.loader.meta.constants.MetaLoaderConstants;
import org.lexgrid.loader.processor.support.AbstractPropertyQualifierResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MetaSourceCodeMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaSourceCodePropertyQualifierResolver extends AbstractPropertyQualifierResolver<Mrconso>{

	public String getQualifierName(Mrconso item) {
		return MetaLoaderConstants.SOURCE_CODE_QUALIFIER;
	}

	public Text getQualifierValue(Mrconso item) {
		return DaoUtility.createText(item.getCode());
	}
}