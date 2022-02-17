
package org.lexgrid.loader.data.property;

import java.util.List;

/**
 * The Class NoopListIdSetter.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NoopListIdSetter<I> implements ListIdSetter<I>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.data.property.ListIdSetter#addIds(java.util.List)
	 */
	public void addIds(List<I> idables) {
		//do nothing	
	}
}