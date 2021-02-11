
package org.lexgrid.loader.processor.support;

import org.apache.commons.lang.StringUtils;

/**
 * The Class AbstractNullValueSkippingOptionalMultiAttribResolver.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractNullValueSkippingResolver<T> implements OptionalResolver<T>{

	/* (non-Javadoc)
	 * @see org.lexgrid.loader.processor.support.OptionalMultiAttribResolver#toProcess(java.lang.Object)
	 */
	public boolean toProcess(T item) {
		return StringUtils.isNotBlank(getValueToCheckForNull(item));
	}
	
	protected abstract String getValueToCheckForNull(T item);
}