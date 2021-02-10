
package org.lexgrid.loader.umls.data.property;
import org.lexgrid.loader.data.property.IndividualIdSetter;
import org.lexgrid.loader.rrf.model.Mrconso;

/**
 * The Class MrconsoAuiIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class MrconsoAuiIndividualIdSetter implements IndividualIdSetter<Mrconso> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Mrconso mrconso){
		return mrconso.getAui();
	}
}