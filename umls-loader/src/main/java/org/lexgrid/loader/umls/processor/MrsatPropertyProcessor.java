
package org.lexgrid.loader.umls.processor;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.processor.EntityPropertyProcessor;
import org.lexgrid.loader.rrf.model.Mrsat;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class MrsatPropertyProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatPropertyProcessor extends EntityPropertyProcessor<Mrsat>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.EntityPropertyProcessor#process(java.lang.Object)
	 */
	/**
	 * Mrsat Properties are always set to 'isPreferred' = false;
	 * This processor assumes that all skippable entries in Mrsat have been skipped.
	 * 
	 * @param item the item
	 * 
	 * @return the entity property
	 * 
	 * @throws Exception the exception
	 */
	@Override
	public ParentIdHolder<Property> doProcess(Mrsat item) throws Exception {
		ParentIdHolder<Property> prop = super.doProcess(item);
		
		return prop;
	}
}