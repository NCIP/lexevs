
package org.lexgrid.loader.data.association;

import junit.framework.Assert;

import org.LexGrid.relations.AssociationSource;
import org.lexgrid.loader.wrappers.ParentIdHolder;

/**
 * The Class EntityAssnsToEntityReproducibleKeyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ReproducibleAssociationInstanceIdResolver extends AbstractReproducibleIdResolver<ParentIdHolder<AssociationSource>> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.association.AbstractReproducibleKeyResolver#resolveMultiAttributesKey(java.lang.Object)
	 */
	public String resolveAssociationInstanceId(
			ParentIdHolder<AssociationSource> key) {
		Assert.assertEquals("Only one (1) AssocationSource and one (1) AssociationTarget may be processed at a time." , 
				key.getItem().getTargetCount() == 1);
		
		return super.generateKey(
			key.getParentId(),
			key.getItem().getSourceEntityCode(),
			key.getItem().getSourceEntityCodeNamespace(),
			key.getItem().getTarget()[0].getTargetEntityCode(),
			key.getItem().getTarget()[0].getTargetEntityCodeNamespace());
	}
}