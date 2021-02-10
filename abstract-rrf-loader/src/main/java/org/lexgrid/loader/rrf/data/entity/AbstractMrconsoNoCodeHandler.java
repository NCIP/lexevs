
package org.lexgrid.loader.rrf.data.entity;

import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class AbstractMrconsoNoCodeHandler.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractMrconsoNoCodeHandler implements NoCodeHandler<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.entity.NoCodeHandler#handleNoCode(java.lang.Object)
	 */
	public String handleNoCode(Mrconso item) {
		if(item.getCode().equals(RrfLoaderConstants.NO_CODE)){
			return doHandleNoCode(item);
		} else {
			return item.getCode();
		}
	}
	
	/**
	 * Do handle no code.
	 * 
	 * @param item the item
	 * 
	 * @return the string
	 */
	protected abstract String doHandleNoCode(Mrconso item);

}