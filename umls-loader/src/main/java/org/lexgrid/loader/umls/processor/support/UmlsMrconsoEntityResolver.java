
package org.lexgrid.loader.umls.processor.support;

import org.LexGrid.commonTypes.types.EntityTypes;
import org.lexgrid.loader.processor.support.AbstractBasicEntityResolver;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class UmlsMrconsoEntityResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class UmlsMrconsoEntityResolver extends AbstractBasicEntityResolver<Mrconso>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getIsActive(java.lang.Object)
	 */
	public boolean getIsActive(Mrconso item) {	
		return true;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getIsAnonymous(java.lang.Object)
	 */
	public boolean getIsAnonymous(Mrconso item) {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityResolver#getIsDefined(java.lang.Object)
	 */
	public boolean getIsDefined(Mrconso item) {
		return false;
	}

	public String[] getEntityTypes(Mrconso item) {
		return new String[]{EntityTypes.CONCEPT.toString()};
	}
}