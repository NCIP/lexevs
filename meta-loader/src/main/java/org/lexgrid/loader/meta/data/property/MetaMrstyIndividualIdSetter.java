
package org.lexgrid.loader.meta.data.property;

import org.lexgrid.loader.data.property.IndividualIdSetter;
import org.lexgrid.loader.rrf.model.Mrsty;

/**
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MetaMrstyIndividualIdSetter implements IndividualIdSetter<Mrsty> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Mrsty item) {
		return item.getAtui();
	}
}