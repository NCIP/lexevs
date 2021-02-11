
package org.lexgrid.loader.meta.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MetaMrconsoEntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaMrconsoEntityCodeResolver implements EntityCodeResolver<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrconso item){
		return item.getCui();
	}
}