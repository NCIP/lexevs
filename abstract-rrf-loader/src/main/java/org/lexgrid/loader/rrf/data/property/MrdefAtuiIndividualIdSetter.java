
package org.lexgrid.loader.rrf.data.property;

import org.lexgrid.loader.data.property.IndividualIdSetter;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefAtuiIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefAtuiIndividualIdSetter implements IndividualIdSetter<Mrdef> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Mrdef item){
		return item.getAtui();
	}
}