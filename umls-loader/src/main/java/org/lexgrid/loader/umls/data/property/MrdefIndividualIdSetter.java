
package org.lexgrid.loader.umls.data.property;

import org.lexgrid.loader.data.property.IndividualIdSetter;
import org.lexgrid.loader.rrf.model.Mrdef;

/**
 * The Class MrdefIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrdefIndividualIdSetter implements IndividualIdSetter<Mrdef> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Mrdef item){
		return item.getAtui();
	}
}