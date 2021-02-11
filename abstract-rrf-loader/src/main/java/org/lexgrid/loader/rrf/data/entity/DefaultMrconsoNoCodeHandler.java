
package org.lexgrid.loader.rrf.data.entity;

import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class DefaultMrconsoNoCodeHandler.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class DefaultMrconsoNoCodeHandler extends AbstractMrconsoNoCodeHandler {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.data.entity.AbstractMrconsoNoCodeHandler#doHandleNoCode(org.lexgrid.loader.rrf.model.Mrconso)
	 */
	@Override
	protected String doHandleNoCode(Mrconso item) {
		return item.getCui() + ":" + item.getAui();
	}
}