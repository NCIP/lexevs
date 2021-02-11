
package org.lexgrid.loader.rrf.data.association;

import org.lexgrid.loader.data.association.AssociationInstanceIdResolver;
import org.lexgrid.loader.rrf.model.Mrrel;

/**
 * The Class MrrelRuiKeyResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrrelRuiAssociationInstanceIdResolver implements AssociationInstanceIdResolver<Mrrel> {
	
	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.association.AssociationInstanceIdResolver#resolveMultiAttributesKey(java.lang.Object)
	 */
	public String resolveAssociationInstanceId(Mrrel key) {
		return key.getRui();
	}
}