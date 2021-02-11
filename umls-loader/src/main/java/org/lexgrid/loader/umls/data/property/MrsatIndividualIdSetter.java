
package org.lexgrid.loader.umls.data.property;

import org.lexgrid.loader.data.property.IndividualIdSetter;
import org.lexgrid.loader.rrf.model.Mrsat;

/**
 * The Class MrsatIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrsatIndividualIdSetter implements IndividualIdSetter<Mrsat> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Mrsat item){
		return item.getAtui();
	}

}