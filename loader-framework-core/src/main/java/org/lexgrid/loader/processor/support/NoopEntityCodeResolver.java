
package org.lexgrid.loader.processor.support;

/**
 * The Class NoopEntityCodeResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class NoopEntityCodeResolver implements EntityCodeResolver {

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.EntityCodeResolver#getEntityCode(java.lang.Object)
	 */
	public String getEntityCode(Object item) {
		return null;
	}
}