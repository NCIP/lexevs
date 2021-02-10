
package org.lexgrid.loader.umls.processor.support;

import org.lexgrid.loader.rrf.constants.RrfLoaderConstants;
import org.lexgrid.loader.rrf.model.Mrrel;
import org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver;

/**
 * The Class AuiBasedRelationResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class AuiBasedRelationResolver extends AbstractRrfRelationResolver {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getSource(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getSource(Mrrel item) {
		return item.getAui1();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getTarget(org.lexgrid.loader.rrf.model.Mrrel)
	 */
	public String getTarget(Mrrel item) {
		return item.getAui2();
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.rrf.processor.support.AbstractRrfRelationResolver#getContainerName()
	 */
	@Override
	public String getContainerName() {
		return RrfLoaderConstants.UMLS_RELATIONS_NAME;
	}	
}