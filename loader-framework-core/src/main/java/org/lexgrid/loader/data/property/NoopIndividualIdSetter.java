
package org.lexgrid.loader.data.property;

/**
 * The Class NoopIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NoopIndividualIdSetter implements IndividualIdSetter {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(Object item) {
		//used if id is to be set later on.
		return null;
	}
}