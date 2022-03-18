
package org.lexgrid.loader.data.property;

import java.util.UUID;

/**
 * The Class RandomGuidIndividualIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class RandomGuidIndividualIdSetter<I> implements IndividualIdSetter<I> {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.IndividualIdSetter#addId(java.lang.Object)
	 */
	public String addId(I item) {
		return UUID.randomUUID().toString();
	}
}