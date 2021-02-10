
package org.lexgrid.loader.umls.processor;

import org.LexGrid.commonTypes.Property;
import org.lexgrid.loader.processor.EntityPropertyProcessor;
import org.lexgrid.loader.rrf.model.Mrsty;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class MrstyPropertyProcessor.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrstyPropertyProcessor extends EntityPropertyProcessor<Mrsty>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.EntityPropertyProcessor#process(java.lang.Object)
	 */
	/**
	 * Mrsty Properties are always set to 'isPreferred' = false;
	 * This processor assumes that all skippable entries in Mrsty have been skipped.
	 * 
	 * @param item the item
	 * 
	 * @return the entity property
	 * 
	 * @throws Exception the exception
	 */
	@Override
	public ParentIdHolder<Property> doProcess(Mrsty item) throws Exception {
		ParentIdHolder<Property> prop = super.doProcess(item);

		return prop;
	}
}