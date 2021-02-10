
package org.lexgrid.loader.meta.staging.processor;

import org.lexgrid.loader.rrf.model.Mrconso;
import org.lexgrid.loader.staging.processor.AbstractKeyValueProcessor;

/**
 * The Class AuiToCuiProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AuiToCuiProcessor extends AbstractKeyValueProcessor<Mrconso> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.staging.processor.AbstractKeyValueProcessor#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Mrconso item) {
		return item.getAui();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.staging.processor.AbstractKeyValueProcessor#getValue(java.lang.Object)
	 */
	@Override
	public String getValue(Mrconso item) {
		return item.getCui();
	}

}