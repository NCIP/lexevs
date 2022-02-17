
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.processor.support.EntityCodeResolver;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class UmlsMrrelEntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrrelEntityCodeResolver implements EntityCodeResolver<Mrrel>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Mrrel item){
		return item.getCui1();
	}
}